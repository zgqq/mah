package mah.common.search;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by zgq on 2017-01-11 12:31
 */
public class SearcherImplTest {

    private Searcher createSearcher() {
        SearcherImplV4 searcherImplV2 = new SearcherImplV4();
        return searcherImplV2;
    }

    private SearcherImplV4 createSearcher2() {
        return new SearcherImplV4();
    }

    @Test
    public void testPerformance() {
        SearcherImplV4 searcher = createSearcher2();
        List<DataRow> totalData = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            List<String> data = new ArrayList<>();
            data.add("fusssscssssck your methodussssssssbbbbbbbfckbbbbaaaaaaaaaaaaaaaaaaaaaaaaaaaa ");
            data.add("fuckyourfuck  sssfussssssssbbbbbbbfckbbbbaaaaaaaaaaaaaaaaaaaaaaaaaaaa ");
            DataRow dataRow = new DataRow(data);
            totalData.add(dataRow);
        }
        long start = System.currentTimeMillis();
        List<SearchResult> searchResults = searcher.smartFuzzyMatch(totalData, "fuck");
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }


    private DataRow produceData(String... texts) {
        List<String> data = new ArrayList<>();
        for (String text : texts) {
            data.add(text);
        }
        return new DataRow(data);
    }

    @Test
    public void testFuzzySearcherPosition() {

        Searcher searcher = createSearcher();
        List<String> data = new ArrayList<>();
        data.add("fuck your method");
        data.add("fuckyourfuck ");

        List<DataRow> totalData = new ArrayList<>();
        totalData.add(new DataRow(data));

        List<SearchResult> searchResults = searcher.smartFuzzyMatch(totalData, "fuck");
        SearchResult searchResult = searchResults.get(0);
        Map<Integer, List<Integer>> matchedIndexs = searchResult.getMatchedResult().getMatchedIndexs();
        Assert.assertNotNull(matchedIndexs);

        List<Integer> conIndexs = matchedIndexs.get(0);
        Assert.assertEquals(0, (int) conIndexs.get(0));
        Assert.assertEquals(1, (int) conIndexs.get(1));
        Assert.assertEquals(2, (int) conIndexs.get(2));
        Assert.assertEquals(3, (int) conIndexs.get(3));

        List<Integer> desIndexs = matchedIndexs.get(1);
        Assert.assertEquals(0, (int) desIndexs.get(0));
        Assert.assertEquals(1, (int) desIndexs.get(1));
        Assert.assertEquals(2, (int) desIndexs.get(2));
        Assert.assertEquals(3, (int) desIndexs.get(3));
        Assert.assertEquals(8, (int) desIndexs.get(4));
        Assert.assertEquals(9, (int) desIndexs.get(5));
        Assert.assertEquals(10, (int) desIndexs.get(6));
        Assert.assertEquals(11, (int) desIndexs.get(7));

        DataRow dataRow = produceData("chaaaichnaina", "ch china");
        List<SearchResult> searchResults2 = searcher.smartFuzzyMatch(Arrays.asList(dataRow), "china");
        SearchResult searchResult2 = searchResults2.get(0);
        MatchedResult matchedResult2 = searchResult2.getMatchedResult();
        Map<Integer, List<Integer>> matchedIndexs2 = matchedResult2.getMatchedIndexs();

        List<Integer> desIndexs2 = matchedIndexs2.get(1);
        Assert.assertEquals(3, (int) desIndexs2.get(0));
        Assert.assertEquals(4, (int) desIndexs2.get(1));
        Assert.assertEquals(5, (int) desIndexs2.get(2));
        Assert.assertEquals(6, (int) desIndexs2.get(3));
        Assert.assertEquals(7, (int) desIndexs2.get(4));

        List<Integer> conIndexs2 = matchedIndexs2.get(0);
        Assert.assertEquals(6, (int) conIndexs2.get(0));
        Assert.assertEquals(7, (int) conIndexs2.get(1));
        Assert.assertEquals(10, (int) conIndexs2.get(2));
        Assert.assertEquals(11, (int) conIndexs2.get(3));
        Assert.assertEquals(12, (int) conIndexs2.get(4));


        DataRow dataRow1 = produceData("chachina", "chchaaaina");
        List<SearchResult> searchResults3 = searcher.smartFuzzyMatch(Arrays.asList(dataRow1), "china");
        SearchResult searchResult3 = searchResults3.get(0);
        Map<Integer, List<Integer>> matchedIndexs3 = searchResult3.getMatchedResult().getMatchedIndexs();

        List<Integer> desIndexs3 = matchedIndexs3.get(1);
        Assert.assertEquals(2, (int) desIndexs3.get(0));
        Assert.assertEquals(3, (int) desIndexs3.get(1));
        Assert.assertEquals(7, (int) desIndexs3.get(2));
        Assert.assertEquals(8, (int) desIndexs3.get(3));
        Assert.assertEquals(9, (int) desIndexs3.get(4));

        List<Integer> conIndexs3 = matchedIndexs3.get(0);
        Assert.assertEquals(3, (int) conIndexs3.get(0));
        Assert.assertEquals(4, (int) conIndexs3.get(1));
        Assert.assertEquals(5, (int) conIndexs3.get(2));
        Assert.assertEquals(6, (int) conIndexs3.get(3));
        Assert.assertEquals(7, (int) conIndexs3.get(4));


        DataRow dataRow2 = produceData("Blankj/AndroidUtilCode");
        List<SearchResult> searchResults4 = searcher.smartFuzzyMatch(Arrays.asList(dataRow2), "Baidu");
        SearchResult searchResult4 = searchResults4.get(0);
        Map<Integer, List<Integer>> matchedIndexs4 = searchResult4.getMatchedResult().getMatchedIndexs();

        List<Integer> desIndexs4 = matchedIndexs4.get(0);
        Assert.assertEquals(0, (int) desIndexs4.get(0));
        Assert.assertEquals(2, (int) desIndexs4.get(1));
        Assert.assertEquals(12, (int) desIndexs4.get(2));
        Assert.assertEquals(13, (int) desIndexs4.get(3));
        Assert.assertEquals(14, (int) desIndexs4.get(4));


        DataRow dataRow3 = produceData("Mrs4s/BaiduPanDownload");
        List<SearchResult> searchResults5 = searcher.smartFuzzyMatch(Arrays.asList(dataRow3), "Baidupan");
        SearchResult searchResult5 = searchResults5.get(0);
        Map<Integer, List<Integer>> matchedIndexs5 = searchResult5.getMatchedResult().getMatchedIndexs();

        List<Integer> desIndexs5 = matchedIndexs5.get(0);
        Assert.assertEquals(6, (int) desIndexs5.get(0));
        Assert.assertEquals(7, (int) desIndexs5.get(1));
        Assert.assertEquals(8, (int) desIndexs5.get(2));
        Assert.assertEquals(9, (int) desIndexs5.get(3));
        Assert.assertEquals(10, (int) desIndexs5.get(4));
        Assert.assertEquals(11, (int) desIndexs5.get(5));
        Assert.assertEquals(12, (int) desIndexs5.get(6));
        Assert.assertEquals(13, (int) desIndexs5.get(7));

        List<SearchResult> searchResults6 = searcher.smartFuzzyMatch(Arrays.asList(dataRow3), "Baipan");
        SearchResult searchResult6 = searchResults6.get(0);
        Map<Integer, List<Integer>> matchedIndexs6 = searchResult6.getMatchedResult().getMatchedIndexs();

        List<Integer> desIndexs6 = matchedIndexs6.get(0);
        Assert.assertEquals(6, (int) desIndexs6.get(0));
        Assert.assertEquals(7, (int) desIndexs6.get(1));
        Assert.assertEquals(8, (int) desIndexs6.get(2));
        Assert.assertEquals(11, (int) desIndexs6.get(3));
        Assert.assertEquals(12, (int) desIndexs6.get(4));
        Assert.assertEquals(13, (int) desIndexs6.get(5));
//        Mrs4s/BaiduPanDownload

        DataRow dataRow4 = produceData("chchina");
        List<SearchResult> searchResults7 = searcher.smartFuzzyMatch(Arrays.asList(dataRow4), "china");
        SearchResult searchResult7 = searchResults7.get(0);
        Map<Integer, List<Integer>> matchedIndexs7 = searchResult7.getMatchedResult().getMatchedIndexs();

        List<Integer> desIndexs7 = matchedIndexs7.get(0);
        Assert.assertEquals(2, (int) desIndexs7.get(0));
        Assert.assertEquals(3, (int) desIndexs7.get(1));
        Assert.assertEquals(4, (int) desIndexs7.get(2));
        Assert.assertEquals(5, (int) desIndexs7.get(3));
        Assert.assertEquals(6, (int) desIndexs7.get(4));


        List<Integer> matchedPosition7 = getMatchedPosition("emacs-china","china");
        Assert.assertEquals(6, (int) matchedPosition7.get(0));
        Assert.assertEquals(7, (int) matchedPosition7.get(1));
        Assert.assertEquals(8, (int) matchedPosition7.get(2));
        Assert.assertEquals(9, (int) matchedPosition7.get(3));
        Assert.assertEquals(10, (int) matchedPosition7.get(4));

    }


    private List<Integer> getMatchedPosition(String data,String keyword) {
        Searcher searcher = createSearcher();
        DataRow dataRow = produceData(data);
        List<SearchResult> searchResults = searcher.smartFuzzyMatch(Arrays.asList(dataRow), keyword);
        SearchResult searchResult = searchResults.get(0);
        Map<Integer, List<Integer>> matchedIndexs = searchResult.getMatchedResult().getMatchedIndexs();
        List<Integer> desIndexs = matchedIndexs.get(0);
        return desIndexs;
    }

}
