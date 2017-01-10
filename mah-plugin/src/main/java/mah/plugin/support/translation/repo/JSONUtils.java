package mah.plugin.support.translation.repo;

import mah.common.json.JSONArr;
import mah.common.json.JSONFactory;
import mah.common.json.support.fastjson.FastjsonJSONFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 16-12-4.
 */
public class JSONUtils {

    private static final JSONFactory jsonFactory = new FastjsonJSONFactory();

    public static JSONFactory getJSONFactory() {
        return jsonFactory;
    }


    public static List<String> toExplains(JSONArr explainsObj) {
        List<String> explains = new ArrayList<>();
        for (Object explain : explainsObj) {
            explains.add(explain.toString());
        }
        return explains;
    }



}
