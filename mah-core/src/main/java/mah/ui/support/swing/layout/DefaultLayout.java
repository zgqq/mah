package mah.ui.support.swing.layout;

import javax.swing.*;

/**
 * Created by zgq on 2017-01-08 12:24
 */
public class DefaultLayout implements SwingLayout{

    private final AbstractClassicLayoutImpl abstractClassicLayout;

    public DefaultLayout() {
        this.abstractClassicLayout = new AbstractClassicLayoutImpl();
    }

    @Override
    public JPanel getPanel() {
        return abstractClassicLayout.getPanel();
    }
}
