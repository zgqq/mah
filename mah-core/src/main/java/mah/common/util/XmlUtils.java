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
package mah.common.util;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by zgq on 2017-01-08 20:04
 */
public final class XmlUtils {
    private XmlUtils() {}

    public static String getChildNodeText(Node parent, String tag) {
        NodeList childNodes = parent.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeName().equals(tag)) {
                return item.getTextContent();
            }
        }
        return "";
    }

    public static Document getDocument(InputStream inputStream) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            return doc;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Document getDocument(File file) {
        try {
            FileInputStream inputStream = new FileInputStream(file);
            return getDocument(inputStream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isNode(Node node, String nodeName) {
        return node.getNodeName().equals(nodeName);
    }

    public static String getAttribute(Node node, String attr) {
        Node actionAttr = node.getAttributes().getNamedItem(attr);
        return actionAttr.getNodeValue();
    }

    public static String getAttributeText(Node node, String attr) {
        Node nameNode = node.getAttributes().getNamedItem(attr);
        String layoutName = nameNode.getTextContent();
        return layoutName;
    }
}
