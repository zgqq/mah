package mah.keybind.config;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-09 09:32
 */
public class XMLGlobalKeybindParser extends XMLKeybindParser {

    private final Document document;

    public XMLGlobalKeybindParser(Document document) {
        this.document = document;
    }

    public List<KeybindConfig> parseGlobalKeybinds() {
        Document document = this.document;
        NodeList globalKeybinds = document.getElementsByTagName("globalKeybind");
        int globalKeybindsLength = globalKeybinds.getLength();
        List<KeybindConfig> globalKeybindConfigs = new ArrayList<>();
        for (int i = 0; i < globalKeybindsLength; i++) {
            Node item = globalKeybinds.item(i);
            KeybindConfig keybind = parseKeybind(item);
            globalKeybindConfigs.add(keybind);
        }
        return globalKeybindConfigs;
    }

}
