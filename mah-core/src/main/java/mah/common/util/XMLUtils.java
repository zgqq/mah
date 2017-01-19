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
public final class XMLUtils {

    private XMLUtils() {

    }

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
        Node nameNode = node.getAttributes().getNamedItem(attr);
        String layoutName = nameNode.getTextContent();
        return layoutName;
    }

}
