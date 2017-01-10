package mah.plugin;
import mah.app.ApplicationEvent;
import mah.app.ApplicationListener;
import mah.plugin.loader.SimplePluginLoader;

/**
 * Created by zgq on 2017-01-08 19:49
 */
public class PluginManager implements ApplicationListener{

    private static final PluginManager INSTANCE = new PluginManager();

    @Override
    public void afterStart(ApplicationEvent applicationEvent) throws Exception {
        SimplePluginLoader pluginLoader = new SimplePluginLoader(applicationEvent.getConfig());
        pluginLoader.start();
    }

    public static PluginManager getInstance() {
        return INSTANCE;
    }
}
