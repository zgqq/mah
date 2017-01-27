package mah.ui.support.swing.layout;

import mah.ui.layout.ClassicAbstractLayoutWrapper;
import mah.ui.support.swing.theme.SwingLayoutTheme;
import mah.ui.theme.LayoutTheme;

import javax.swing.*;

/**
 * Created by zgq on 2017-01-09 10:43
 */
public abstract class SwingAbstractClassicLayoutWrapper extends ClassicAbstractLayoutWrapper implements SwingLayout {

    public SwingAbstractClassicLayoutWrapper(ClassicAbstractLayoutImpl layout) {
        super(layout);
    }

    @Override
    public ClassicAbstractLayoutImpl getLayout() {
        return (ClassicAbstractLayoutImpl) super.getLayout();
    }

    @Override
    public JPanel getPanel() {
        return getLayout().getPanel();
    }

    @Override
    public void apply(LayoutTheme theme) {
        getLayout().apply(theme);
    }

    public SwingLayoutTheme getCurrentTheme() {
        return getLayout().getCurrentTheme();
    }
}
