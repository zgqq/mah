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
package mah.keybind;

import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;
import mah.action.Action;
import mah.action.ActionException;
import mah.action.ActionManager;
import mah.action.GlobalAction;
import mah.app.ApplicationEvent;
import mah.app.ApplicationListener;
import mah.app.config.Config;
import mah.app.config.XmlConfig;
import mah.keybind.config.GlobalKeybindConfig;
import mah.keybind.config.XmlGlobalKeybindParser;
import mah.keybind.listener.GlobalKeyEvent;
import mah.keybind.listener.GlobalKeyListener;
import mah.keybind.listener.GlobalKeybindListener;
import mah.keybind.listener.KeyPressedHandler;
import mah.keybind.util.SimpleParser;
import mah.mode.Mode;
import mah.mode.ModeManager;
import mah.ui.layout.LayoutFactoryBean;
import mah.ui.window.Window;
import mah.ui.window.WindowManager;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

/**
 * Created by zgq on 2017-01-09 09:21
 */
public class KeybindManager implements ApplicationListener, GlobalKeyListener {
    private final Map<String, List<Keybind>> keybinds = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(KeybindManager.class);
    private final List<Keybind> globalKeybinds = new ArrayList<>();
    private Mode currentMode;
    private static final KeybindManager INSTANCE = new KeybindManager();
    private final KeybindMatcher keybindMatcher = new KeybindMatcher();
    private final GlobalKeybindListener globalListener = new GlobalKeybindListener();

    public void addKeybind(String mod, Keybind keybind) {
        List<Keybind> keybindings = keybinds.get(mod);
        if (keybindings == null) {
            keybindings = new ArrayList<>();
            keybinds.put(mod, keybindings);
        }
        keybindings.add(keybind);
    }

    public static KeybindManager getInstance() {
        return INSTANCE;
    }

    public void addGlobalKeybind(Keybind keybind) {
        globalKeybinds.add(keybind);
    }

    private void consumeKeystroke(KeyStroke keyStroke) {
        final Provider provider = Provider.getCurrentProvider(false);
        final HotKeyListener listener = hotKey -> {
            //do nothing
        };
        provider.register(keyStroke, listener);
    }

    private void registerGlobalKeybinds(Document document) {
        XmlGlobalKeybindParser globalKeybindParser = new XmlGlobalKeybindParser(document);
        List<GlobalKeybindConfig> globalKeybindConfigs = globalKeybindParser.parseGlobalKeybinds();
        globalKeybindConfigs.forEach(keybind -> {
            Keybind keybinding = new Keybind();
            Action action = ModeManager.getInstance().findGlobalAction(keybind.getAction());
            if (!(action instanceof GlobalAction)) {
                throw new ActionException("The action is not global mode " + keybind.getAction());
            }
            keybinding.setAction(action);
            List<KeyStroke> keyStrokes = SimpleParser.parse(keybind.getListen());
            List<KeyStroke> consumeKeyStrokes = SimpleParser.parse(keybind.getConsume());
            keybinding.setKeyStrokes(keyStrokes);
            KeybindManager.getInstance().addGlobalKeybind(keybinding);
            consumeKeystroke(consumeKeyStrokes.get(0));
        });
    }

    @Override
    public void start(ApplicationEvent event) {
        LayoutFactoryBean.getInstance().setOnKeyPressed(new KeyPressedHandler());
        globalListener.addListener(this);
        globalListener.start();
    }

    @Override
    public void afterStart(ApplicationEvent event) {
        Config config = event.getConfig();
        if (config instanceof XmlConfig) {
            XmlConfig xmlConfig = (XmlConfig) config;
            registerGlobalKeybinds(xmlConfig.getDocument());
        }
    }

    @Nullable
    private Action findAction(Mode mode, KeyStroke pressedKeyStroke) {
        List<Keybind> keybinds = this.keybinds.get(mode.getName());
        if (keybinds != null) {
            Action action = keybindMatcher.addPendingKeybindIfNotFoundAction(keybinds, pressedKeyStroke);
            if (action != null) {
                return action;
            }
        }

        final List<Mode> children = mode.getChildren();
        for (final Mode child : children) {
            Action action = findAction(child, pressedKeyStroke);
            if (action != null) {
                return action;
            }
        }
        return null;
    }

    @Nullable
    private Action findAction(KeyStroke pressedKeyStroke) {
        Action action;
        keybindMatcher.start();
        if (keybindMatcher.isEmpty()) {
            action = findAction(currentMode, pressedKeyStroke);
        } else {
            action = keybindMatcher.continueMatch(pressedKeyStroke);
        }
        keybindMatcher.end();
        return action;
    }

    @Nullable
    private Action findAction(List<Keybind> keybinds, KeyStroke pressedKeyStroke) {
        return keybindMatcher.addPendingKeybindIfNotFoundAction(keybinds, pressedKeyStroke);
    }

    public void setCurrentMode(Mode currentMode) {
        this.currentMode = currentMode;
    }

    public void tryExecuteAction(KeyStroke keyStroke) {
        Action action = findAction(keyStroke);
        if (action != null) {
            ActionManager.getInstance().handleAction(action);
        }
    }

    private Action findGlobalAction(KeyStroke pressedKeyStroke) {
        return findAction(globalKeybinds, pressedKeyStroke);
    }

    public void tryExecuteGlobalAction(KeyStroke keyStroke) {
        Action action = findGlobalAction(keyStroke);
        if (action != null) {
            ActionManager.getInstance().handleAction(action);
        }
    }

    public void addGlobalKeyListener(GlobalKeyListener globalKeyListener) {
        globalListener.addListener(globalKeyListener);
    }

    @Override
    public void keyPressed(GlobalKeyEvent keyEvent) {
        Window window = WindowManager.getInstance().getCurrentWindow();
        if (window != null && window.isShowing() && window.isFocused()) {
            return;
        }
        KeybindManager.getInstance().tryExecuteGlobalAction(keyEvent.getKeyStroke());
    }
}
