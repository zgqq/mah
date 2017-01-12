package mah.ui.layout;

import mah.ui.event.EventHandler;
import mah.ui.key.KeyEvent;

/**
 * Created by zgq on 2017-01-08 11:54
 */
public interface Layout {

    void init();

    default String getName(){
        throw new UnsupportedOperationException("Unable to get layout name");
    }

    void setDefaultMode();

    void setOnKeyPressed(EventHandler<? extends KeyEvent> keyPressedHandler);

    void setOnKeyReleased(EventHandler<? extends KeyEvent> keyReleasedHandler);

    void onSetCurrentLayout();


}
