package mah.plugin.support.github.starred.sync;

import mah.plugin.support.github.entity.GithubRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class RepositoryUpdater {

    private Logger logger = LoggerFactory.getLogger(RepositoryUpdater.class);
    private volatile Future<UpdateResult> synchronizeFuture;

    private String synchURL;
    private String localStoreFile;
    private ExecutorService executorService;

    public RepositoryUpdater(ExecutorService executorService, String localStoreFile, String synchURL) {
        this.executorService = executorService;
        this.localStoreFile = localStoreFile;
        this.synchURL = synchURL;
    }

    public void checkRemote(GithubRepositories githubRepositories) {
        SynchronizeRepositoryTask synchronizeRepositoryTask = new SynchronizeRepositoryTask(githubRepositories, this.synchURL);
        synchronizeFuture = executorService.submit(synchronizeRepositoryTask);
    }

    public void updateLocal() {
        executorService.submit(new LocalUpdateTask(this, localStoreFile));
    }

    public UpdateResult getUpdateResult() throws ExecutionException, InterruptedException {
        if (synchronizeFuture == null) {
            logger.warn("Could not update local store due to remote check unavaible!");
            return null;
        }
        UpdateResult updateResult = synchronizeFuture.get();
        return updateResult;
    }


}

