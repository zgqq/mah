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

