package mah.ui.support.swing;

import mah.ui.support.swing.window.WindowFactoryImpl;
import mah.ui.window.WindowFactory;

/**
 * Created by zgq on 2017-01-08 11:45
 */
public class FactoryHelper {

    public WindowFactory createWindowFactory() {
        return new WindowFactoryImpl();
    }

}
