package mah.plugin;

/**
 * Created by zgq on 2017-01-08 19:52
 */
public interface Plugin {

    default void init() throws Exception{

    }

    default void prepare() throws Exception{

    }

    String getName();

    void setPluginMetainfo(PluginMetainfo pluginMetainfo);

    PluginMetainfo getPluginMetainfo();
}
