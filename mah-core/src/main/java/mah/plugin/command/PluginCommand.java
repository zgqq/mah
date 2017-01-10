package mah.plugin.command;

import mah.command.Command;
import mah.plugin.Plugin;

/**
 * Created by zgq on 2017-01-08 15:16
 */
public interface PluginCommand extends Command{

    Plugin getPlugin();

    void setPlugin(Plugin plugin);

}
