package mah.ui.support.swing.window;

import mah.ui.window.Window;
import mah.ui.window.WindowFactory;
import mah.ui.window.WindowFactoryBean;
import mah.ui.window.WindowProperties;

/**
 * Created by zgq on 2017-01-08 11:46
 */
public class WindowFactoryImpl implements WindowFactory{

    @Override
    public Window createWindow(WindowProperties windowProperties) {
        WindowImpl window = new WindowImpl(windowProperties);
        WindowFactoryBean.getInstance().initBean(window);
        window.init();
        return window;
    }

}
