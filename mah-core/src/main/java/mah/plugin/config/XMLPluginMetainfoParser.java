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
import mah.common.util.XMLUtils;
import mah.plugin.PluginMetainfo;
import mah.plugin.loader.PluginClassLoader;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

/**
 * Created by zgq on 2017-01-08 20:02
 */
public class XMLPluginMetainfoParser {

    private final File pluginDir;
    private final InputStream metainfoFile;
    private final static PluginClassLoader pluginClassLoader = new PluginClassLoader();

    public XMLPluginMetainfoParser(File pluginDir, InputStream metainfoFile) {
        this.pluginDir = pluginDir;
        this.metainfoFile = metainfoFile;
    }

    @NotNull
    protected final List<PluginMetainfo> parsePluginMetainfosFromJars(File pluginDir, List<String> pluginNames) throws Exception {
        List<PluginMetainfo> pluginMetainfos = null;
        if (pluginDir!=null && pluginDir.isDirectory()) {
            List<String> pluginFiles = Arrays.stream(pluginDir.list((dir, name) -> {
                if (name.endsWith(".jar")) return true;
                else return false;
            })).collect(Collectors.toList());
            for (String pluginFile : pluginFiles) {
                File file = new File(pluginDir.getAbsolutePath() + File.separator + pluginFile);
                JarFile jarFile = new JarFile(file);
                JarEntry jarEntry = jarFile.getJarEntry("META-INF/plugin.xml");
                InputStream inputStream = jarFile.getInputStream(jarEntry);
                pluginMetainfos = parsePluginMetainfos(inputStream, pluginNames);
                for (PluginMetainfo pluginMetainfo : pluginMetainfos) {
                    //Load plugin
                    pluginClassLoader.loadClass(pluginMetainfo.getPluginClass(), jarFile);
                    pluginMetainfo.setPluginLoader(pluginClassLoader);
                }
            }
        }
        if (pluginMetainfos == null) {
            return new ArrayList<>();
        }
        return pluginMetainfos;
    }

    @NotNull
    public List<PluginMetainfo> parsePluginMetainfos(List<String> pluginNames) {
        try {
            List<PluginMetainfo> pluginMetainfos = parseBuiltinPluginMetainfos(pluginNames);
            List<PluginMetainfo> jarPluginMetainfos = parsePluginMetainfosFromJars(pluginDir,pluginNames);
            pluginMetainfos.addAll(jarPluginMetainfos);
            return pluginMetainfos;
        } catch (Exception e) {
            throw new ParserConfigException(e);
        }
    }

    @NotNull
    private List<PluginMetainfo> parseBuiltinPluginMetainfos(List<String> pluginNames) throws Exception {
        if (metainfoFile == null) {
            return new ArrayList<>();
        }
        Document doc = XMLUtils.getDocument(metainfoFile);
        List<PluginMetainfo> pluginMetainfos = parseMetainfos(doc, pluginNames);
        for (PluginMetainfo pluginMetainfo : pluginMetainfos) {
            pluginMetainfo.setPluginLoader(getClass().getClassLoader());
        }
        return pluginMetainfos;
    }


    private List<PluginMetainfo> parseMetainfos(Document doc, List<String> pluginNames) {
        List<PluginMetainfo> metainfos = new ArrayList<>();
        NodeList pluginNodes = doc.getElementsByTagName("plugin");
        int length = pluginNodes.getLength();
        for (int j = 0; j < length; j++) {
            Node item = pluginNodes.item(j);
            PluginMetainfo metainfo = parseMetaInfo(item);
            if(pluginNames==null  || pluginNames.contains(metainfo.getName())){
                metainfos.add(metainfo);
            }
        }
        return metainfos;
    }

    private PluginMetainfo parseMetaInfo(Node item) {
        PluginMetainfo metainfo = new PluginMetainfo();
        NodeList pluginInfoNode = item.getChildNodes();
        for (int k = 0; k < pluginInfoNode.getLength(); k++) {
            Node info = pluginInfoNode.item(k);
            String nodeName = info.getNodeName();
            if (nodeName.equals("class")) {
                metainfo.setPluginClass(info.getTextContent());
            } else if (nodeName.equals("author")) {
                metainfo.setAuthor(info.getTextContent());
            } else if (nodeName.equals("name")) {
                metainfo.setName(info.getTextContent());
            } else if (nodeName.equals("description")) {
                metainfo.setDescription(info.getTextContent());
            }
        }
        return metainfo;
    }


    protected List<PluginMetainfo> parsePluginMetainfos(InputStream inputStream,List<String> pluginNames) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(inputStream);
            return parseMetainfos(doc,pluginNames);
        } catch (Exception e) {
            throw new ParserConfigException(e);
        }
    }
}
