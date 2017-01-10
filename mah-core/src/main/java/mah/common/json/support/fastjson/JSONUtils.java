package mah.common.json.support.fastjson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import mah.common.json.JSONArr;
import mah.common.json.JSONObj;

/**
 * Created by zgq on 16-12-4.
 */
public class JSONUtils {

    public static JSONObj createJSONObj(JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        return new FastjsonAdapter(jsonObject);
    }

    public static JSONArr createJSONArr(JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        return new FastjsonArrAdapter(jsonArray);
    }
}
