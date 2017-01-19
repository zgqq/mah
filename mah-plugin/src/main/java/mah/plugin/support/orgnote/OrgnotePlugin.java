package mah.plugin.support.orgnote;


import mah.openapi.plugin.PluginSupport;

/**
 * Created by zgq on 16-12-19.
 */
public class OrgnotePlugin extends PluginSupport {

    @Override
    public void prepare() {
        registerCommand(new ReviewNoteCommand());
    }
}
