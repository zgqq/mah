package mah.ui.support.swing.util;

import javax.swing.*;

/**
 * Created by zgq on 2017-01-08 13:54
 */
public final class SwingUtils {

    private SwingUtils() {

    }

    public static JPanel createPanelWithXBoxLayout() {
        JPanel panel = new JPanel();
        setXBoxLayout(panel);
        return panel;
    }

    public static JPanel createPanelWithYBoxLayout() {
        JPanel panel = new JPanel();
        setYBoxLayout(panel);
        return panel;
    }

    public static BoxLayout setYBoxLayout(JPanel panel) {
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);
        return boxLayout;
    }

    public static BoxLayout setXBoxLayout(JPanel panel) {
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.X_AXIS);
        panel.setLayout(boxLayout);
        return boxLayout;

    }


}
