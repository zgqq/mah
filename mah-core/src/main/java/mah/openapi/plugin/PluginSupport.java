package mah.openapi.plugin;

import mah.command.Command;
import mah.command.CommandManager;
import mah.plugin.Plugin;
import mah.plugin.PluginMetainfo;

/**
 * Created by zgq on 16-12-24.
 */
public abstract class PluginSupport implements Plugin {

    private PluginMetainfo pluginMetainfo;

    @Override
    public final String getName() {
        return pluginMetainfo.getName();
    }

    @Override
    public PluginMetainfo getPluginMetainfo() {
        return pluginMetainfo;
    }

    @Override
    public final void setPluginMetainfo(PluginMetainfo pluginMetainfo) {
        this.pluginMetainfo = pluginMetainfo;
    }

    protected final void registerCommand(Command command) {
        CommandManager.getInstance().registerCommand(getName(), command);
    }

}
