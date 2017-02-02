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
package mah.plugin.support.translation.parser;

import mah.common.json.JSONArr;
import mah.common.json.JSONFactory;
import mah.common.json.JSONObj;
import mah.plugin.support.translation.Word;
import mah.plugin.support.translation.repo.JSONUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 16-12-4.
 */
public class YoudaoParser implements WordParser {

    private JSONFactory jsonFactory;

    public YoudaoParser(JSONFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Override
    public Word parseWord(String content) {
        JSONObj jsonObj = jsonFactory.parseObj(content);
        Word word = new Word();
        word.setWord(jsonObj.getString("query"));
        List<String> translation = jsonObj.getJSONArr("translation");
        word.setTranslations(translation);
        JSONObj basic = jsonObj.getJSONObj("basic");
        if (basic != null) {
            word.setExplains(JSONUtils.toExplains(basic.getJSONArr("explains")));
        }
        JSONArr web = jsonObj.getJSONArr("web");
        if (web != null) {
            List<Word.MeaningPair> meaningPairs = new ArrayList<>();
            for (int i = 0; i < web.size(); i++) {
                JSONObj o = (JSONObj) web.get(i);
                Word.MeaningPair meaning = new Word.MeaningPair(o.getString("key"),o.getJSONArr("value"));
                meaningPairs.add(meaning);
            }
            word.setMeaningPairs(meaningPairs);
        }
        return word;
    }

}
