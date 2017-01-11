package mah.openapi.search;

import mah.common.search.SearchResult;
import mah.common.search.Searchable;
import mah.common.search.Searcher;
import mah.common.search.SearcherImplV2;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * Created by zgq on 2017-01-10 17:19
 */
public class CacheSearcher<V> {

    private final Map<String, Future<V>> SEARCH_CACHE = new ConcurrentHashMap<>();
    private final Searcher searcher;
    private final List<Searchable> data;

    public CacheSearcher(List<Searchable> data) {
        this.searcher = new SearcherImplV2();
        this.data = data;
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
