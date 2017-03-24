package mah.plugin.support.search

import mah.mode.AbstractMode

/**
 * Created by zgq on 17-3-24.
 */
class SearchMode: AbstractMode("search_mode") {
    override fun init() {
        excludeAction("DefaultSelectItem");
    }
}
