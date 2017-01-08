package mah.ui.support.swing.layout;

import mah.ui.layout.Layout;
import mah.ui.theme.Themeable;

import javax.swing.*;

/**
 * Created by zgq on 2017-01-08 12:11
 */
public interface SwingLayout extends Layout,Themeable {

    JPanel getPanel();

}
