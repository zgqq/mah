package mah.common.json;

/**
 * Created by zgq on 16-12-4.
 */
public interface JSONFactory {

    JSONArr parseArrFromLocalFile(String file);

    JSONObj parseObjFromLocalFile(String file);

    JSONObj parseObj(String content);

    JSONArr parseArr(String content);
}
