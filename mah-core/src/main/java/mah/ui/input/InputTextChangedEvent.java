package mah.ui.input;

/**
 * Created by zgq on 2017-01-09 09:09
 */
public class InputTextChangedEvent {

    private final TextState newState;
    private final TextState oldState;

    public InputTextChangedEvent(TextState newState, TextState oldState) {
        this.newState = newState;
        this.oldState = oldState;
    }

    public TextState getNewState() {
        return newState;
    }

    public TextState getOldState() {
        return oldState;
    }
}
