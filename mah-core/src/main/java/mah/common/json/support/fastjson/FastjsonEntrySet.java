package mah.common.json.support.fastjson;

import com.alibaba.fastjson.JSONObject;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by zgq on 16-12-4.
 */
public class FastjsonEntrySet extends AbstractSet implements Set{

    private final Set<Map.Entry<String, Object>> entries;

    public FastjsonEntrySet(Set<Map.Entry<String, Object>> entries) {
        this.entries = entries;
    }

    @Override
    public Iterator iterator() {
        return new FastjsonIterator(this.entries.iterator());
    }
    @Override
    public int size() {
        return entries.size();
    }

}
