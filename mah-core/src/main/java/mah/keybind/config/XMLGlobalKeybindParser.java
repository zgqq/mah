package mah.keybind.config;

import mah.common.util.XMLUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-09 09:32
 */
public class XMLGlobalKeybindParser {

    private final Document document;

    public XMLGlobalKeybindParser(Document document) {
        this.document = document;
    }

    public List<GlobalKeybindConfig> parseGlobalKeybinds() {
        Document document = this.document;
        NodeList globalKeybinds = document.getElementsByTagName("globalKeybind");
        int globalKeybindsLength = globalKeybinds.getLength();
        List<GlobalKeybindConfig> globalKeybindConfigs = new ArrayList<>();
        for (int i = 0; i < globalKeybindsLength; i++) {
            Node item = globalKeybinds.item(i);
            GlobalKeybindConfig keybind = parseKeybind(item);
            globalKeybindConfigs.add(keybind);
        }
        return globalKeybindConfigs;
    }

    private GlobalKeybindConfig parseKeybind(Node item) {
        String listen = XMLUtils.getAttribute(item, "listen");
        String consume = XMLUtils.getAttribute(item, "consume");
        String action = XMLUtils.getAttribute(item, "action");
        return new GlobalKeybindConfig(listen, consume, action);
    }
}
