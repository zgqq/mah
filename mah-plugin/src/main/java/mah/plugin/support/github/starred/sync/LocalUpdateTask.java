package mah.plugin.support.github.starred.sync;

import com.alibaba.fastjson.JSON;
import mah.common.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;


public class LocalUpdateTask implements Runnable {
    private final static Logger logger = LoggerFactory.getLogger(LocalUpdateTask.class);
    private RepositoryUpdater repositoryUpdater;
    private String localFile;

    LocalUpdateTask(RepositoryUpdater repositoryUpdater, String localFile) {
        this.repositoryUpdater = repositoryUpdater;
        this.localFile = localFile;
    }

    public void run() {
        try {
            logger.info("Updating store");
            UpdateResult result = repositoryUpdater.getUpdateResult();
            logger.info("Finished update");
            if (result.getAddRepositoryCount() != 0) {
                logger.info("Write to file {} ", localFile);
                IOUtils.writeToFile(localFile, JSON.toJSONString(result.getRepositories()));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            if (e.getCause() instanceof IOException) {
                IOException cause = (IOException) e.getCause();
                logger.debug("Could not fetch github url", cause);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
