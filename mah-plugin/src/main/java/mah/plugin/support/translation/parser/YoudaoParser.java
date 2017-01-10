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
