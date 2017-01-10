package mah.ui.pane.input;

import mah.ui.event.EventHandler;
import mah.ui.input.Input;
import mah.ui.input.InputTextChangedEvent;
import mah.ui.pane.Pane;

/**
 * Created by zgq on 2017-01-08 11:59
 */
public interface InputPane extends Input,Pane{


    void setOnInputTextChanged(EventHandler<? extends InputTextChangedEvent> inputTextChangedHandler);
}
