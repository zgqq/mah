package mah.keybind;

import mah.action.Action;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-29 22:04
 */
public class KeybindMatcher {

    private List<Keybind> pendingKeybinds;
    private List<Keybind> remainingKeybinds;
    private boolean isFoundAction;
    private int keyIndex;

    private boolean isEmpty() {
        return pendingKeybinds == null || pendingKeybinds.isEmpty();
    }

    private Action continueMatch(KeyStroke pressedKeyStroke) {
        final List<Keybind> prevKeybinds = pendingKeybinds;
        for (Keybind keybinding : prevKeybinds) {
            List<KeyStroke> keyStrokes = keybinding.getKeyStrokes();
            KeyStroke keyStroke = keyStrokes.get(keyIndex);
            if (keyStroke.equals(pressedKeyStroke)) {
                if (keyStrokes.size() - 1 == keyIndex) {
                    isFoundAction = true;
                    return keybinding.getAction();
                }
                remainingKeybinds.add(keybinding);
            }
        }
        return null;
    }

    @Nullable
    public Keybind matchKeybind(List<Keybind> keybinds, KeyStroke pressedKeyStroke) {
        for (Keybind keybind : keybinds) {
            List<KeyStroke> keyStrokes = keybind.getKeyStrokes();
            KeyStroke keyStroke = keyStrokes.get(0);
            if (keyStroke.equals(pressedKeyStroke)) {
                return keybind;
            }
        }
        return null;
    }

    @Nullable
    public Action matchAction(List<Keybind> keybinds, KeyStroke pressedKeyStroke) {
        Action action = null;
        if (isEmpty()) {
            Keybind keybind = matchKeybind(keybinds, pressedKeyStroke);
            if (keybind == null) {
                return null;
            }
            if (keybind.getKeyStrokes().size() == 1) {
                action = keybind.getAction();
                isFoundAction = true;
            } else {
                remainingKeybinds.add(keybind);
            }
        } else {
            action = continueMatch(pressedKeyStroke);
        }
        return action;
    }

    public void start() {
        isFoundAction = false;
        remainingKeybinds = new ArrayList<>();
    }

    public void end() {
        pendingKeybinds = remainingKeybinds;
        //If there are pending keybinds
        if (isFoundAction || pendingKeybinds.size() == 0) {
            keyIndex = 0;
        } else {
            keyIndex++;
        }
    }
}
