package mah.app.config;

import org.w3c.dom.Document;

/**
 * Created by zgq on 2017-01-08 20:24
 */
public class XMLConfig implements Config {

    private final Document document;

    public XMLConfig(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return document;
    }

}
