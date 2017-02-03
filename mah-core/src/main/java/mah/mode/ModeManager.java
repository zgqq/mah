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
package mah.mode;

import mah.action.Action;
import mah.action.ActionException;
import mah.action.ActionHandler;
import mah.action.GlobalAction;
import mah.app.ApplicationEvent;
import mah.app.ApplicationListener;
import mah.app.config.Config;
import mah.app.config.XmlConfig;
import mah.keybind.Keybind;
import mah.keybind.KeybindManager;
import mah.keybind.config.KeybindConfig;
import mah.keybind.util.SimpleParser;
import mah.mode.config.ModeConfig;
import mah.mode.config.XmlModeParser;
import org.w3c.dom.Document;

import javax.swing.*;
import java.util.*;

/**
 * Created by zgq on 2017-01-08 20:34
 */
public class ModeManager implements ApplicationListener {
    private static final ModeManager INSTANCE = new ModeManager();
    private final Map<String, Mode> modes = new HashMap<>();

    private ModeManager() {}

    public static ModeManager getInstance() {
        return INSTANCE;
    }

    public void triggerMode(Mode mode) {
        triggerMode(mode.getName());
    }

    public void triggerMode(String mod) {
        Mode mode = getMode(mod);
        if (mode == null) {
            throw new ModeException("Not found mode " + mod);
        }
        KeybindManager.getInstance().setCurrentMode(mode);
    }

    public Mode getMode(String mod) {
        return modes.get(mod);
    }

    public void registerMode(Mode mode) {
        mode.init();
        modes.put(mode.getName(), mode);
    }

    public void registerMode(Mode mode, ActionHandler actionHandler) {
        mode.init();
        mode.updateActionHandler(actionHandler);
        modes.put(mode.getName(), mode);
    }

    public Mode registerOrUpdateMode(Mode mode, ActionHandler actionHandler) {
        Mode existedMode = modes.get(mode.getName());
        if (existedMode == null) {
            registerMode(mode, actionHandler);
            return null;
        } else {
            existedMode.updateActionHandler(actionHandler);
        }
        return existedMode;
    }

    public Action findGlobalAction(String actionName) {
        Set<Map.Entry<String, Mode>> entries = modes.entrySet();
        Action action = null;
        for (Map.Entry<String, Mode> entry : entries) {
            Mode mode = entry.getValue();
            action = mode.lookupAction(actionName);
            if (action != null && action instanceof GlobalAction) {
                break;
            }
        }
        if (action == null) {
            throw new ActionException("Not found global action " + actionName);
        }
        return action;
    }

    public Mode getOrRegisterMode(Mode mode) {
        Mode existedMode = getMode(mode.getName());
        if (existedMode == null) {
            registerMode(mode);
            return mode;
        }
        return existedMode;
    }

    @Override
    public void afterStart(ApplicationEvent event) {
        Config config = event.getConfig();
        if (config instanceof XmlConfig) {
            XmlConfig xmlConfig = (XmlConfig) config;
            registerModeKeybinds(xmlConfig.getDocument());
        }
    }

    private void registerModeKeybinds(Document document) {
        XmlModeParser xmlModeParser = new XmlModeParser(document);
        List<ModeConfig> modeConfigs = xmlModeParser.parseModeConfigs();
        modeConfigs.forEach(modeConfig -> {
            HashSet<KeybindConfig> keybinds = modeConfig.getKeybinds();
            for (KeybindConfig keybindConfig : keybinds) {
                Keybind keybind = new Keybind();
                Mode mode = modes.get(modeConfig.getName());
                if (mode == null) {
                    throw new ModeException("Not found mode " + modeConfig.getName());
                }
                Action action = mode.findAction(keybindConfig.getAction());
                keybind.setAction(action);
                List<KeyStroke> keyStrokes = SimpleParser.parse(keybindConfig.getBind());
                keybind.setKeyStrokes(keyStrokes);
                KeybindManager.getInstance().addKeybind(modeConfig.getName(), keybind);
            }
        });
    }
}
