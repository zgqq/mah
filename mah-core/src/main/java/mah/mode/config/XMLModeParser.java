package mah.mode.config;

import mah.common.util.StringUtils;
import mah.keybind.config.KeybindConfig;
import mah.keybind.config.XMLKeybindParser;
import mah.mode.ModeException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zgq on 2017-01-09 13:43
 */
public class XMLModeParser extends XMLKeybindParser {

    private final Document document;

    public XMLModeParser(Document document) {
        this.document = document;
    }

    private ModeConfig parseModeConfig(Node item) {
        ModeConfig modeConfig = new ModeConfig();
        String mode = item.getAttributes().getNamedItem("name").getTextContent();
        Node parentNode = item.getAttributes().getNamedItem("parent");
        if (parentNode != null) {
            modeConfig.setParent(parentNode.getTextContent());
        }
        NodeList keybinds = item.getChildNodes();
        int actionLen = keybinds.getLength();
        List<KeybindConfig> keybindConfigs = new ArrayList<>();
        for (int j = 0; j < actionLen; j++) {
            Node keybindNode = keybinds.item(j);
            if (keybindNode.getNodeName().equals("keybind")) {
                KeybindConfig keybind = parseKeybind(keybindNode);
                keybind.setMode(mode);
                keybindConfigs.add(keybind);
            }
        }
        modeConfig.setKeybinds(keybindConfigs);
        modeConfig.setName(mode);
        return modeConfig;
    }


    private void setParent(List<ModeConfig> modeConfigs, Map<String, ModeConfig> modeNameToConfig) {
        for (ModeConfig modeConfig : modeConfigs) {
            String parent = modeConfig.getParent();
            if (!StringUtils.isEmpty(parent)) {
                ModeConfig parentMode = modeNameToConfig.get(parent);
                if (parentMode == null) {
                    throw new ModeException("Not found parent mode " + parent);
                }
                modeConfig.setParentMode(parentMode);
            }
        }
    }

    public List<ModeConfig> parseModeConfigs() {
        NodeList modes = document.getElementsByTagName("mode");
        int modeLen = modes.getLength();
        Map<String, ModeConfig> modeNameToConfig = new HashMap<>();
        List<ModeConfig> modeConfigs = new ArrayList<>();
        for (int i = 0; i < modeLen; i++) {
            Node item = modes.item(i);
            ModeConfig modeConfig = parseModeConfig(item);
            modeConfigs.add(modeConfig);
            modeNameToConfig.put(modeConfig.getName(), modeConfig);
        }
        setParent(modeConfigs, modeNameToConfig);
        return modeConfigs;
    }

}
