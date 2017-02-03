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

import com.alibaba.fastjson.JSON;
import mah.common.util.IoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class LocalUpdateTask implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalUpdateTask.class);
    private RepositoryUpdater repositoryUpdater;
    private String localFile;

    LocalUpdateTask(RepositoryUpdater repositoryUpdater, String localFile) {
        this.repositoryUpdater = repositoryUpdater;
        this.localFile = localFile;
    }

    public void run() {
        try {
            LOGGER.info("Updating store");
            UpdateResult result = repositoryUpdater.getUpdateResult();
            LOGGER.info("Finished update");
            if (result.getAddRepositoryCount() != 0) {
                LOGGER.info("Write to file {} ", localFile);
                IoUtils.writeToFile(localFile, JSON.toJSONString(result.getRepositories()));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof IOException) {
                IOException cause = (IOException) e.getCause();
                LOGGER.debug("Could not fetch github url", cause);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
