package mah.ui.support.swing.layout;

import mah.ui.theme.LayoutTheme;

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

    @Override
    public void apply(LayoutTheme theme) {
        abstractClassicLayout.apply(theme);
    }
}
