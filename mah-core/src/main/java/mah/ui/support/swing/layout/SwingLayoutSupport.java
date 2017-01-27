package mah.ui.support.swing.layout;

import mah.ui.UIException;
import mah.ui.event.EventHandler;
import mah.ui.key.KeyEvent;
import mah.ui.layout.LayoutSupport;
import mah.ui.support.swing.theme.SwingLayoutTheme;
import mah.ui.theme.LayoutTheme;

import java.awt.*;
import java.awt.event.KeyAdapter;

/**
 * Created by zgq on 2017-01-09 12:17
 */
public abstract class SwingLayoutSupport extends LayoutSupport implements SwingLayout {

    private SwingLayoutTheme currentTheme;

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
                try {
                    eventHandler.handle(keyEvent);
                } catch (Exception e1) {
                    throw new UIException(e1);
                }
            }
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            KeyEvent keyEvent = convertEvent(e);
            for (EventHandler eventHandler : getKeyReleasedHandlers()) {
                try {
                    eventHandler.handle(keyEvent);
                } catch (Exception e1) {
                    throw new UIException(e1);
                }
            }
        }
    }

    @Override
    public final void apply(LayoutTheme theme) {
        this.currentTheme = ((SwingLayoutTheme) theme);
        applyTheme(this.currentTheme);
    }

    protected abstract void applyTheme(SwingLayoutTheme layoutTheme);

    public SwingLayoutTheme getCurrentTheme() {
        return currentTheme;
    }

    protected Color getColorByProperty(String property) {
        return currentTheme.getColorByProperty(property);
    }
}
