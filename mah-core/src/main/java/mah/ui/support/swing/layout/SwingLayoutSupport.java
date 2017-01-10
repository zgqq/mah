package mah.ui.support.swing.layout;

import mah.ui.event.EventHandler;
import mah.ui.key.KeyEvent;
import mah.ui.layout.LayoutSupport;

import java.awt.event.KeyAdapter;

/**
 * Created by zgq on 2017-01-09 12:17
 */
public abstract class SwingLayoutSupport extends LayoutSupport implements SwingLayout {

    private static KeyEvent convertEvent(java.awt.event.KeyEvent event) {
        int keyCode = event.getKeyCode();
        KeyEvent keyEvent = new KeyEvent();
        keyEvent.setKeyCode(keyCode);
        return keyEvent;
    }

    protected class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
            KeyEvent keyEvent = convertEvent(e);
            for (EventHandler eventHandler : getKeyPressedHandlers()) {
                eventHandler.handle(keyEvent);
            }
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            KeyEvent keyEvent = convertEvent(e);
            for (EventHandler eventHandler : getKeyReleasedHandlers()) {
                eventHandler.handle(keyEvent);
            }
        }
    }

}
