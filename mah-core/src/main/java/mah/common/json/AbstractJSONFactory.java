package mah.common.json;

import mah.common.util.IOUtils;

import java.io.IOException;

/**
 * Created by zgq on 16-12-4.
 */
public abstract class AbstractJSONFactory implements JSONFactory {
    @Override
    public JSONArr parseArrFromLocalFile(String file) {
        try {
            String content = IOUtils.getStringByFilename(file);
            return parseArr(content);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }


    @Override
    public JSONObj parseObjFromLocalFile(String file) {
        try {
            String content = IOUtils.getStringByFilename(file);
            return parseObj(content);
        } catch (IOException e) {
            throw new JSONException(e);
        }
    }
}
