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


import lombok.val;
import mah.command.event.CommonFilterEvent;
import mah.command.event.NotFoundCommandEvent;
import mah.command.event.TriggerEvent;
import mah.event.ComparableEventHandler;
import mah.event.EventHandler;
import mah.ui.input.InputTextChangedEvent;
import mah.ui.input.TextState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zgq on 2/13/17.
 */
public class CommandExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommandExecutor.class);
    private final ExecutorService executorService = Executors.newCachedThreadPool();
    private final Map<String, Command> commands;
    private final List<ComparableEventHandler<NotFoundCommandEvent>> notFoundCommandHandlers = new ArrayList<>();
    private final List<CommandPostProcessor> commandPostProcessors = new ArrayList<>();

    private Command currentCommand;
    private String currentTriggerKey;
    private String currentQuery;

    CommandExecutor(Map<String, Command> commands) {
        this.commands = commands;
    }

    private static final Comparator COMPARATOR = (Comparator<ComparableEventHandler<NotFoundCommandEvent>>)
            (o1, o2) -> o1.compareTo(o2);


    void addCommandPostProcessor(CommandPostProcessor postProccessor) {
        commandPostProcessors.add(postProccessor);
    }

    void addNotFoundCommandHandler(ComparableEventHandler<NotFoundCommandEvent> commandEventEventHandler) {
        this.notFoundCommandHandlers.add(commandEventEventHandler);
        this.notFoundCommandHandlers.sort(COMPARATOR);
    }

    @Nullable
    Command getCurrentCommand() {
        return currentCommand;
    }

    @Nullable
    String getCurrentTriggerKey() {
        return currentTriggerKey;
    }

    String getCurrentQuery() {
        return currentQuery;
    }

    void shutdownNow() {
        executorService.shutdownNow();
    }

    void tryTriggerCommand(InputTextChangedEvent event) {
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
                    int index;
                    if (input.equals(key)) {
                        index = key.length();
                        currentTriggerKey = key;
                    } else {
                        index = key.length() + 1;
                        currentTriggerKey = key + ' ';
                    }
                    currentQuery = input.substring(index);
                }
            }

            if (!triggerSucc) {
                handleNotFoundCommand(input);
            }
        } catch (Exception e) {
            throw new CommandException(e);
        }
    }

    private void handleNotFoundCommand(String input) throws Exception {
        // There is no command found
        if (currentCommand != null) {
            currentCommand.idle();
        }
        NotFoundCommandEvent notFoundCommandEvent = new NotFoundCommandEvent(input, currentCommand);
        for (EventHandler<NotFoundCommandEvent> notFoundCommandHandler : notFoundCommandHandlers) {
            notFoundCommandHandler.handle(notFoundCommandEvent);
        }
        if (currentCommand != null) {
            currentCommand = null;
        }
        currentTriggerKey = null;
        currentQuery = null;
    }

    private ExecutionContext buildExecutionContext(Command command, String input) {
        return new ExecutionContext(command, input);
    }

    private void beforeExecute(ExecutionContext context) {
        for (CommandPostProcessor commandPostProcessor : commandPostProcessors) {
            commandPostProcessor.postProcessBeforeExecute(context);
        }
    }

    private void afterExecute(ExecutionContext context) {
        for (CommandPostProcessor commandPostProcessor : commandPostProcessors) {
            commandPostProcessor.postProcessAfterExecute(context);
        }
    }

    private void triggerCommand(Command command, String triggerKey, String input) throws Exception {
        ExecutionContext executionContext = buildExecutionContext(command, input);
        if (input.equals(triggerKey)) {
            beforeExecute(executionContext);
            executeCommand(command, triggerKey);
            afterExecute(executionContext);
            currentCommand = command;
            return;
        } else {
            if (input.charAt(triggerKey.length()) == ' ') {
                beforeExecute(executionContext);
                filterCommand(command, triggerKey, input);
                afterExecute(executionContext);
                currentCommand = command;
                return;
            }
        }
    }


    private void executeCommand(Command command, String triggerKey) {
        executorService.submit(new TriggerCommandTask(command, triggerKey));
    }


    private void filterCommand(Command command, String triggerKey, String input) throws Exception {
        String inputContent = input.substring(triggerKey.length() + 1, input.length());
        executorService.submit(new FilterCommandTask(command, triggerKey, inputContent));
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
            } catch (Throwable e) {
                LOGGER.error("Command " + command + " failed to be executed", e);
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
                List<EventHandler<? extends CommonFilterEvent>> commonFilterEventHandlers = command
                        .getCommonFilterEventHandlers();
                for (EventHandler commonFilterEventHandler : commonFilterEventHandlers) {
                    commonFilterEventHandler.handle(commonFilterEvent);
                }
            } catch (Throwable e) {
                LOGGER.error("Command " + this.command + " failed to be filtered", e);
            }
        }
    }
}
