package mah.plugin.config;

import mah.app.config.Config;
import mah.app.config.XMLConfig;
import mah.plugin.PluginException;
import mah.plugin.PluginMetainfo;
import org.w3c.dom.Document;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * Created by zgq on 2017-01-08 19:58
 */
public class XMLConfigReader {

    private XMLPluginMetainfoParser pluginMetainfosParser;
    private XMLPluginConfigParser pluginConfigParser;

    public XMLConfigReader(File pluginDir, Config config) {
        if (!(config instanceof XMLConfig)) {
            throw new IllegalStateException("Config must be XMLConfig!Actual:"+config);
        }
        XMLConfig xmlConfig = (XMLConfig) config;
        try {
            Document document = xmlConfig.getDocument();
            InputStream resource = getClass().getClassLoader().getResourceAsStream("META-INF/plugin.xml");
            this.pluginMetainfosParser = new XMLPluginMetainfoParser(pluginDir,resource);
            this.pluginConfigParser = new XMLPluginConfigParser(document);
        } catch (Exception e) {
            throw new PluginException(e);
        }
    }

    public List<PluginMetainfo> getActivePluginMetainfos() {
        List<String> activePlugins = getActivePluginNames();
        List<PluginMetainfo> activePluginMetainfos = getPluginMetainfos(activePlugins);
        return activePluginMetainfos;
    }

    public List<PluginMetainfo> getPluginMetainfos(List<String> pluginNames) {
        return pluginMetainfosParser.parsePluginMetainfos(pluginNames);
    }

    public List<String> getActivePluginNames() {
        return this.pluginConfigParser.parseActivePlugins();
    }

    public List<? extends PluginConfig> parsePluginConfigs(List<String> pluginNames) {
        return pluginConfigParser.parsePluginConfigs(pluginNames);
    }

}
