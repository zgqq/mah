package mah.common.json;

import mah.common.json.support.fastjson.FastjsonJSONFactory;
import mah.common.util.HttpUtils;

/**
 * Created by zgq on 17-1-2 下午3:58.
 */
public class JSONUtils {

    private static final JSONFactory FACTORY = new FastjsonJSONFactory();

    public static JSONObj getJSON(String url) {
        String content = HttpUtils.getContent(url);
        return FACTORY.parseObj(content);
    }

}
