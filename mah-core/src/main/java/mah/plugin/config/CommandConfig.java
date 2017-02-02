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
