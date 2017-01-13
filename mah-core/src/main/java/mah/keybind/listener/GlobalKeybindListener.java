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
            logger.debug("press ctrl");
            return;
        } else if (keyCode == NativeKeyEvent.VC_ALT_L || keyCode == NativeKeyEvent.VC_ALT_R) {
            keyState.setAlt(true);
            logger.debug("press alt");
            return;
        } else if (keyCode == NativeKeyEvent.VC_META_L || keyCode == NativeKeyEvent.VC_META_R) {
            keyState.setMeta(true);
            logger.debug("press meta");
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
        logger.info("press " + keyStroke);
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
