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
import mah.keybind.config.GlobalKeybindConfig;
import mah.keybind.config.XMLGlobalKeybindParser;
import mah.keybind.listener.GlobalKeybindListener;
import mah.keybind.listener.KeyPressedHandler;
import mah.keybind.util.SimpleParser;
import mah.mode.Mode;
import mah.mode.ModeManager;
import mah.ui.layout.LayoutFactoryBean;
import org.jetbrains.annotations.Nullable;
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
    private final List<Keybind> GLOBAL_KEYBINDS = new ArrayList<>();
    private Mode currentMode;
    private static final KeybindManager INSTANCE = new KeybindManager();
    private final KeybindMatcher keybindMatcher = new KeybindMatcher();

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
        GLOBAL_KEYBINDS.add(keybind);
    }

    private void consumeKeystroke(KeyStroke keyStroke) {
        final Provider provider = Provider.getCurrentProvider(false);
        final HotKeyListener listener = hotKey -> {
            //do nothing
        };
        provider.register(keyStroke, listener);
    }

    private void registerGlobalKeybinds(Document document) {
        XMLGlobalKeybindParser globalKeybindParser = new XMLGlobalKeybindParser(document);
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
        GlobalKeybindListener globalListener = new GlobalKeybindListener();
        globalListener.start();
    }

    @Override
    public void afterStart(ApplicationEvent event) {
        Config config = event.getConfig();
        if (config instanceof XMLConfig) {
            XMLConfig xmlConfig = (XMLConfig) config;
            registerGlobalKeybinds(xmlConfig.getDocument());
        }
    }

    @Nullable
    private Action findAction(Mode mode, KeyStroke pressedKeyStroke) {
        List<Keybind> keybinds = KEYBINDS.get(mode.getName());
        if (keybinds != null) {
            Action action = keybindMatcher.matchAction(keybinds, pressedKeyStroke);
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
        keybindMatcher.start();
        Action action = findAction(currentMode, pressedKeyStroke);
        keybindMatcher.end();
        return action;
    }

    @Nullable
    private Action findAction(List<Keybind> keybinds, KeyStroke pressedKeyStroke) {
        return keybindMatcher.matchAction(keybinds, pressedKeyStroke);
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
        return findAction(GLOBAL_KEYBINDS, pressedKeyStroke);
    }

    public void tryExecuteGlobalAction(KeyStroke keyStroke) {
        Action action = findGlobalAction(keyStroke);
        if (action != null) {
            ActionManager.getInstance().handleAction(action);
        }
    }
}
