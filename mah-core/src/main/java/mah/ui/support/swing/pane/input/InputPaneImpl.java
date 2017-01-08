package mah.ui.support.swing.pane.input;

import mah.ui.pane.input.InputPane;
import mah.ui.support.swing.pane.SwingPane;

import javax.swing.*;
import java.awt.*;

/**
 * Created by zgq on 2017-01-08 12:00
 */
public class InputPaneImpl implements InputPane,SwingPane{

    private JTextField input;
    private JPanel panel;

    public InputPaneImpl() {

    }

    private void init() {
        this.panel = new JPanel();
        this.panel.setSize(800,75);
        this.panel.setLayout(new GridBagLayout());
        input = new JTextField(10);
        this.panel.add(input);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
