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
package mah.plugin.support.translation.repo;

import com.alibaba.fastjson.JSON;
import mah.common.json.JSONArr;
import mah.common.json.JSONObj;
import mah.common.util.IOUtils;
import mah.common.util.StringUtils;
import mah.plugin.support.translation.Word;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zgq on 16-12-4.
 */
public class WordRepository {

    private final List<Word> words = new ArrayList<>();
    private final String localFile;
    private static final ConcurrentHashMap<String, Object> FILE_LOCKS = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock readLock = rwl.readLock();
    private final Lock writeLock = rwl.writeLock();
    private Object fileLock;

    public WordRepository(String localFile) {
        this.localFile = localFile;
        Object wordLock = new Object();
        fileLock = FILE_LOCKS.putIfAbsent(localFile, wordLock);
        if (fileLock == null) {
            fileLock = wordLock;
        }
    }

    public void init() {
        JSONArr map = mah.common.json.JSONUtils.parseArrFromLocalFile(localFile);
        if (map == null) {
            return;
        }
        map.forEach(entry -> {
            JSONObj wordObj = (JSONObj) entry;
            Word word = new Word();
            word.setWord(wordObj.getString("word"));
            JSONArr explainsObj = wordObj.getJSONArr("explains");
            List<String> explains = JSONUtils.toExplains(explainsObj);
            word.setExplains(explains);
            word.setLatestQueryTime(wordObj.getDate("latestQueryTime"));
            JSONArr meaningPairArr = wordObj.getJSONArr("meaningPairs");
            if (meaningPairArr != null) {
                List<Word.MeaningPair> meaningPairs = new ArrayList<>();
                for (int i = 0; i < meaningPairArr.size(); i++) {
                    JSONObj jsonObj = (JSONObj) meaningPairArr.get(i);
                    String key = jsonObj.getString("key");
                    List<String> value =jsonObj.getJSONArr("value");
                    Word.MeaningPair meaningPair = new Word.MeaningPair(key, value);
                    meaningPairs.add(meaningPair);
                }
                word.setMeaningPairs(meaningPairs);
            }
            JSONArr translations = wordObj.getJSONArr("translations");
            word.setTranslations(translations);
            words.add(word);
        });
    }

    public void save() {
        synchronized (fileLock) {
            try {
                IOUtils.writeToFile(localFile, JSON.toJSONString(words));
            } catch (IOException e) {
                throw new RepositoryException(e);
            }
        }
    }

    public Word findWord(String wordStr) {
        if (StringUtils.isEmpty(wordStr))
            return null;
        readLock.lock();
        try{
            for (Word word : words) {
                if (word.getWord().equals(wordStr)) {
                    return word;
                }
            }
        }finally {
            readLock.unlock();
        }
        return null;
    }

    public void addWord(Word word) {
        writeLock.lock();
        try{
            Word oldWord = findWord(word.getWord());
            if (oldWord != null) {
                oldWord.incrementQueryCount();
                oldWord.setLatestQueryTime(new Date());
            } else {
                word.setLatestQueryTime(new Date());
                words.add(word);
            }
        } finally {
            writeLock.unlock();
        }
        save();
    }

    public List<Word> list(int count) {
        if (count > words.size()) {
            return words;
        }
        return words.subList(words.size()-count, words.size()-1);
    }
}
