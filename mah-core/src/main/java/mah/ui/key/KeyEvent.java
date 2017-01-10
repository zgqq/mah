package mah.ui.key;

/**
 * Created by zgq on 2017-01-09 09:46
 */
public class KeyEvent {
    private int keyCode;
    private KeystateManager keyState = KeystateManager.getInstance();
    private boolean modifierKey;

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public boolean altPressed() {
        return keyState.altPressed();
    }

    public boolean metaPressed() {
        return keyState.metaPressed();
    }

    public boolean ctrlPressed() {
        return keyState.ctrlPressed();
    }

    public boolean shiftPressed() {
        return keyState.shiftPressed();
    }


    public boolean isModifierKey() {
        switch (keyCode) {
            case java.awt.event.KeyEvent.VK_CONTROL:
            case java.awt.event.KeyEvent.VK_ALT:
            case java.awt.event.KeyEvent.VK_META:
            case java.awt.event.KeyEvent.VK_SHIFT:
                return true;
            default:
                return false;
        }
    }
}


