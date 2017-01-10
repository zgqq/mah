package mah.command.listener;

import mah.command.CommandManager;
import mah.ui.event.EventHandler;
import mah.ui.input.InputTextChangedEvent;

/**
 * Created by zgq on 2017-01-09 14:39
 */
public class InputTextListener implements EventHandler<InputTextChangedEvent> {

    @Override
    public void handle(InputTextChangedEvent event) {
        CommandManager.getInstance().tryTriggerCommand(event);
    }

}

