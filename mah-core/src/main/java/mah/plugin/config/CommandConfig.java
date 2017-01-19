package mah.plugin.config;

import mah.common.util.StringUtils;
import mah.plugin.PluginException;

/**
 * Created by zgq on 2017-01-08 20:22
 */
public class CommandConfig {

    private final String pluginName;
    private final String commandName;
    private final String triggerKey;


    public CommandConfig(String pluginName, String commandName, String triggerKey) {
        if (StringUtils.isEmpty(pluginName)) {
            throw new PluginException("Plugin name must not be null!");
        }
        if (StringUtils.isEmpty(commandName)) {
            throw new PluginException("Command name must not be null!");
        }
        if (StringUtils.isEmpty(triggerKey)) {
            throw new PluginException("Trigger key must not be null!");
        }
        this.pluginName = pluginName;
        this.commandName = commandName;
        this.triggerKey = triggerKey;
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getCommandName() {
        return commandName;
    }

    public String getTriggerKey() {
        return triggerKey;
    }

}
