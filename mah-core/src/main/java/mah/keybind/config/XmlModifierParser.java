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
package mah.keybind.config;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2/9/17.
 */
public class XmlModifierParser {
    private final Document document;

    public XmlModifierParser(Document document) {
        this.document = document;
    }

    public List<ModifierConfig> parseModifiers() {
        final Document document = this.document;
        final NodeList globalKeybinds = document.getElementsByTagName("modifier");
        final int globalKeybindsLength = globalKeybinds.getLength();
        final List<ModifierConfig> globalKeybindConfigs = new ArrayList<>();
        for (int i = 0; i < globalKeybindsLength; i++) {
            Node item = globalKeybinds.item(i);
            ModifierConfig modifierConfig = parseModifier(item);
            globalKeybindConfigs.add(modifierConfig);
        }
        return globalKeybindConfigs;
    }

    private ModifierConfig parseModifier(Node item) {
        final Node nameNode = item.getAttributes().getNamedItem("name");
        final String modifier = nameNode.getNodeValue();
        final Node asNode = item.getAttributes().getNamedItem("as");
        final String as = asNode.getNodeValue();
        return ModifierConfig.of(modifier, as);
    }
}
