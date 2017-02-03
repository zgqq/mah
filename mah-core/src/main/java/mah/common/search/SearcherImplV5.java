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

package mah.common.search;


import mah.common.util.StringUtils;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zgq on 16-11-24.
 */
public class SearcherImplV5 implements Searcher {

    private SearchResultComparator comparator;

    public SearcherImplV5() {
        this.comparator = new SearchResultComparator();
    }

    private List<SearchResult> fuzzyMatch(List<? extends Searchable> data, String keyword, boolean smart) {
        List<SearchResult> searchResults = new ArrayList<>();
        if (StringUtils.isEmpty(keyword)) {
            return searchResults;
        }
        for (Searchable dataRow : data) {
            int prority = 0;
            List<String> dataCells = dataRow.fetchData();
            Map<Integer, String> matchedColumns = new HashMap<>();
            Map<Integer, List<Integer>> matchedIndexs = new HashMap<>();
            for (int k = 0; k < dataCells.size(); k++) {
                List<Integer> matchedIndex = new ArrayList<>();
                List<Node> nodes = new ArrayList<>();
                String columnData = dataCells.get(k);
                matchNodes(columnData, keyword, smart, 0, nodes);
                if (nodes.size() > 0) {
                    for (int i = 0; i < nodes.size(); i++) {
                        Node node = nodes.get(i);
                        matchedIndex.addAll(node.realIndexs);
                        prority += node.priority;
                    }
                    matchedColumns.put(k, columnData);
                    matchedIndexs.put(k, matchedIndex);
                }
            }
            if (prority > 0) {
                SearchResult searchResult = new SearchResult(dataRow, prority);
                MatchedResult matchedResult = new MatchedResult(matchedColumns, matchedIndexs);
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
        int priority;
        List<Integer> matchedIndexs;
        List<Integer> realIndexs;

        public Node(int prority, List<Integer> matchedIndexs) {
            this.priority = prority;
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

    protected void matchNodes(String content, String keyword, boolean smart, int prevLen, List<Node> nodes) {
        if (StringUtils.isEmpty(content)) {
            return;
        }
        List<Node> matchedNode = matchNodes(content, keyword, 0);
        int size = matchedNode.size();
        if (size > 0) {
            int end = 0;
            Node node = matchedNode.get(matchedNode.size() - 1);
            List<Integer> mi = node.matchedIndexs;
            if (mi.size() >= 0) {
                end = mi.get(mi.size() - 1);
            }
            Node propNode = selectOptimalNode(content, keyword, matchedNode);
            computeProp(propNode, prevLen);
            nodes.add(propNode);
            if (end >= content.length() - 1) {
                return;
            }
            matchNodes(content.substring(end + 1, content.length()), keyword, smart, prevLen + end + 1, nodes);
        }
    }

    private List<Node> matchNodes(String text, String key, int offset) {
        List<Node> matchedNodes = new ArrayList<>(3);
        List<Node> curNodes = new ArrayList<>(3);
        char keyFirstChar = key.charAt(0);
        mat:
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            List<Integer> indexs;
            if (compare(keyFirstChar, c, true)) {
                indexs = new ArrayList<>();
                // add new node when encountering first char
                curNodes.add(new Node(0, indexs));
            }
            int longestNodeIndex = getLongestNodeIndex(key, offset, curNodes, i, c);
            if (longestNodeIndex != -1) {
                // remove those nodes that are prior to longest node
                Node longestNode = curNodes.get(longestNodeIndex);
                for (int k = longestNodeIndex - 1; k >= 0; k--) {
                    curNodes.remove(k);
                }
                // retrieving completes if there is only node
                List<Integer> mmi = longestNode.matchedIndexs;
                if (mmi.size() == key.length()) {
                    matchedNodes.add(longestNode);
                    if (curNodes.size() == 1) {
                        break mat;
                    }
                    // remove matched node
                    curNodes.remove(longestNode);
                }
            }
        }
        return matchedNodes;
    }

    private void computeProp(Node propNode, int prevLen) {
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
            String substring = content.substring(start, end + 1);
            matchedIndexs = new ArrayList<>();
            int[] matchedInd = computeOptimalMatchedIndexs(substring, key);
            for (int index : matchedInd) {
                matchedIndexs.add(start + index);
            }
            node.matchedIndexs = matchedIndexs;
        }
        return node;
    }

    protected final Node selectOptimalNode(String content, String key, List<Node> matchedNodes) {
        int priority = 1;
        int ind = 0;
        for (int j = 0; j < matchedNodes.size(); j++) {
            Node node = computerProrityNode(content, key, matchedNodes.get(j));
            List<Integer> matchedIndexs = node.matchedIndexs;
            int q = 0;
            int succ = 0;
            for (int i = matchedIndexs.size() - 1; i >= 1; i--) {
                int ind1 = matchedIndexs.get(i - 1);
                int ind2 = matchedIndexs.get(i);
                if ((ind2 - ind1) == 1) {
                    ++succ;
                    q += succ * 10;
                } else {
                    succ = 0;
                }
            }
            if (q > priority) {
                ind = j;
                priority = q;
            }
        }
        Node node = matchedNodes.get(ind);
        node.priority = priority;
        return node;
    }

    private int getLongestNodeIndex(String key, int offset, List<Node> curNodes, int i, char c) {
        int longestNodeIndex = -1;
        int maxSize = -1;
        for (int j = 0; j < curNodes.size(); j++) {
            Node node = curNodes.get(j);
            List<Integer> matchedIndexs = node.matchedIndexs;
            int indexsSize = matchedIndexs.size();
            if (indexsSize < key.length()) {
                // match next char of node
                char nextChar = key.charAt(indexsSize);
                if (compare(nextChar, c, true)) {
                    matchedIndexs.add(offset + i);
                }
            }
            int size = matchedIndexs.size();
            if (size >= maxSize) {
                maxSize = size;
                longestNodeIndex = j;
            }
        }
        return longestNodeIndex;
    }

    public final int[] computeOptimalMatchedIndexs(String str, String key) {
        if (key == null) {
            throw new NullPointerException("key could not be null");
        }
        int[] pi = new int[key.length()];
        pi[0] = 0;
        pi[key.length() - 1] = str.length() - 1;
        for (int i = 1; i < pi.length - 1; i++) {
            int prev = pi[i - 1];
            char c = str.charAt(prev + 1);
            if (compare(key.charAt(i), c, true)) {
                pi[i] = prev + 1;
                continue;
            } else {
                int last = pi[i - 1];
                int curOffset = last + 1;
                List<Node> nodes = matchNodes(str.substring(curOffset), key.substring(i), curOffset);
                Node node = nodes.get(0);
                List<Integer> matchedIndexs = node.matchedIndexs;
                Integer headIndex = matchedIndexs.get(0);
                pi[i] = headIndex;
            }
        }
        return pi;
    }
}

