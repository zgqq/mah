package mah.common.search;


import mah.common.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zgq on 16-11-24.
 */
@Deprecated
public class SearcherImplV3 implements Searcher {

    private SearchResultComparator comparator;

    public SearcherImplV3() {
        this.comparator = new SearchResultComparator();
    }

    private List<SearchResult> fuzzyMatch(List<? extends Searchable> data, String keyword, boolean smart) {
        List<SearchResult> searchResults = new ArrayList<>();
        for (Searchable dataRow : data) {
            int prority = 0;
            List<String> dataCells = dataRow.fetchData();
            Map<Integer, String> matchedColumns = new HashMap<>();
            Map<Integer, List<Integer>> matchedIndexs = new HashMap<>();
            for (int k = 0; k < dataCells.size(); k++) {
                List<Integer> matchedIndex = new ArrayList<>();
                List<Node> nodes = new ArrayList<>();
                String columnData  = dataCells.get(k);
                computePrority2(columnData, keyword, smart, 0, nodes);
                if (nodes.size() > 0) {
                    for (int i = 0; i < nodes.size(); i++) {
                        Node node = nodes.get(i);
                        for (int i1 = 0; i1 < node.realIndexs.size(); i1++) {
                            matchedIndex.add(node.realIndexs.get(i1));
                        }
                        prority += node.path;
                    }
                    matchedColumns.put(k, columnData);
                    matchedIndexs.put(k, matchedIndex);
                }
            }
            if (prority > 0) {
                SearchResult searchResult = new SearchResult(dataRow, prority);
                MatchedResult matchedResult = new MatchedResult(matchedColumns,matchedIndexs);
                searchResult.setMatchedResult(matchedResult);
                searchResults.add(searchResult);
            }
        }
        searchResults.sort(comparator);
        return searchResults;
    }

    @Override
    public List<SearchResult> smartFuzzyMatch(List<? extends Searchable> data, String keyword) {
        return fuzzyMatch(data, keyword, true);
    }



    static class Node {
        int path;
        List<Integer> matchedIndexs;
        List<Integer> realIndexs;

        public Node(int path, List<Integer> matchedIndexs) {
            this.path = path;
            this.matchedIndexs = matchedIndexs;
        }
    }

    protected boolean compare(char c, char c2, boolean smart) {
        if (smart) {
            if (Character.isUpperCase(c)) {
                return c == c2;
            }
            return c == Character.toLowerCase(c2);
        }
        return c == c2;
    }

    protected void computePrority2(String content, String keyword, boolean smart, int prevLen, List<Node> nodes) {
        if (StringUtils.isEmpty(content) || StringUtils.isEmpty(keyword)) {
            return;
        }
        char fkc = keyword.charAt(0);
        List<Node> matchedNode = new ArrayList<>();
        List<Node> mis = new ArrayList<>();
        mat:
        for (int i1 = 0; i1 < content.length(); i1++) {
            char c = content.charAt(i1);
            List<Integer> ni;
            if (compare(fkc, c, smart)) {
                ni = new ArrayList<>();
                // add new node when encountering first char
                mis.add(new Node(0, ni));
            }

            for (Node node : mis) {
                List<Integer> oi = node.matchedIndexs;
                int os = oi.size();
                if (os < keyword.length()) {
                    // match next char of node
                    char c1 = keyword.charAt(os);
                    if (compare(c1, c, smart)) {
                        oi.add(i1);
                    }
                }
            }

            // select most node
            int ms = -1;
            int mms = -1;
            for (int i2 = 0; i2 < mis.size(); i2++) {
                Node node = mis.get(i2);
                List<Integer> mi = node.matchedIndexs;
                int size = mi.size();
                if (size >= mms) {
                    mms = size;
                    ms = i2;
                }
            }

            if (ms != -1) {
                Node node = mis.get(ms);
                for (int k = ms - 1; k > 0; k--) {
                    mis.remove(k);
                }
                List<Integer> mmi = node.matchedIndexs;
                if (mmi.size() == keyword.length()) {
                    matchedNode.add(node);
                    if (mis.size() == 1) {
                        break mat;
                    }
                    mmi.remove(node);
                }
            }
        }

        int size = matchedNode.size();
        if (size > 0) {
            int end = 0;
            Node node = matchedNode.get(matchedNode.size() - 1);
            List<Integer> mi = node.matchedIndexs;
            if (mi.size() >= 0) {
                end = mi.get(mi.size() - 1);
            }
            Node propNode = selectProrityNode(content, keyword, matchedNode);
            computeProp(propNode, prevLen);
            nodes.add(propNode);
            if (end >= content.length() - 1) {
                return;
            }
            computePrority2(content.substring(end + 1, content.length()), keyword, smart, prevLen + end + 1, nodes);
        }
    }

