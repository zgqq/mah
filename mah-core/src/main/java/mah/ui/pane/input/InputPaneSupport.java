package mah.ui.pane.input;

import mah.action.ActionHandler;
import mah.ui.event.EventHandler;
import mah.ui.input.AbstractInput;
import mah.ui.input.InputTextChangedEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-09 15:30
 */
public abstract class InputPaneSupport extends AbstractInput implements InputPane,ActionHandler {

    private final List<EventHandler<? extends InputTextChangedEvent>> inputTextChangedHandlers = new ArrayList<>();

    @Override
    public void setOnInputTextChanged(EventHandler<? extends InputTextChangedEvent> inputTextChangedHandler) {
        inputTextChangedHandlers.add(inputTextChangedHandler);
    }

    public List<EventHandler<? extends InputTextChangedEvent>> getInputTextChangedHandlers() {
        return inputTextChangedHandlers;
    }
}
