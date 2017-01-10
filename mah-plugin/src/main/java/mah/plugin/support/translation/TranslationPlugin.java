package mah.plugin.support.translation;

import mah.openapi.plugin.PluginSupport;

/**
 * Created by zgq on 16-12-24.
 */
public class TranslationPlugin extends PluginSupport{

    @Override
    public void init() {
        registerCommand(new TranslationCommand());
    }

}
