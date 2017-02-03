/**
 * MIT License
 *
 * Copyright (c) 2017 zgqq
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package mah.common.json.support.fastjson;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import mah.common.json.JsonArr;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.stream.Stream;

/**
 * Created by zgq on 16-12-4.
 */
public class FastjsonArrAdapter extends AbstractList implements JsonArr {

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
