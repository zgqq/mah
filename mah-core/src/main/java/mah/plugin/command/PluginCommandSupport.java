/**
 * MIT License
 *
 * Copyright (c) 2017 zgqq
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package mah.plugin.command;

import mah.command.CommandSupport;
import mah.openapi.ui.layout.LayoutFactory;
import mah.openapi.ui.layout.SwingLayoutFactory;
import mah.plugin.Plugin;
import mah.plugin.PluginMetainfo;
import mah.ui.util.UiUtils;

import java.io.File;

/**
 * Created by zgq on 2017-01-09 16:57
 */
public abstract class PluginCommandSupport extends CommandSupport implements PluginCommand {

    private Plugin plugin;
    private LayoutFactory layoutFactory;

    @Override
    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }


    protected final String getFileStoredInPluginDataDir(String file) {
        return getPluginDataDir(file);
    }

    protected final String getPluginDataDir(String dir) {
        PluginMetainfo pluginMetainfo = getPlugin().getPluginMetainfo();
        return pluginMetainfo.getPluginDataDir() + File.separator + dir;
    }


    protected LayoutFactory getLayoutFactory() {
        layoutFactory = new SwingLayoutFactory(this);
        return layoutFactory;
    }

    protected final void hideWindow() {
        UiUtils.hideWindow();
    }

}
