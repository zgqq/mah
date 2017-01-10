package mah.keybind.listener;

import mah.keybind.KeybindManager;
import mah.ui.event.EventHandler;
import mah.ui.key.KeyEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.InputEvent;

/**
 * Created by zgq on 2017-01-09 09:42
 */
public class KeyPressedHandler implements EventHandler<KeyEvent>{

    private static final Logger logger = LoggerFactory.getLogger(KeyPressedHandler.class);

    @Override
    public void handle(KeyEvent event) {
        int keyCode = event.getKeyCode();
        int mask = 0;
        if (event.ctrlPressed()) {
            mask |= InputEvent.CTRL_MASK;
        }

        if (event.altPressed()) {
            mask |= InputEvent.ALT_MASK;
        }

        if (event.shiftPressed()) {
            mask |= InputEvent.SHIFT_MASK;
        }

        if (event.metaPressed()) {
            mask |= InputEvent.META_MASK;
        }

        if (!event.isModifierKey()) {
           KeyStroke keyStroke = KeyStroke.getKeyStroke(keyCode, mask);
            logger.debug("try key stroke {}", keyStroke);
            KeybindManager.getInstance().tryExecuteAction(keyStroke);
            return;
        }
    }


}
