package mah.common.json.support.fastjson;

import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;

public class FastjsonIterator implements Iterator {

        private Iterator iterator;

        public FastjsonIterator(Iterator iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Object next() {
            Object next = iterator.next();
            if (next instanceof String) {
                return next;
            }
            JSONObject origin = (JSONObject) next;
            return new FastjsonAdapter(origin);
        }
    }
