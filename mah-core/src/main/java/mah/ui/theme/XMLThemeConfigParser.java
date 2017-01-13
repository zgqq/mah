package mah.ui.theme;

import org.jetbrains.annotations.Nullable;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Created by zgq on 2017-01-13 21:14
 */
public class XMLThemeConfigParser {

    private Document document;

    public XMLThemeConfigParser(Document document) {
        this.document = document;
    }

    @Nullable
    public String parseTheme() {
        NodeList themeNode = document.getElementsByTagName("theme");
        int length = themeNode.getLength();
        if (length > 0) {
            Node item = themeNode.item(0);
            return item.getTextContent();
        }
        return null;
    }
}
