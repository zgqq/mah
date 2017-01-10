package mah.ui.key;

import mah.ui.event.EventHandler;

/**
 * Created by zgq on 2017-01-09 10:26
 */
public class KeyPressedHandler implements EventHandler<KeyEvent> {

    @Override
    public void handle(KeyEvent event) {
        int keyCode = event.getKeyCode();
        KeystateManager keyState = KeystateManager.getInstance();
        switch (keyCode) {
            case java.awt.event.KeyEvent.VK_CONTROL:
                keyState.setCtrl(true);
                break;
            case java.awt.event.KeyEvent.VK_ALT:
                keyState.setAlt(true);
                break;
            case java.awt.event.KeyEvent.VK_META:
                keyState.setMeta(true);
                break;
            default:
                break;
        }
    }

}
