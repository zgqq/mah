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
import mah.ui.key.KeystateManager;
import mah.ui.window.Window;
import mah.ui.window.WindowManager;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zgq on 2017-01-09 09:36
 */
public class GlobalKeybindListener extends SwingKeyAdapter {

    private org.slf4j.Logger logger = LoggerFactory.getLogger(GlobalKeybindListener.class);

    public void start() {
        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());

            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(this);

        // Get the logger for "org.jnativehook" and set the level to off.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        // Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        Window window = WindowManager.getInstance().getCurrentWindow();
        if (window!=null && window.isShowing() && window.isFocused()) {
            return;
        }
        KeystateManager keyState = KeystateManager.getInstance();
        int keyCode = e.getKeyCode();
        if (keyCode == NativeKeyEvent.VC_CONTROL_L || e.getKeyCode() == NativeKeyEvent.VC_CONTROL_R) {
            keyState.setCtrl(true);
            logger.trace("press ctrl");
            return;
        } else if (keyCode == NativeKeyEvent.VC_ALT_L || keyCode == NativeKeyEvent.VC_ALT_R) {
            keyState.setAlt(true);
            logger.trace("press alt");
            return;
        } else if (keyCode == NativeKeyEvent.VC_META_L || keyCode == NativeKeyEvent.VC_META_R) {
            keyState.setMeta(true);
            logger.trace("press meta");
            return;
        } else {
        }

        int javaKeyCode = getJavaKeyCode(e);
        int mask = 0;
        if (keyState.ctrlPressed()) {
            mask |= KeyEvent.CTRL_MASK;
        }

        if (keyState.altPressed()) {
            mask |= KeyEvent.ALT_MASK;
        }

        if (keyState.metaPressed()) {
            mask |= KeyEvent.META_MASK;
        }
        KeyStroke keyStroke = KeyStroke.getKeyStroke(javaKeyCode, mask);
        logger.trace("press " + keyStroke);
        KeybindManager.getInstance().tryExecuteGlobalAction(keyStroke);
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        KeystateManager keyState = KeystateManager.getInstance();
        int keyCode = e.getKeyCode();
        if (keyCode == NativeKeyEvent.VC_CONTROL_L || e.getKeyCode() == NativeKeyEvent.VC_CONTROL_R) {
            keyState.setCtrl(false);
            return;
        }

        if (keyCode == NativeKeyEvent.VC_ALT_L || keyCode == NativeKeyEvent.VC_ALT_R) {
            keyState.setAlt(false);
            return;
        }

        if (keyCode == NativeKeyEvent.VC_META_L || keyCode == NativeKeyEvent.VC_META_R) {
            keyState.setMeta(false);
            return;
        }
    }

    public void nativeKeyTyped(NativeKeyEvent e) {
    }


}
