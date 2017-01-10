package mah.common.json.support.fastjson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import mah.common.json.JSONArr;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;

/**
 * Created by zgq on 16-12-4.
 */
public class FastjsonArrAdapter extends AbstractList implements JSONArr {

    private JSONArray jsonArray;

    public FastjsonArrAdapter(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @Override
    public Object get(int index) {
        Object o = jsonArray.get(index);
        if (o instanceof String) {
            String s = (String) o;
            return s;
        }
        return new FastjsonAdapter((JSONObject)o);
    }

    @Override
    public int hashCode() {
        return jsonArray.hashCode();
    }

    @Override
    public Iterator iterator() {
        return new FastjsonIterator(jsonArray.iterator());
    }

    @Override
    public Spliterator<Object> spliterator() {
        return jsonArray.spliterator();
    }

    @Override
    public Stream<Object> stream() {
        return jsonArray.stream();
    }

    @Override
    public Stream<Object> parallelStream() {
        return jsonArray.parallelStream();
    }


    @Override
    public int size() {
        return jsonArray.size();
    }
}
