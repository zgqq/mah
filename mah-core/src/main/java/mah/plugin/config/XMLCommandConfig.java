package mah.plugin.config;


import org.w3c.dom.Node;

/**
 * Created by zgq on 2017-01-08 20:21
 */
public class XMLCommandConfig extends CommandConfig {

    private final Node customConfig;

    public XMLCommandConfig(String pluginName, String commandName, String triggerKey, Node customConfig) {
        super(pluginName, commandName, triggerKey);
        synchronized (this) {
            if (customConfig != null) {
                this.customConfig = customConfig.cloneNode(true);
            }else{
                this.customConfig = null;
            }
        }
    }

    public Node getCustomConfig() {
        synchronized (this){
            return customConfig;
        }
    }
}
