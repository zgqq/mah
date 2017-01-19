package mah.ui.layout;

import mah.mode.Mode;
import mah.ui.event.EventHandler;
import mah.ui.key.KeyEvent;
import mah.ui.support.swing.layout.LayoutUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-09 12:11
 */
public abstract class LayoutSupport implements Layout {

    private List<EventHandler<? extends KeyEvent>> keyReleasedHandlers = new ArrayList<>();
    private List<EventHandler<? extends KeyEvent>> keyPressedHandlers = new ArrayList<>();
    private final List<ModeListener> modeListeners = new ArrayList<>();
    private Mode mode;

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

    protected final void setMode(Mode mode) {
        this.mode = mode;
    }

    protected final void addModeListener(ModeListener modeListener) {
        modeListeners.add(modeListener);
    }

    protected final void triggerMode() {
        LayoutUtils.triggerMode(mode, modeListeners);
    }


}
