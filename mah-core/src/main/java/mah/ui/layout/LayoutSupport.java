package mah.ui.layout;

import mah.ui.event.EventHandler;
import mah.ui.key.KeyEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-09 12:11
 */
public abstract class LayoutSupport implements Layout {

    private List<EventHandler<? extends KeyEvent>> keyReleasedHandlers = new ArrayList<>();
    private List<EventHandler<? extends KeyEvent>> keyPressedHandlers = new ArrayList<>();

    @Override
    public void setOnKeyReleased(EventHandler<? extends KeyEvent> eventHandler) {
        keyReleasedHandlers.add(eventHandler);
    }

    @Override
    public void setOnKeyPressed(EventHandler<? extends KeyEvent> eventHandler) {
        keyPressedHandlers.add(eventHandler);
    }

    public List<EventHandler<? extends KeyEvent>> getKeyPressedHandlers() {
        return keyPressedHandlers;
    }

    public List<EventHandler<? extends KeyEvent>> getKeyReleasedHandlers() {
        return keyReleasedHandlers;
    }



}
