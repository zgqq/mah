package mah.common.json.support.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import mah.common.json.AbstractJSONFactory;
import mah.common.json.JSONArr;
import mah.common.json.JSONFactory;
import mah.common.json.JSONObj;

import java.util.List;

/**
 * Created by zgq on 16-12-4.
 */
public class FastjsonJSONFactory extends AbstractJSONFactory implements JSONFactory {

    @Override
    public JSONObj parseObj(String content) {
        JSONObject jsonObject = JSON.parseObject(content);
        return JSONUtils.createJSONObj(jsonObject);
    }

    @Override
    public JSONArr parseArr(String content) {
        JSONArray jsonArray = JSON.parseArray(content);
        return JSONUtils.createJSONArr(jsonArray);
    }

    @Override
    public <T> List<T> parseArr(String text, Class<T> clazz) {
        return JSON.parseArray(text,clazz);
    }
}
