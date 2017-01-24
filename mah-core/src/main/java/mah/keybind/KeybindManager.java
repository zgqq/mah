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
import mah.app.config.XMLConfig;
import mah.keybind.config.KeybindConfig;
import mah.keybind.config.XMLGlobalKeybindParser;
import mah.keybind.listener.KeyPressedHandler;
import mah.keybind.util.SimpleParser;
import mah.mode.Mode;
import mah.mode.ModeManager;
import mah.ui.layout.LayoutFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zgq on 2017-01-09 09:21
 */
public class KeybindManager implements ApplicationListener {

    private final Map<String, List<Keybind>> KEYBINDS = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(KeybindManager.class);
    private Mode currentMode;
    private int keyIndex;
    private static final KeybindManager INSTANCE = new KeybindManager();
    private List<Keybind> currentKeybind;

    public void addKeybind(String mod, Keybind keybind) {
        List<Keybind> keybindings = KEYBINDS.get(mod);
        if (keybindings == null) {
            keybindings = new ArrayList<>();
            KEYBINDS.put(mod, keybindings);
        }
        keybindings.add(keybind);
    }

    public static KeybindManager getInstance() {
        return INSTANCE;
    }

    public void addGlobalKeybind(Keybind keybind) {
        final Provider provider = Provider.getCurrentProvider(true);
        final HotKeyListener listener = hotKey -> {
            ActionManager.getInstance().handleAction(keybind.getAction());
        };
        KeyStroke keyStroke = keybind.getKeyStrokes().get(0);
        provider.register(keyStroke, listener);
    }

    private void registerGlobalKeybinds(Document document) {
        XMLGlobalKeybindParser globalKeybindParser = new XMLGlobalKeybindParser(document);
        List<KeybindConfig> globalKeybindConfigs = globalKeybindParser.parseGlobalKeybinds();
        globalKeybindConfigs.forEach(keybind -> {
            Keybind keybinding = new Keybind();
            Action action = ModeManager.getInstance().findGlobalAction(keybind.getAction());
            if (!(action instanceof GlobalAction)) {
                throw new ActionException("The action is not global mode " + keybind.getAction());
            }
            keybinding.setAction(action);
            List<KeyStroke> keyStrokes = SimpleParser.parse(keybind.getBind());
            keybinding.setKeyStrokes(keyStrokes);
            KeybindManager.getInstance().addGlobalKeybind(keybinding);
        });
    }

    @Override
    public void start(ApplicationEvent event) {
        LayoutFactoryBean.getInstance().setOnKeyPressed(new KeyPressedHandler());
    }

    @Override
    public void afterStart(ApplicationEvent event) {
        Config config = event.getConfig();
        if (config instanceof XMLConfig) {
            XMLConfig xmlConfig = (XMLConfig) config;
            registerGlobalKeybinds(xmlConfig.getDocument());
        }
    }

    private Action findAction(KeyStroke pressedKeyStroke) {
        String mod = currentMode.getName();
        List<Keybind> keybinds = KEYBINDS.get(mod);
        Mode curMod = currentMode;
        while (keybinds == null) {
            Mode parent = curMod.getParent();
            if (parent == null) {
                break;
            }
            curMod = parent;
            keybinds = KEYBINDS.get(parent.getName());
        }
        if (keybinds == null) {
            return null;
        }
        return findAction(keybinds, pressedKeyStroke);
    }

    private Action matchAction(List<Keybind> keybinds, KeyStroke pressedKeyStroke) {
        currentKeybind = new ArrayList<>();
        for (Keybind keybind : keybinds) {
            List<KeyStroke> keyStrokes = keybind.getKeyStrokes();
            KeyStroke keyStroke = keyStrokes.get(0);
            if (keyStroke.equals(pressedKeyStroke)) {
                if (keyStrokes.size() > 1) {
                    currentKeybind.add(keybind);
                } else {
                    currentKeybind = null;
                    return keybind.getAction();
                }
            }
        }
        return null;
    }

    private Action matchActionByPendingKeybinds(KeyStroke pressedKeyStroke) {
        List<Keybind> prevKeybinds = currentKeybind;
        currentKeybind = new ArrayList<>();
        for (Keybind keybinding : prevKeybinds) {
            List<KeyStroke> keyStrokes = keybinding.getKeyStrokes();
            KeyStroke keyStroke = keyStrokes.get(keyIndex);
            if (keyStroke.equals(pressedKeyStroke)) {
                if (keyStrokes.size() - 1 == keyIndex) {
                    restoreKeybind();
                    return keybinding.getAction();
                }
                currentKeybind.add(keybinding);
            }
        }
        if (currentKeybind.size() > 0) {
            keyIndex++;
            return null;
        }
        restoreKeybind();
        return null;
    }

    private Action findAction(List<Keybind> keybinds, KeyStroke pressedKeyStroke) {
        if (currentKeybind == null) {
            Action action = matchAction(keybinds, pressedKeyStroke);
            if (action != null) {
                return action;
            }
            if (currentKeybind.size() > 0) {
                keyIndex = 1;
            } else {
                currentKeybind = null;
            }
        } else {
            return matchActionByPendingKeybinds(pressedKeyStroke);
        }
        return null;
    }

    private void restoreKeybind() {
        currentKeybind = null;
        keyIndex = 0;
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
}
