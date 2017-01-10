package mah.ui.key;

import mah.ui.event.EventHandler;

/**
 * Created by zgq on 2017-01-09 10:28
 */
public class KeyReleasedHandler implements EventHandler<KeyEvent> {

    private void setCtrlPressed() {
        KeystateManager.getInstance().setCtrl(false);
    }

    private void setAltPressed() {
        KeystateManager.getInstance().setAlt(false);
    }

    private void setMetaPressed() {
        KeystateManager.getInstance().setMeta(false);
    }

    @Override
    public void handle(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (keyCode == java.awt.event.KeyEvent.VK_CONTROL) {
            setCtrlPressed();
            return;
        } else if (keyCode == java.awt.event.KeyEvent.VK_ALT) {
            setAltPressed();
            return;
        } else if (keyCode == java.awt.event.KeyEvent.VK_ENTER) {
            return;
        } else if (keyCode == java.awt.event.KeyEvent.VK_META) {
            setMetaPressed();
            return;
        }
    }

}
