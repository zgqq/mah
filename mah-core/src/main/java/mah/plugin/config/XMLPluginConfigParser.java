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
package mah.plugin.config;

import mah.app.config.ParserConfigException;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-08 19:59
 */
public class XMLPluginConfigParser {

    private final Document document;

    public XMLPluginConfigParser(Document document) {
        this.document = document;
    }

    @NotNull
    public List<? extends PluginConfig> parsePluginConfigs(List<String> pluginNames) {
        List<PluginConfig> pluginConfigs = new ArrayList<>();
        NodeList pluginConfigNode = document.getElementsByTagName("plug");
        for (int i = 0; i < pluginConfigNode.getLength(); i++) {
            Node item = pluginConfigNode.item(i);
            NamedNodeMap attributes = item.getAttributes();
            Node nameAttr = attributes.getNamedItem("name");
            if (nameAttr == null) {
                throw new ParserConfigException("ThemeConfig don't specify plugin name");
            }
            String name = nameAttr.getNodeValue();
            if (pluginNames.contains(name)) {
                List<XMLCommandConfig> commandConfigs = parseCommandConfigs(name,item);
                PluginConfig pluginConfig = new PluginConfig();
                pluginConfig.setName(name);
                pluginConfig.setCommandConfigs(commandConfigs);
                pluginConfigs.add(pluginConfig);
            }
        }
        return pluginConfigs;
    }

    private List<XMLCommandConfig> parseCommandConfigs(String pluginName,Node item) {
        List<XMLCommandConfig> commandConfigs = new ArrayList<>();
        NodeList commandNodes = item.getChildNodes();
        for (int i = 0; i < commandNodes.getLength(); i++) {
            Node itemNode = commandNodes.item(i);
            if (itemNode.getNodeName().equals("command")) {
                Node commandNameAttr = itemNode.getAttributes().getNamedItem("name");
                NodeList commandNode = itemNode.getChildNodes();
                String commandName = commandNameAttr.getTextContent();
                String key = null;
                Node configNode = null;
                for (int j = 0; j < commandNode.getLength(); j++) {
                    Node commandChildNode = commandNode.item(j);
                    String nodeName = commandChildNode.getNodeName();
                    if (nodeName.equals("trigger")) {
                        key = commandChildNode.getAttributes().getNamedItem("key").getTextContent();
                    } else if (nodeName.equals("config")) {
                        configNode = commandChildNode;
                    }
                }
                XMLCommandConfig commandConfig = new XMLCommandConfig(pluginName,commandName,key,configNode);
                commandConfigs.add(commandConfig);
            }
        }
        return commandConfigs;
    }

    @NotNull
    public List<String> parseActivePlugins() {
        Document doc = document;
        NodeList pluginNodes = doc.getElementsByTagName("plugs");
        List<String> activePlugins = parseActivePluginNames(pluginNodes);
        return activePlugins;
    }

    @NotNull
    private List<String> parseActivePluginNames(NodeList pluginNodes) {
        int length = pluginNodes.getLength();
        List<String> pluginNames = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Node items = pluginNodes.item(i);
            NodeList childNodes = items.getChildNodes();
            for (int j = 0; j < childNodes.getLength(); j++) {
                String pluginName = childNodes.item(j).getTextContent();
                pluginNames.add(pluginName);
            }
        }
        return pluginNames;
    }

}
