package mah.plugin.support.search

import mah.openapi.plugin.PluginSupport

/**
 * Created by zgq on 2/27/17.
 */
class SearchPlugin : PluginSupport(){
    override fun prepare() {
        registerCommand(GoogleSearchCommand())
    }
}