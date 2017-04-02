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

import mah.keybind.config.ModifierConfig;
import mah.ui.key.KeystateManager;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by zgq on 2017-01-09 09:36
 */
public class GlobalKeybindListener extends SwingKeyAdapter {
    private static final Map<ModifierConfig.Modifier, Integer> MODIFIER_CODE = new HashMap<>();
    static {
        MODIFIER_CODE.put(ModifierConfig.Modifier.CAPLOCK, NativeKeyEvent.VC_CAPS_LOCK);
        MODIFIER_CODE.put(ModifierConfig.Modifier.L_CONTROL, NativeKeyEvent.VC_CONTROL_L);
        MODIFIER_CODE.put(ModifierConfig.Modifier.R_CONTROL, NativeKeyEvent.VC_CONTROL_R);
        MODIFIER_CODE.put(ModifierConfig.Modifier.L_ALT, NativeKeyEvent.VC_ALT_L);
        MODIFIER_CODE.put(ModifierConfig.Modifier.R_ALT, NativeKeyEvent.VC_ALT_R);
        MODIFIER_CODE.put(ModifierConfig.Modifier.L_META, NativeKeyEvent.VC_META_L);
        MODIFIER_CODE.put(ModifierConfig.Modifier.R_META, NativeKeyEvent.VC_META_R);
    }

    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(GlobalKeybindListener.class);
    private final List<GlobalKeyListener> keyListeners = new CopyOnWriteArrayList<>();
    private final KeystateManager keyState = new KeystateManager();
    private final Map<Integer, Integer> modifierMap = new HashMap<>();


    private static Integer getKeycode(ModifierConfig.Modifier modifier) {
        return MODIFIER_CODE.get(modifier);
    }

    public void start(final List<ModifierConfig> modifierConfigs) {
        for (ModifierConfig modifierConfig : modifierConfigs) {
            modifierMap.put(getKeycode(modifierConfig.getOrigin()), getKeycode(modifierConfig.getAs()));
        }

        try {
            GlobalScreen.registerNativeHook();
        } catch (NativeHookException ex) {
            System.err.println("There was a problem registering the native hook.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        GlobalScreen.addNativeKeyListener(this);
        // Get the LOGGER for "org.jnativehook" and set the level to off.
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        // Don't forget to disable the parent handlers.
        logger.setUseParentHandlers(false);
    }

    public void nativeKeyPressed(NativeKeyEvent e) {
        int keyCode = getRealKeycode(e);
        LOGGER.debug("Actual key code {},convert code {}", e.getKeyCode(), keyCode);
        if (keyCode == NativeKeyEvent.VC_CONTROL_L || keyCode == NativeKeyEvent.VC_CONTROL_R) {
            keyState.setCtrl(true);
            LOGGER.debug("press ctrl");
            return;
        } else if (keyCode == NativeKeyEvent.VC_ALT_L || keyCode == NativeKeyEvent.VC_ALT_R) {
            keyState.setAlt(true);
            LOGGER.debug("press alt");
            return;
        } else if (keyCode == NativeKeyEvent.VC_META_L || keyCode == NativeKeyEvent.VC_META_R) {
            keyState.setMeta(true);
            LOGGER.debug("press meta");
            return;
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
        for (GlobalKeyListener keyListener : keyListeners) {
            GlobalKeyEvent globalKeyEvent = new GlobalKeyEvent(keyStroke);
            keyListener.keyPressed(globalKeyEvent);
        }
        LOGGER.debug("press " + keyStroke);
    }

    private int getRealKeycode(NativeKeyEvent e) {
        int keyCode = e.getKeyCode();
        Integer realKeycode = modifierMap.get(keyCode);
        keyCode = realKeycode == null ? keyCode : realKeycode;
        return keyCode;
    }

    public void nativeKeyReleased(NativeKeyEvent e) {
        int keyCode = getRealKeycode(e);
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

    public void nativeKeyTyped(NativeKeyEvent e) {}

    public void addListener(GlobalKeyListener globalKeyListener) {
        keyListeners.add(globalKeyListener);
    }
}
