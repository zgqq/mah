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
