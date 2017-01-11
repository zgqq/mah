package mah.common.search;

import java.util.List;

/**
 * Created by zgq on 16-11-6.
 */
public interface Searcher {


    List<SearchResult> smartFuzzyMatch(List<? extends Searchable> dataRows, String keyword);

}
