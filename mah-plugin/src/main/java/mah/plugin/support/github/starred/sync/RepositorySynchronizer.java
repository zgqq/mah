package mah.plugin.support.github.starred.sync;

import mah.app.ExecutorServices;
import mah.common.search.SearchResult;
import mah.openapi.search.CacheSearcher;
import mah.plugin.support.github.entity.GithubRepositories;
import mah.plugin.support.github.entity.GithubRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zgq on 2017-01-13 10:32
 */
public class RepositorySynchronizer implements GithubRepositories.Listener {

    private List<GithubRepository> repositoryData;
    private RepositoryUpdater repositoryUpdater;
    private SynchronizerListener listener;
    private static final Logger logger = LoggerFactory.getLogger(RepositorySynchronizer.class);

    private Lock lock = new ReentrantLock();
    private volatile boolean init;
    private Lock initLock = new ReentrantLock();
    private volatile CacheSearcher<List<SearchResult>> searcher;

    public RepositorySynchronizer(List<GithubRepository> githubRepositories, ExecutorService executor, String localRepositoryFile, String starredRepositoryAPI, SynchronizerListener listener) {
        repositoryUpdater = new RepositoryUpdater(executor, localRepositoryFile, starredRepositoryAPI);
        int delay = 0;
        if (githubRepositories == null) {
            repositoryData = new CopyOnWriteArrayList();
            ExecutorServices.submit(() -> {
                init();
            });
            delay = 5;
        } else {
            repositoryData = new CopyOnWriteArrayList<>(githubRepositories);
            searcher = new CacheSearcher(repositoryData);
        }
        this.listener = listener;
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            updateRepositories();
        }, delay, 5, TimeUnit.MINUTES);
    }

    public void fetchRepositories(int size) {
        initLock.lock();
        try {
            List<GithubRepository> repositories = new ArrayList<>();
            int i = 0;
            List<GithubRepository> repositoryData = getRepositoryData();
            for (GithubRepository githubRepository : repositoryData) {
                repositories.add(githubRepository);
                if (++i >= size) {
                    break;
                }
            }
            listener.fetchRepositories(repositoryData);
        } finally {
            initLock.unlock();
        }
    }

    private void init() {
        initLock.lock();
        try {
            listener.startInitialization();
            UpdateResult updateResult;
            lock.lock();
            try {
                init = true;
                repositoryData.clear();
                updateResult = updateRepositories();
                init = false;
            } finally {
                lock.unlock();
            }
            listener.endInitialization(updateResult);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            initLock.unlock();
        }
    }

    public void clear() {
        init();
    }

    public CacheSearcher<List<SearchResult>> getSearcher() {
        initLock.lock();
        try {
            return searcher;
        } finally {
            initLock.unlock();
        }
    }

    @Override
    public void onRepositoryAdded(GithubRepository repository) {
        listener.repositoryAdded(repository);
    }

    public List<GithubRepository> getRepositoryData() {
        initLock.lock();
        try {
            return repositoryData;
        } finally {
            initLock.unlock();
        }
    }


    private synchronized void sychronizeRepositories(GithubRepositories githubRepositories) {
        repositoryUpdater.checkRemote(githubRepositories);
        repositoryUpdater.updateLocal();
    }


    private UpdateResult updateRepositories() {
        lock.lock();
        try {
            GithubRepositories githubRepositories = new GithubRepositories(repositoryData);
            githubRepositories.addListener(this);
            sychronizeRepositories(githubRepositories);
            if (searcher != null) {
                searcher.clear();
            }
            return repositoryUpdater.getUpdateResult();
        } catch (Exception e) {
            throw new RuntimeException("fail to update repository", e);
        } finally {
            lock.unlock();
        }
    }


    public boolean isInit() {
        return init;
    }
}


