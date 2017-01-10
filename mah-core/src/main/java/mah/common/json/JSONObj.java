package mah.common.json;

import java.util.Date;
import java.util.Map;

/**
 * Created by zgq on 16-12-4.
 */
public interface JSONObj extends Map<String,Object> {

    String getString(String key);

    JSONObj getJSONObj(String key);

    JSONArr getJSONArr(String key);

    Date getDate(String key);

    int getInt(String key);
}
