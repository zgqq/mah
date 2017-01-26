package mah.command;


import mah.app.ApplicationEvent;
import mah.app.ApplicationListener;
import mah.command.event.CommonFilterEvent;
import mah.command.event.EventHandler;
import mah.command.event.TriggerEvent;
import mah.command.listener.InputTextListener;
import mah.plugin.PluginException;
import mah.ui.input.InputPaneFactoryBean;
import mah.ui.input.InputTextChangedEvent;
import mah.ui.input.TextState;
import mah.ui.layout.Layout;
import mah.ui.window.Window;
import mah.ui.window.WindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zgq on 2017-01-08 20:32
 */
public class CommandManager implements ApplicationListener {

    private static final CommandManager INSTANCE = new CommandManager();
    private final static Logger logger = LoggerFactory.getLogger(CommandManager.class);
    private final Map<String, Map<String, Command>> PLUGIN_COMMAND = new HashMap<>();
    private final Map<String, Command> commands = new HashMap<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private volatile Command lockedCommand;
    private Command currentCommand;
    private String currentTriggerKey;
    private String currentQuery;

    private CommandManager() {}

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

    public Command getLockedCommand() {
        return lockedCommand;
    }

    public void setLockedCommand(Command command) {
        lockedCommand = command;
    }


    public static CommandManager getInstance() {
        return INSTANCE;
    }

    public void registerCommand(String plugin, Command command) {
        Map<String, Command> commands = PLUGIN_COMMAND.get(plugin);
        if (commands == null) {
            commands = new HashMap<>();
            PLUGIN_COMMAND.put(plugin, commands);
        }
        commands.put(command.getName(), command);
    }

    public Command findCommand(String plugin, String command) {
        Map<String, Command> commands = PLUGIN_COMMAND.get(plugin);
        if (commands == null) {
            return null;
        }
        return commands.get(command);
    }

    @Override
    public void start(ApplicationEvent event) {
        InputPaneFactoryBean.getInstance().setOnInputTextChanged(new InputTextListener());
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


    private void executeCommand(Command command, String triggerKey) {
        executorService.submit(new TriggerCommandTask(command, triggerKey));
    }


    private void triggerCommand(Command command, String triggerKey, String input) throws Exception {
        if (input.equals(triggerKey)) {
            executeCommand(command, triggerKey);
            currentCommand = command;
            return;
        } else {
            if (input.charAt(triggerKey.length()) == ' ') {
                filterCommand(command, triggerKey, input);
                currentCommand = command;
                return;
            }
        }
    }

    private void filterCommand(Command command, String triggerKey, String input) throws Exception {
        String inputContent = input.substring(triggerKey.length() + 1, input.length());
        executorService.submit(new FilterCommandTask(command, triggerKey, inputContent));
    }

    public void tryTriggerCommand(InputTextChangedEvent event) {
        synchronized (this) {
            TextState newState = event.getNewState();
            String input = newState.getText();
            try {
                boolean triggerSucc = false;
                for (Map.Entry<String, Command> entry : commands.entrySet()) {
                    String key = entry.getKey();
                    Command command = entry.getValue();
                    if (input.startsWith(key)) {
                        triggerCommand(command, key, input);
                        triggerSucc = true;
                        currentTriggerKey = key.trim();
                        int index;
                        if (input.equals(key)){
                            index = key.length();
                        }else {
                            index = key.length() + 1;
                        }
                        currentQuery = input.substring(index);
                    }
                }

                // There is no plugin found
                if (!triggerSucc) {
                    if (currentCommand != null) {
                        currentCommand.idle();
                    }
                    Window currentWindow = WindowManager.getInstance().getCurrentWindow();
                    Layout currentLayout = currentWindow.getCurrentLayout();
                    Layout defaultLayout = currentWindow.getDefaultLayout();
                    if (currentLayout == defaultLayout) {
                        return;
                    }
                    if (currentCommand != null) {
                        currentCommand = null;
                        setDefaultLayout();
                    }
                }
            } catch (Exception e) {
                throw new CommandException(e);
            }
        }
    }

    public synchronized String getCurrentTriggerKey() {
        return currentTriggerKey;
    }

    private void setDefaultLayout() {
        WindowManager.getInstance().getCurrentWindow().useDefaultLayoutAsCurrentLayout();
    }

    @Override
    public void shutdown() throws Exception {
        executorService.shutdownNow();
    }

    public Command getCurrentCommand() {
        synchronized (this) {
            return currentCommand;
        }
    }

    public synchronized String getCurrentQuery() {
        return currentQuery;
    }

    static class TriggerCommandTask implements Runnable {

        private final Command command;
        private final String triggerKey;

        TriggerCommandTask(Command command, String triggerKey) {
            this.command = command;
            this.triggerKey = triggerKey;
        }

        @Override
        public void run() {
            try {
                List<EventHandler<? extends TriggerEvent>> triggerEventHandlers = command.getTriggerEventHandlers();
                TriggerEvent triggerEvent = new TriggerEvent(triggerKey);
                for (EventHandler triggerEventHandler : triggerEventHandlers) {
                    triggerEventHandler.handle(triggerEvent);
                }
            } catch (Exception e) {
                logger.error("Command " + command + " failed to be executed", e);
            }
        }
    }

    static class FilterCommandTask implements Runnable {

        private final Command command;
        private final String triggerKey;
        private final String content;

        FilterCommandTask(Command command, String triggerKey, String content) {
            this.command = command;
            this.triggerKey = triggerKey;
            this.content = content;
        }

        @Override
        public void run() {
            try {
                CommonFilterEvent commonFilterEvent = new CommonFilterEvent(triggerKey, content);
                List<EventHandler<? extends CommonFilterEvent>> commonFilterEventHandlers = command.getCommonFilterEventHandlers();
                for (EventHandler commonFilterEventHandler : commonFilterEventHandlers) {
                    commonFilterEventHandler.handle(commonFilterEvent);
                }
            } catch (Exception e) {
                logger.error("Command " + this.command + " failed to be filtered", e);
            }
        }
    }
}
