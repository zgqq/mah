package mah.common.util;

/**
 * Created by zgq on 2017-01-08 20:23
 */
public class StringUtils {

    public static boolean isNotEmpty(String text) {
        return !isEmpty(text);
    }

    public static boolean isEmpty(String text) {
        return text==null || text.trim().length()==0;
    }

}
