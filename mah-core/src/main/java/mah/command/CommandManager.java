package mah.command;


import mah.app.ApplicationEvent;
import mah.app.ApplicationListener;
import mah.command.event.CommonFilterEvent;
import mah.command.event.EventHandler;
import mah.command.event.TriggerEvent;
import mah.command.listener.InputTextListener;
import mah.plugin.PluginException;
import mah.plugin.command.PluginCommand;
import mah.ui.input.InputPaneFactoryBean;
import mah.ui.input.InputTextChangedEvent;
import mah.ui.input.TextState;
import mah.ui.layout.Layout;
import mah.ui.window.Window;
import mah.ui.window.WindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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
    private Command currentCommand;

    private CommandManager() {

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

    private Layout getLayout(PluginCommand command) {
        for (Field field : command.getClass().getDeclaredFields()) {
            Annotation annotation = field.getAnnotation(mah.ui.annnotation.Layout.class);
            if (annotation != null) {
                try {
                    field.setAccessible(true);
                    Layout o = (Layout) field.get(command);
                    return o;
                } catch (IllegalAccessException e) {
                    throw new CommandException(e);
                }
            }
        }
        return null;
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

//    private void sychInput(TextState textState, Layout newLayout) {
//        if (newLayout instanceof InputPaneProvider) {
//            InputPaneProvider inputPaneProvider = (InputPaneProvider) newLayout;
//            InputPane inputPane = inputPaneProvider.getInputPane();
//            UIManager.getInstance().runLater(() -> {
//                inputPane.setTextStateQuietly(textState);
//            });
//        }
//        newLayout.setDefaultMode();
//    }

//    private void sychInput(Command command, TextState textState) {
//        if (command instanceof PluginCommand) {
//            Layout layout = getLayout((PluginCommand) command);
//            if (layout != null) {
//                sychInput(textState, layout);
//            } else {
//                Window currentWindow = WindowManager.getInstance().getCurrentWindow();
//                currentWindow.getCurrentLayout().setDefaultMode();
//            }
//        }
//    }

    private void executeCommand(Command command, String triggerKey) {
//        sychInput(command, textState);
//        CommandEvent commandEvent = new CommandEvent(triggerKey);
        executorService.submit(new TriggerCommandTask(command, triggerKey));
    }


    private void triggerCommand(Command command, String triggerKey, String input) throws Exception {
        if (input.equals(triggerKey)) {
            executeCommand(command, triggerKey);
            currentCommand = command;
            return;
        } else {
            if (input.charAt(triggerKey.length()) == ' ') {
//                if (input.length() == triggerKey.length() + 1) {
//                    executeCommand(command, triggerKey);
//                    currentCommand = command;
//                    return;
//                }
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
                        if (input.equals(key) || input.charAt(key.length()) == ' ') {
                            triggerCommand(command, key, input);
                            triggerSucc = true;
                        }
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

    private void setDefaultLayout() {
        WindowManager.getInstance().getCurrentWindow().useDefaultLayoutAsCurrentLayout();
    }

    public Command getCurrentCommand() {
        synchronized (this) {
            return currentCommand;
        }
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
