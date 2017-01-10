package mah.keybind;

import mah.action.Action;

import javax.swing.*;
import java.util.List;

/**
 * Created by zgq on 2017-01-09 09:21
 */
public class Keybind {

    private List<KeyStroke> keyStrokes;

    private Action action;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public List<KeyStroke> getKeyStrokes() {
        return keyStrokes;
    }

    public void setKeyStrokes(List<KeyStroke> keyStrokes) {
        this.keyStrokes = keyStrokes;
    }

}
