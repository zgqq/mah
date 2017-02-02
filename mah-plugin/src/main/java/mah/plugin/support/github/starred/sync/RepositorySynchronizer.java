/**
 * MIT License
 *
 * Copyright (c) 2017 zgqq
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
            UpdateResult updateResult = repositoryUpdater.getUpdateResult();
            searcher = new CacheSearcher(repositoryData);
            return updateResult;
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


