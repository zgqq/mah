package mah.common.search;

/**
 * Created by zgq on 16-11-7.
 */
public class SearchResult implements Comparable<SearchResult> {

    private Searchable dataRow;
    private MatchedResult matchedResult;
    private int prority;

    public SearchResult(Searchable dataRow, int prority) {
        this.dataRow = dataRow;
        this.prority = prority;
    }


    public int getPrority() {
        return prority;
    }

    @Override
    public int compareTo(SearchResult o) {
        if (this.getPrority() < o.getPrority()) {
            return 1;
        } else if (this.getPrority() > o.getPrority()) {
            return -1;
        }
        return 0;
    }

    public Searchable getDataRow() {
        return dataRow;
    }

    public MatchedResult getMatchedResult() {
        return matchedResult;
    }

    public void setMatchedResult(MatchedResult matchedResult) {
        this.matchedResult = matchedResult;
    }
}
