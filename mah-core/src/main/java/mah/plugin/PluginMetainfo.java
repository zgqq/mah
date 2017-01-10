package mah.plugin;

/**
 * Created by zgq on 2017-01-08 20:03
 */
public class PluginMetainfo {

    private ClassLoader pluginLoader;
    private String pluginClass;
    private String name;
    private String description;
    private String author;
    private String pluginDataDir;

    public ClassLoader getPluginLoader() {
        return pluginLoader;
    }

    public void setPluginLoader(ClassLoader pluginLoader) {
        this.pluginLoader = pluginLoader;
    }

    public String getPluginClass() {
        return pluginClass;
    }

    public void setPluginClass(String pluginClass) {
        this.pluginClass = pluginClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPluginDataDir() {
        return pluginDataDir;
    }

    public void setPluginDataDir(String pluginDataDir) {
        this.pluginDataDir = pluginDataDir;
    }


}
