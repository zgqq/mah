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
package mah.mode.config;

import mah.common.util.StringUtils;
import mah.keybind.config.KeybindConfig;
import mah.keybind.config.XmlKeybindParser;
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
public class XmlModeParser extends XmlKeybindParser {

    private final Document document;

    public XmlModeParser(Document document) {
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
                KeybindConfig keybind = parseKeybind(keybindNode,mode);
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
