package mah.ui.layout;

import mah.ui.event.EventHandler;
import mah.ui.key.KeyEvent;

/**
 * Created by zgq on 2017-01-09 13:13
 */
public abstract class LayoutWrapper implements Layout{
    private final Layout layout;

    protected LayoutWrapper(Layout layout) {
        this.layout = layout;
    }

    @Override
    public String getName() {
        return layout.getName();
    }

    @Override
    public void setDefaultMode() {
        layout.setDefaultMode();
    }

    @Override
    public void setOnKeyPressed(EventHandler<? extends KeyEvent> keyPressedHandler) {
        layout.setOnKeyPressed(keyPressedHandler);
    }

    @Override
    public void setOnKeyReleased(EventHandler<? extends KeyEvent> keyReleasedHandler) {
        layout.setOnKeyReleased(keyReleasedHandler);
    }

    @Override
    public void init() {
        this.layout.init();
    }
    @Override
    public void onSetCurrentLayout() {
        layout.onSetCurrentLayout();
    }

}
