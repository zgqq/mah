package mah.common.util;

import java.util.Collection;

/**
 * Created by zgq on 2017-01-11 20:20
 */
public class CollectionUtils {

    public static boolean isNotEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

}
