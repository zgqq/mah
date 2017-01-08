package mah.ui.window;

import mah.app.ApplicationEvent;
import mah.ui.UIManager;
import mah.ui.layout.LayoutType;

/**
 * Created by zgq on 2017-01-08 11:39
 */
public class WindowManager {
    private static WindowManager ourInstance = new WindowManager();
    private WindowFactory windowFactory;
    private Window currentWindow;

    public static WindowManager getInstance() {
        return ourInstance;
    }

    private WindowManager() {
    }

    public void start(ApplicationEvent applicationEvent) {
        this.windowFactory = UIManager.getInstance().getWindowFactory();
        currentWindow = this.windowFactory.createWindow(new WindowProperties.Builder(LayoutType.DEFAULT).build());
    }

    public void showWindow() {
        currentWindow.show();
    }

}
