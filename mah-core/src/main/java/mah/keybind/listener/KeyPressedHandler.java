/**
 * MIT License
 *
 * Copyright (c) 2017 zgqq
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
