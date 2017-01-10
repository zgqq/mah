package mah.ui.support.swing.pane.input;

import mah.ui.input.InputPaneFactoryBean;

/**
 * Created by zgq on 2017-01-09 15:45
 */
public class InputPaneFactory {

    public static InputPaneImpl createInputPane() {
        InputPaneImpl inputPane = new InputPaneImpl();
        InputPaneFactoryBean.getInstance().initBean(inputPane);
        inputPane.init();
        return inputPane;
    }

}
