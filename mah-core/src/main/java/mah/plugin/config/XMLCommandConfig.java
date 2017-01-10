package mah.plugin.config;


import org.w3c.dom.Node;

/**
 * Created by zgq on 2017-01-08 20:21
 */
public class XMLCommandConfig extends CommandConfig {

    private Node customConfig;

    public XMLCommandConfig(String pluginName, String commandName, String triggerKey, Node customConfig) {
        super(pluginName, commandName, triggerKey);
        this.customConfig = customConfig;
    }

    public Node getCustomConfig() {
        return customConfig;
    }

    public void setCustomConfig(Node customConfig) {
        this.customConfig = customConfig;
    }

}
