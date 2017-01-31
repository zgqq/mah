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

    public boolean isEmpty() {
        return pendingKeybinds == null || pendingKeybinds.isEmpty();
    }

    public Action continueMatch(KeyStroke pressedKeyStroke) {
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
    public List<Keybind> matchKeybind(List<Keybind> keybinds, KeyStroke pressedKeyStroke) {
        List<Keybind> pendingKeybinds = new ArrayList<>();
        for (Keybind keybind : keybinds) {
            List<KeyStroke> keyStrokes = keybind.getKeyStrokes();
            KeyStroke keyStroke = keyStrokes.get(0);
            if (keyStroke.equals(pressedKeyStroke)) {
                pendingKeybinds.add(keybind);
            }
        }
        return pendingKeybinds;
    }

    @Nullable
    public Action addPendingKeybindIfNotFoundAction(List<Keybind> keybinds, KeyStroke pressedKeyStroke) {
        if (!isEmpty()) {
            throw new IllegalStateException("Pending keybind is not empty");
        }
        Action action = null;
        List<Keybind> pendingKeybinds = matchKeybind(keybinds, pressedKeyStroke);
        final int size = pendingKeybinds.size();
        if (size == 0) {
            return null;
        } else if (size == 1) {
            Keybind keybind = pendingKeybinds.get(0);
            if (keybind.getKeyStrokes().size() == 1) {
                action = keybind.getAction();
                isFoundAction = true;
                return action;
            }
        }
        remainingKeybinds.addAll(pendingKeybinds);
        return action;
    }

    public void start() {
        isFoundAction = false;
        remainingKeybinds = new ArrayList<>();
    }

    public void end() {
        pendingKeybinds = remainingKeybinds;
        if (isFoundAction || pendingKeybinds.size() == 0) {
            keyIndex = 0;
        } else {
            // if not found action and the count of pending keybind greater than 0
            keyIndex++;
        }
    }
}
