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
package mah.command;

import mah.app.ApplicationEvent;
import mah.app.ApplicationListener;
import mah.command.listener.InputTextListener;
import mah.event.ComparableEventHandler;
import mah.plugin.PluginException;
import mah.ui.input.InputPaneFactoryBean;
import mah.ui.input.InputTextChangedEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zgq on 2017-01-08 20:32
 */
public class CommandManager implements ApplicationListener {
    private static final CommandManager INSTANCE = new CommandManager();
    private final Map<String, Command> commands = new HashMap<>();
    private final Map<String, Map<String, Command>> pluginCommands = new HashMap<>();
    private final CommandExecutor commandExecutor = new CommandExecutor(commands);
    private volatile Command lockedCommand;
    private static final int DEFAULT_HANDLER_PRIORITY = -1;

    private CommandManager() {}

    public static CommandManager getInstance() {
        return INSTANCE;
    }

    public List<String> findCommandMaps(Command command) {
        List<String> maps = new ArrayList<>();
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            String key = entry.getKey();
            Command value = entry.getValue();
            if (value == command) {
                maps.add(key);
            }
        }
        return maps;
    }

    public void addCommandPostProcessor(CommandPostProcessor postProcessor) {
        synchronized (this) {
            commandExecutor.addCommandPostProcessor(postProcessor);
        }
    }

    public Command getLockedCommand() {
        return lockedCommand;
    }

    public void setLockedCommand(Command command) {
        lockedCommand = command;
    }

    public void tryTriggerCommand(InputTextChangedEvent event) {
        synchronized (this) {
            commandExecutor.tryTriggerCommand(event);
        }
    }

    public synchronized void registerCommand(String plugin, Command command) {
        Map<String, Command> commands = pluginCommands.get(plugin);
        if (commands == null) {
            commands = new HashMap<>();
            pluginCommands.put(plugin, commands);
        }
        commands.put(command.getName(), command);
    }

    public synchronized Command findCommand(String plugin, String command) {
        Map<String, Command> commands = pluginCommands.get(plugin);
        if (commands == null) {
            return null;
        }
        return commands.get(command);
    }

    @Override
    public void start(ApplicationEvent event) {
        InputPaneFactoryBean.getInstance().setOnInputTextChanged(new InputTextListener());
        addNotFoundCommandHandler(new DefaultCommandHandler(DEFAULT_HANDLER_PRIORITY));
    }

    public void addNotFoundCommandHandler(ComparableEventHandler comparableEventHandler) {
        synchronized (this) {
            commandExecutor.addNotFoundCommandHandler(comparableEventHandler);
        }
    }

    public void mapCommand(String key, Command command) {
        synchronized (this) {
            Command prevCommand = commands.get(key);
            if (prevCommand != null) {
                throw new PluginException("key " + key + " already were registered");
            }
            commands.put(key, command);
        }
    }

    @Nullable
    public synchronized String getCurrentTriggerKey() {
        return commandExecutor.getCurrentTriggerKey();
    }

    @Nullable
    public Command getCurrentCommand() {
        synchronized (this) {
            return commandExecutor.getCurrentCommand();
        }
    }

    @Nullable
    public synchronized String getCurrentQuery() {
        return commandExecutor.getCurrentQuery();
    }

    @Override
    public void shutdown() throws Exception {
        commandExecutor.shutdownNow();
    }
}
