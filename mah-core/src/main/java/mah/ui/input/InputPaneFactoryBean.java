package mah.ui.input;

import mah.action.ActionHandler;
import mah.mode.ModeManager;
import mah.ui.event.EventHandler;
import mah.ui.pane.input.InputPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-09 14:36
 */
public class InputPaneFactoryBean {

    private static final InputPaneFactoryBean INSTANCE = new InputPaneFactoryBean();
    private List<EventHandler<? extends InputTextChangedEvent>> inputTextChangedHandlers = new ArrayList<>();

    public static InputPaneFactoryBean getInstance() {
        return INSTANCE;
    }

    public void setOnInputTextChanged(EventHandler<? extends InputTextChangedEvent> handler) {
        inputTextChangedHandlers.add(handler);
    }

    private void updateActionHandler(InputPane inputPane) {
        if (inputPane instanceof ActionHandler) {
            ActionHandler actionHandler = (ActionHandler) inputPane;
            ModeManager.getInstance().registerOrUpdateMode(InputMode.getAndRegisterMode(), actionHandler);
        }
    }

    public void initBean(InputPane inputPane) {
        for (EventHandler<? extends InputTextChangedEvent> inputTextChangedHandler : inputTextChangedHandlers) {
            inputPane.setOnInputTextChanged(inputTextChangedHandler);
        }
        updateActionHandler(inputPane);
    }

}