    private void computeProp(Node propNode,int prevLen) {
        List<Integer> matchedIndexs = propNode.matchedIndexs;
        List<Integer> realIndexs = new ArrayList();
        for (Integer matchedIndex : matchedIndexs) {
            realIndexs.add(matchedIndex + prevLen);
        }
        propNode.realIndexs = realIndexs;
    }

    protected final Node computerProrityNode(String content, String key, Node node) {
        List<Integer> matchedIndexs = node.matchedIndexs;
        if (matchedIndexs.size() > 2) {
            int start = matchedIndexs.get(0);
            int end = matchedIndexs.get(matchedIndexs.size() - 1);
            String substring = content.substring(start, end+1);
            int[] matchedInd = getMatchedIndex(substring, key);
            matchedIndexs = new ArrayList<>();
            for (int index : matchedInd) {
                matchedIndexs.add(start+index);
            }
            node.matchedIndexs = matchedIndexs;
        }
        return node;
    }

    protected final Node selectProrityNode(String content, String key, List<Node> matchedNodes) {
        int prop = 9999999;
        int ind = 0;
        for (int j = 0; j < matchedNodes.size(); j++) {
            Node node = computerProrityNode(content, key, matchedNodes.get(j));
            List<Integer> matchedIndexs = node.matchedIndexs;
            int q = 0;
            for (int i = matchedIndexs.size() - 1; i >= 1; i--) {
                int ind1 = matchedIndexs.get(i - 1);
                int ind2 = matchedIndexs.get(i);
                q += ind2 - ind1;
            }
            if (q < prop) {
                ind = j;
                prop = q;
            }
        }
        Node node = matchedNodes.get(ind);
        node.path = prop;
        return node;
    }

    public final boolean testMatch(String matchedStr, String str) {
        int i = 0;
        int j = 0;
        boolean matched = false;
        for (; i < matchedStr.length(); i++) {
            char c = matchedStr.charAt(i);
            for (; j < str.length(); j++) {
                if (compare(c,str.charAt(j),true)) {
                    if (i == matchedStr.length() - 1) {
                        matched = true;
                    }
                    j++;
                    break;
                }
            }
            if (j == str.length()) {
                break;
            }
        }
        return matched;
    }

    public final int[] getMatchedIndex(String str, String key) {
        if (key == null) {
            throw new NullPointerException("key could not be null");
        }

        int[] pi = new int[key.length()];
        pi[0] = 0;
        pi[key.length() - 1] = str.length() - 1;
        for (int i = 1; i < pi.length - 1; i++) {
            int prev = pi[i - 1];
            char c = str.charAt(prev + 1);
            if (compare(key.charAt(i),c,true)) {
                pi[i] = prev + 1;
                continue;
            }
            int offset = pi[i - 1] + 1;
            char oc = key.charAt(i);
            int mp = -1;
            int mpi = -1;
            for (int j = offset; j < str.length(); j++) {
                if (compare(oc,str.charAt(j),true)) {
                    int mn = 0;
                    int k = j + 1;
                    int u = i + 1;
                    for (; k < str.length(); k++, u++) {
                        if (u>=key.length() || str.charAt(k) != key.charAt(u)) {
                            break;
                        }
                        mn++;
                    }
                    if (mn > mp) {
                        if (u < key.length()) {
                            StringBuilder sb = new StringBuilder();
                            for (int l = u; l < key.length(); l++) {
                                sb.append(key.charAt(l));
                            }
                            String substr = str.substring(k, str.length());
                            if (testMatch(sb.toString(), substr)) {
                                mp = mn;
                                mpi = j;
                            }
                        } else {
                            mp = mn;
                            mpi = j;
                        }
                    }
                }
            }
            if (mpi != -1) {
                pi[i] = mpi;
                for (int i1 = mpi + 1; i1 < mp; i1++) {
                    pi[i1] = pi[i1 - 1] + 1;
                }
            }
        }
        return pi;
    }

}
