package mah.plugin.command;

import mah.command.CommandSupport;
import mah.openapi.ui.layout.LayoutFactory;
import mah.openapi.ui.layout.SwingLayoutFactory;
import mah.plugin.Plugin;
import mah.plugin.PluginMetainfo;
import mah.ui.util.UIUtils;

import java.io.File;

/**
 * Created by zgq on 2017-01-09 16:57
 */
public abstract class PluginCommandSupport extends CommandSupport implements PluginCommand {

    private Plugin plugin;
    private LayoutFactory layoutFactory;

    @Override
    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }


    protected final String getFileStoredInPluginDataDir(String file) {
        return getPluginDataDir(file);
    }

    protected final String getPluginDataDir(String dir) {
        PluginMetainfo pluginMetainfo = getPlugin().getPluginMetainfo();
        return pluginMetainfo.getPluginDataDir() + File.separator + dir;
    }


    protected LayoutFactory getLayoutFactory() {
        layoutFactory = new SwingLayoutFactory(this);
        return layoutFactory;
    }

    protected final void hideWindow() {
        UIUtils.hideWindow();
    }

}
