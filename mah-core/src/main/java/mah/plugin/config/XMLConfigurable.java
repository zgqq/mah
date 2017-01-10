package mah.plugin.config;


import org.w3c.dom.Node;

/**
 * Created by zgq on 2017-01-08 20:35
 */
public interface XMLConfigurable {

    /**
     * Will be invoked before method(configure) of the plugin implementing this interface.
     * In other words,this method will be invoked very early.
     * @param node
     * @throws Exception
     */
    void configure(Node node) throws Exception;


}
