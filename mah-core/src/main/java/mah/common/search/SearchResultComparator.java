package mah.common.search;

import java.util.Comparator;

/**
 * Created by zgq on 16-11-7.
 */
public class SearchResultComparator implements Comparator<SearchResult>{

    @Override
    public int compare(SearchResult o1, SearchResult o2) {
        return o1.compareTo(o2);
    }

}
