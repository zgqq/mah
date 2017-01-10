package mah.ui.window;

import mah.action.ActionHandler;
import mah.mode.ModeManager;

/**
 * Created by zgq on 2017-01-09 09:48
 */
public class WindowFactoryBean {

    private static final WindowFactoryBean INSTANCE = new WindowFactoryBean();


    private WindowFactoryBean() {
    }

    public static WindowFactoryBean getInstance() {
        return INSTANCE;
    }


    private void registerOrUpdateMode(Window window) {
        if (window instanceof ActionHandler) {
            ActionHandler actionHandler = (ActionHandler) window;
            ModeManager.getInstance().registerOrUpdateMode(new WindowMode("window_mode"), actionHandler);
        }
    }

    public void initBean(Window window) {
        registerOrUpdateMode(window);
    }

}
