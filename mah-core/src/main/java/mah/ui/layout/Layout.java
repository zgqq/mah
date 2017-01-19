package mah.ui.layout;

import mah.mode.Mode;
import mah.ui.event.EventHandler;
import mah.ui.key.KeyEvent;

/**
 * Created by zgq on 2017-01-08 11:54
 */
public interface Layout {

    void init();

    void setOnKeyPressed(EventHandler<? extends KeyEvent> keyPressedHandler);

    void setOnKeyReleased(EventHandler<? extends KeyEvent> keyReleasedHandler);

    default void onSetCurrentLayout() {}

    default String getName() {
        throw new UnsupportedOperationException("Unable to get layout name");
    }

    default void setDefaultMode() {
        throw new UnsupportedOperationException("Unable to set default mode");
    }

    default Mode getMode(){
        throw new UnsupportedOperationException("Unable to get mode");
    }

    default void registerMode(Mode mode,ModeListener modeListener){
        throw new UnsupportedOperationException("Unsupport register mode");
    }

}
