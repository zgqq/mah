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
package mah.plugin.support.orgnote.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import mah.common.util.IoUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 16-10-19.
 */
public class Config {
    private List<String> filenames = new ArrayList<>();
    private int fnIndex;
    private int nodeIndex;

    public boolean contains(String filename) {
        return filenames.contains(filename);
    }
    public List<String> getFilenames() {
        return filenames;
    }

    public void setFilenames(List<String> filenames) {
        this.filenames = filenames;
    }

    public int getFnIndex() {
        return fnIndex;
    }

    public void setFnIndex(int fnIndex) {
        this.fnIndex = fnIndex;
    }

    public int getNodeIndex() {
        return nodeIndex;
    }

    public void setNodeIndex(int nodeIndex) {
        this.nodeIndex = nodeIndex;
    }


    public String getCurrentFilename() {
        if (filenames.size() > 0) {
            return filenames.get(fnIndex);
        }
        return null;
    }

    public void addDataFilename(String s) {
        filenames.add(s);
    }

    public boolean dataEmpty() {
        return filenames.size() == 0;
    }

    public static void update(Config config, List<JSONObject> list,String path) {
        if (config.getNodeIndex() >= list.size() - 1) {
            if (config.getFnIndex() + 1 >= config.getFilenames().size()) {
                config.setFnIndex(0);
            } else {
                config.setFnIndex(config.getFnIndex() + 1);
            }
            config.setNodeIndex(0);
        } else {
            config.setNodeIndex(config.getNodeIndex() + 1);
        }
        try {
            IoUtils.writeToFile(path, JSON.toJSONString(config));
        } catch (IOException e) {
            new RuntimeException(e);
        }
    }

    public static void update(String path) {
        update(Config.config,Config.list,path);
    }

    private static Config config;
    private static List<JSONObject> list;
    public static final void setSource(Config config, List<JSONObject> list) {
       Config.config = config;
        Config.list = list;
    }

    public static void updateReviewList(String dataDir) {
        updateReviewList(Config.config,Config.list,dataDir);
    }

    public static void updateReviewList(Config config,List<JSONObject> list,String dataDir) {
        try {
            IoUtils.writeToFile(dataDir + "/" + config.getCurrentFilename(), JSON.toJSONString(list));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
