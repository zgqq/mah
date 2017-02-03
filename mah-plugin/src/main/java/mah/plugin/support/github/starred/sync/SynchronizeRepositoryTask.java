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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import mah.common.util.IoUtils;
import mah.plugin.support.github.entity.GithubRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

public class SynchronizeRepositoryTask implements Callable<UpdateResult> {
    private final GithubRepositories starredRepositories;
    private Logger logger = LoggerFactory.getLogger(SynchronizeRepositoryTask.class);
    private String syncUrl;

    public SynchronizeRepositoryTask(GithubRepositories starredRepositories, String syncUrl) {
        if (starredRepositories == null) {
            throw new NullPointerException("User store wrapper is null");
        }
        this.starredRepositories = starredRepositories;
        this.syncUrl = syncUrl;
    }

    @Override
    public UpdateResult call() throws Exception {
        boolean needInit = false;
        String firstRepo;
        if (starredRepositories.size() <= 0) {
            needInit = true;
        } else {
            firstRepo = starredRepositories.getFirstRepositoryName();
            logger.info("The local latest store {}", firstRepo);
        }
        int pageIndex = 0;
        int addCount = 0;
        loop:
        while (true) {
            URL starredUrl;
            ++pageIndex;
            starredUrl = new URL(this.syncUrl + "&page=" + pageIndex + "&per_page=5");
            HttpURLConnection connection = (HttpURLConnection) starredUrl.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            String content = IoUtils.toString(inputStream);
            JSONArray reps = JSON.parseArray(content);
            if ((reps.size() <= 0)) {
                break loop;
            }
            for (Object rep : reps) {
                JSONObject jsonObject = (JSONObject) rep;
                String name = jsonObject.getString("full_name");
                if (needInit) {
                    logger.debug("Add {}", name);
                    starredRepositories.addRepository(name, jsonObject.getString("description"));
                    addCount++;
                    continue;
                }
                if (starredRepositories.contains(name)) {
                    logger.info("The latest {}", name);
                    break loop;
                } else {
                    logger.debug("Add {}", name);
                    starredRepositories.updateNewRepository(name, jsonObject.getString("description"));
                    addCount++;
                }
            }
            connection.disconnect();
        }
        logger.info("Updated {} repositorys", addCount);
        UpdateResult updateResult = new UpdateResult(addCount, starredRepositories);
        return updateResult;
    }
}

