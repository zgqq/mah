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
package mah.plugin.support.orgnote.source;

import com.alibaba.fastjson.JSON;
import mah.common.util.IoUtils;
import mah.plugin.support.orgnote.entity.NodeEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by zgq on 10/14/16.
 */
public class Orgmode2 {
    public static final NodeEntity parseNodeEntity(String content) {
        ParserHelper parserHelper = new ParserHelper(content);
        if (parserHelper.hasNext()) {
            parserHelper.nextLine();
            if (parserHelper.isHeadline()) {
                return parseNodeEntity(parserHelper,parserHelper.getHeadlineLevel());
            }
            throw new RuntimeException("Only support headline start with *");
        }
        return new NodeEntity();
    }

    public static class ParserHelper {
        private final List<String> contents;
        private int currentLine;
        private int headlineLevel;

        public ParserHelper(String content) {
            Scanner scanner = new Scanner(content);
            contents = new ArrayList<>();
            while (scanner.hasNext()) {
                contents.add(scanner.nextLine());
            }
        }

        public boolean hasNext() {
            return contents.size() > currentLine;
        }

        public String getLine() {
            String line = contents.get(currentLine - 1);
            return line;
        }

        public boolean isHeadline() {
            String line = getLine();
            if (line.isEmpty()) {
                return false;
            }
            char c = line.charAt(0);
            if (c == '*') {
                int level = 1;
                for (; level < line.length() && '*' == line.charAt(level); level++) {
                }
                // If it is title line
                if (level < line.length() && ' ' == line.charAt(level)) {
                    headlineLevel = level;
                    return true;
                }
            }
            return false;
        }

        public int getHeadlineLevel() {
            return headlineLevel;
        }

        public void nextLine() {
            currentLine++;
        }

        public void backToPreviousLine() {
            currentLine--;
        }

        private int getCharSkipSpaceFromTail(String line) {
            for (int i = line.length() - 1; i >= 0; i--) {
                char c = line.charAt(i);
                if (c == ' ') {
                    continue;
                }
                return i;
            }
            return -1;
        }

        public int getCurrentLine() {
            return currentLine;
        }

        public String getTitle() {
            String line = getLine();
            int nospIndex = getCharSkipSpaceFromTail(line);
            if (nospIndex == -1) {
                return "";
            }

            if (line.charAt(nospIndex) != ':') {
                return line.substring(headlineLevel,line.length());
            }

            int tagStartInd = -1;
            for (int i = nospIndex - 1; i > 0; i--) {
                char c = line.charAt(i);
                if (c == ' ') {
                    break;
                } else if (c == ':') {
                    tagStartInd = i;
                }
            }
            if (tagStartInd == -1) {
                return line.substring(headlineLevel, line.length());
            }

            return line.substring(headlineLevel, tagStartInd);
        }

        public List<String> getRange(int start, int end) {
            return contents.subList(start - 1, end);
        }
    }

    public static final NodeEntity parseNodeEntity(ParserHelper parserHelper, int currentLevel) {
        NodeEntity nodeEntity = new NodeEntity();
        ArrayList<NodeEntity> childrens = null;
        boolean sameHeadline = true;
        int conStartInd = 0;
        boolean hasChild = false;
        while (parserHelper.hasNext()) {
            parserHelper.nextLine();
            if (parserHelper.isHeadline()) {
                if (parserHelper.getHeadlineLevel() < currentLevel) {
                    if (!hasChild) {
                        nodeEntity.setContentLines(parserHelper.getRange(conStartInd,
                                parserHelper.getCurrentLine() - 1));
                    }
                    parserHelper.backToPreviousLine();
                    return nodeEntity;
                } else if (parserHelper.getHeadlineLevel() > currentLevel) {
                    hasChild = true;
                    parserHelper.backToPreviousLine();
                    if (childrens == null) {
                        childrens = new ArrayList<>();
                    }
                    // Parse its children node.
                    NodeEntity child = parseNodeEntity(parserHelper, parserHelper.getHeadlineLevel());
                    childrens.add(child);
                } else if (parserHelper.getHeadlineLevel() == currentLevel) {
                    if (!sameHeadline) {
                        if (!hasChild) {
                            nodeEntity.setContentLines(parserHelper.getRange(conStartInd,
                                    parserHelper.getCurrentLine() - 1));
                        }
                        parserHelper.backToPreviousLine();
                        return nodeEntity;
                    }
                    sameHeadline = false;
                    // Parse headline
                    nodeEntity.setTitle(parserHelper.getTitle());
                    conStartInd = parserHelper.getCurrentLine() + 1;
                }
            }
        }
        nodeEntity.setChildrens(childrens);
        return nodeEntity;
    }
    public static final String toJson(String filename) throws IOException {
        String content = IoUtils.getStringByFilename(filename);
        NodeEntity nodeEntity = Orgmode2.parseNodeEntity(content);
        return JSON.toJSONString(nodeEntity.getChildrens());
    }
}
