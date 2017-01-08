package mah.ui;

import mah.app.ApplicationEvent;
import mah.app.ApplicationListener;
import mah.ui.support.swing.FactoryHelper;
import mah.ui.window.WindowFactory;
import mah.ui.window.WindowManager;

/**
 * Created by zgq on 2017-01-08 11:38
 */
public class UIManager implements ApplicationListener{

    private static UIManager ourInstance = new UIManager();
    private FactoryHelper helper = new FactoryHelper();

    public static UIManager getInstance() {
        return ourInstance;
    }

    private UIManager() {
    }

    @Override
    public void start(ApplicationEvent applicationEvent) {
        WindowManager.getInstance().start(applicationEvent);
    }

    @Override
    public void afterStart(ApplicationEvent applicationEvent) {
        WindowManager.getInstance().showWindow();
    }

    public WindowFactory getWindowFactory() {
        return helper.createWindowFactory();
    }
}
