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
package mah.plugin.support.suggestion;

import mah.common.util.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Created by zgq on 2/14/17.
 */
public class CommandHistories {
    private final HashMap<String, Node> histories = new HashMap<>();

    public void add(String input) {
        if (StringUtils.isNotEmpty(input)) {
            Node node = histories.get(input);
            if (node == null) {
                node = new Node(input);
                Node parent = getParentNode(input);
                if (parent != null) {
                    parent.addChild(node);
                }
                histories.put(input, node);
            }
        }
    }

    @Nullable
    private Node getParentNode(String input) {
        Node node = null;
        if (input != null && input.length() > 1) {
            String prefix = input.substring(0, input.length() - 1);
            node = histories.get(prefix);
        }
        return node;
    }

    @Nullable
    private Node getNode(String input) {
        Node node = histories.get(input);
        if (node != null) {
            if (node.getChildren().size() == 0) {
                return node;
            }
            return selectOptimalNode(node);
        }
        return null;
    }

    private Node selectOptimalNode(Node parent) {
        HashSet<Node> nodes = new HashSet<>();
        leaveNodes(parent, nodes);
        int maxCount = -1;
        Node optimalNode = null;
        for (Node node : nodes) {
            int accessCount = node.getAccessCount();
            if (accessCount > maxCount) {
                optimalNode = node;
                maxCount = accessCount;
            }
        }
        return optimalNode;
    }

    private void leaveNodes(Node node, HashSet<Node> leaveNodes) {
        for (Node child : node.getChildren()) {
            if (child.getChildren().size() == 0) {
                leaveNodes.add(child);
                continue;
            }
            leaveNodes(child, leaveNodes);
        }
    }

    public Optional<Node> historyStartWith(String history) {
        return Optional.ofNullable(getNode(history));
    }

    public void increaseAccessCount(String inputedText) {
        Node node = histories.get(inputedText);
        if (node != null) {
            node.increaseAccessCount();
        }
    }

    public static class Node {
        private final String text;
        private final List<Node> children = new ArrayList<>();
        private int accessCount;

        public Node(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public List<Node> getChildren() {
            return children;
        }

        public void addChild(Node node) {
            this.children.add(node);
        }

        public int getAccessCount() {
            return accessCount;
        }

        public void increaseAccessCount() {
            accessCount++;
        }
    }
}
