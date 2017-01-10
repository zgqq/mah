package mah.plugin.config;

import java.util.List;

/**
 * Created by zgq on 2017-01-08 20:00
 */
public class PluginConfig {

    private List<? extends CommandConfig> commandConfigs;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<? extends CommandConfig> getCommandConfigs() {
        return commandConfigs;
    }

    public void setCommandConfigs(List<? extends CommandConfig> commandConfigs) {
        this.commandConfigs = commandConfigs;
    }
}
