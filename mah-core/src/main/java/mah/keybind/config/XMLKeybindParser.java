package mah.keybind.config;

import org.w3c.dom.Node;

/**
 * Created by zgq on 2017-01-09 09:30
 */
public class XMLKeybindParser {

    protected KeybindConfig parseKeybind(Node item) {
        KeybindConfig keybind = new KeybindConfig();
        Node bindAttr = item.getAttributes().getNamedItem("bind");
        String keys = bindAttr.getNodeValue();
        Node actionAttr = item.getAttributes().getNamedItem("action");
        String action = actionAttr.getNodeValue();
        keybind.setBind(keys);
        keybind.setAction(action);
        return keybind;
    }

}
