package mah.app.config;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
 * Created by zgq on 2017-01-09 10:04
 */
public class XMLConfigParser {

    private final String path;

    public XMLConfigParser(String path) {
        this.path = path;
    }

    public Config parse() {
        File file = new File(path);
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            return new XMLConfig(doc);
        } catch (Exception e) {
            throw new ParserConfigException(e);
        }
    }
}
