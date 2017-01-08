package mah.app;

import mah.ui.UIManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-08 11:35
 */
public class ApplicationManager {

    private static final ApplicationManager INSTANCE = new ApplicationManager();
    private static final List<ApplicationListener> APPLICATION_LISTENER = new ArrayList<>();

    static {
        APPLICATION_LISTENER.add(UIManager.getInstance());
    }

    private ApplicationManager() {

    }

    public void start() {
        ApplicationEvent event = new ApplicationEvent();
        for (ApplicationListener applicationListener : APPLICATION_LISTENER) {
            applicationListener.start(event);
        }
        for (ApplicationListener applicationListener : APPLICATION_LISTENER) {
            applicationListener.afterStart(event);
        }
    }

    public static ApplicationManager getInstance() {
        return INSTANCE;
    }
}
