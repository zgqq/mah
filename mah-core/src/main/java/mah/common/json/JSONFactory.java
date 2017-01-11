package mah.common.json;

import java.util.List;

/**
 * Created by zgq on 16-12-4.
 */
public interface JSONFactory {

//    JSONArr parseArrFromLocalFile(String file);

//    JSONObj parseObjFromLocalFile(String file);

    JSONObj parseObj(String text);

    JSONArr parseArr(String text);

   <T> List<T> parseArr(String json, Class<T> clazz);

}
