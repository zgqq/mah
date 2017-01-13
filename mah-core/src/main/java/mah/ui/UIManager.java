package mah.ui;

import mah.app.ApplicationEvent;
import mah.app.ApplicationListener;
import mah.app.system.SystemMode;
import mah.mode.ModeManager;
import mah.ui.input.InputMode;
import mah.ui.pane.item.ItemMode;
import mah.ui.support.swing.FactoryHelper;
import mah.ui.theme.ThemeManager;
import mah.ui.window.WindowFactory;
import mah.ui.window.WindowManager;
import mah.ui.window.WindowMode;

import javax.swing.*;

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

    private void registerMode() {
        ModeManager.getInstance().registerMode(new SystemMode());
        ModeManager.getInstance().registerMode(new WindowMode(ModeManager.getInstance().getMode(SystemMode.NAME)));
        ModeManager.getInstance().registerMode(new InputMode(ModeManager.getInstance().getMode(WindowMode.NAME)));
        ModeManager.getInstance().registerMode(new ItemMode(ModeManager.getInstance().getMode(InputMode.NAME)));
    }

    @Override
    public void start(ApplicationEvent applicationEvent) {
        registerMode();
        ThemeManager.getInstance().start(applicationEvent);
    }

    @Override
    public void afterStart(ApplicationEvent applicationEvent) {
        runLater(()->{
            WindowManager.getInstance().createWindow();
            WindowManager.getInstance().showWindow();
        });
    }

    public WindowFactory getWindowFactory() {
        return helper.createWindowFactory();
    }

    public void runLater(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }
}
