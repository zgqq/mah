package mah.common.search;


import java.util.List;
import java.util.Map;

/**
 * Created by zgq on 16-11-25.
 */
public class MatchedResult {

    private final Map<Integer, String> matchedColumns;
    private final Map<Integer, List<Integer>> matchedIndexs;

    public MatchedResult(Map<Integer, String> matchedColumns, Map<Integer, List<Integer>> matchedIndexs) {
        this.matchedColumns = matchedColumns;
        this.matchedIndexs = matchedIndexs;
    }


    public List<Integer> findMatchedIndex(Integer columnIndex) {
        return matchedIndexs.get(columnIndex);
    }

    public Map<Integer, List<Integer>> getMatchedIndexs() {
        return matchedIndexs;
    }
}
