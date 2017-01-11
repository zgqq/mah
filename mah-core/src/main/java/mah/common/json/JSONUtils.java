package mah.common.json;

import mah.common.json.support.fastjson.FastjsonJSONFactory;
import mah.common.util.HttpUtils;
import mah.common.util.IOUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by zgq on 17-1-2 3:58.
 */
public class JSONUtils {

    private static final JSONFactory FACTORY = new FastjsonJSONFactory();

    private JSONUtils() {

    }


    public static JSONObj getJSON(String url) {
        String content = HttpUtils.getContent(url);
        return FACTORY.parseObj(content);
    }

    public static <T> List<T> parseArrFromLocalFile(String file, Class<T> clazz) {
        try {
            String text = IOUtils.getStringByFilename(file);
            return FACTORY.parseArr(text, clazz);
        } catch (Exception e) {
            throw new JSONException(e);
        }
    }

    public static JSONArr parseArrFromLocalFile(String file) {
        try {
            String content = IOUtils.getStringByFilename(file);
            return parseArr(content);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    public JSONObj parseObjFromLocalFile(String file) {
        try {
            String content = IOUtils.getStringByFilename(file);
            return parseObj(content);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }

    private JSONObj parseObj(String text) {
        return FACTORY.parseObj(text);
    }

    public static <T> List<T> parseArr(String text, Class<T> clazz) {
        return FACTORY.parseArr(text, clazz);
    }

    public static JSONArr parseArr(String text) {
        return FACTORY.parseArr(text);
    }
}
