package mah.ui.layout;

import mah.ui.event.EventHandler;
import mah.ui.key.KeyEvent;
import mah.ui.key.KeyPressedHandler;
import mah.ui.key.KeyReleasedHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-09 12:08
 */
public class LayoutFactoryBean {

    private static final LayoutFactoryBean INSTANCE = new LayoutFactoryBean();
    private static List<EventHandler<? extends KeyEvent>> keyReleasedHandlers = new ArrayList<>();
    private static List<EventHandler<? extends KeyEvent>> keyPressedHandlers = new ArrayList<>();

    static {
        keyPressedHandlers.add(new KeyPressedHandler());
        keyReleasedHandlers.add(new KeyReleasedHandler());
    }

    private LayoutFactoryBean() {

    }

    public static LayoutFactoryBean getInstance() {
        return INSTANCE;
    }

    public void setOnKeyPressed(EventHandler<? extends KeyEvent> eventHandler) {
        keyPressedHandlers.add(eventHandler);
    }

    public void setOnKeyReleased(EventHandler<? extends KeyEvent> eventHandler) {
        keyReleasedHandlers.add(eventHandler);
    }


    public void initBean(Layout layout) {
        for (EventHandler<? extends KeyEvent> keyPressedHandler : keyPressedHandlers) {
            layout.setOnKeyPressed(keyPressedHandler);
        }
        for (EventHandler<? extends KeyEvent> keyReleasedHandler : keyReleasedHandlers) {
            layout.setOnKeyReleased(keyReleasedHandler);
        }
    }

}
