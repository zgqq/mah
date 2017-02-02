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
package mah.openapi.search;

import mah.common.search.SearchResult;
import mah.common.search.Searchable;
import mah.common.search.Searcher;
import mah.common.search.SearcherImplV5;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by zgq on 2017-01-10 17:19
 */
public class CacheSearcher<V> {

    private final ConcurrentHashMap<String, Future<V>> SEARCH_CACHE = new ConcurrentHashMap<>();
    private final Searcher searcher;
    private final List<Searchable> data;

    public CacheSearcher(List<Searchable> data) {
        this.searcher = new SearcherImplV5();
        this.data = new ArrayList<>(data);
    }

    public V smartFuzzySearch(String keyword) {
        while (true) {
            Future<V> searchTask = SEARCH_CACHE.get(keyword);
            if (searchTask == null) {
                Callable<V> eval = () -> {
                    List<SearchResult> searchResults = searcher.smartFuzzyMatch(data, keyword);
                    return (V) searchResults;
                };
                FutureTask<V> ft = new FutureTask<V>(eval);
                searchTask = SEARCH_CACHE.putIfAbsent(keyword, ft);
                if (searchTask == null) {
                    searchTask = ft;
                    ft.run();
                }
            }
            try {
                return searchTask.get();
            } catch (Exception e) {
                SEARCH_CACHE.remove(keyword, searchTask);
                throw new RuntimeException(e);
            }
        }
    }

}
