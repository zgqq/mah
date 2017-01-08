package mah.ui.support.swing.pane.input;

import mah.ui.pane.input.InputPane;
import mah.ui.support.swing.pane.SwingPane;
import mah.ui.support.swing.theme.LayoutThemeImpl;
import mah.ui.theme.LayoutTheme;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by zgq on 2017-01-08 12:00
 */
public class InputPaneImpl implements InputPane,SwingPane{

    private JTextPane input;
    private JPanel panel;

    public InputPaneImpl() {
        init();
    }

    private void init() {
        this.panel = new JPanel();
        this.panel.setLayout(new GridBagLayout());
        initInput();
    }

    private void initInput() {
        input = new JTextPane();
        input.setPreferredSize(new Dimension(540,60));
        input.setFont(new Font(null, Font.PLAIN, 28));
        input.setCaretColor(Color.WHITE);
        disposeKeybinds(input);
        this.panel.setPreferredSize(new Dimension(600,70));
        this.panel.add(input);
    }

    private void disposeKeybinds(JTextPane input) {
        InputMap inputMap = input.getInputMap();
        KeyStroke keystroke = KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK, false);
        inputMap.put(keystroke, "none");
        KeyStroke keystroke2 = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, KeyEvent.CTRL_MASK, false);
        inputMap.put(keystroke2, "none");
        KeyStroke keystroke3 = KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0, false);
        inputMap.put(keystroke3, "none");
        KeyStroke keystroke4 = KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_MASK, false);
        inputMap.put(keystroke4, "none");
        KeyStroke keystroke5 = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false);
        inputMap.put(keystroke5, "none");
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void apply(LayoutTheme theme) {
        if (theme instanceof LayoutThemeImpl) {
            LayoutThemeImpl layoutTheme = (LayoutThemeImpl) theme;
            String panelBackgroundColor= layoutTheme.findProperty("input-pane-background-color");
            panel.setBackground(Color.decode(panelBackgroundColor));
            String inputFieldBackgroundColor = layoutTheme.findProperty("input-field-background-color");
            input.setBackground(Color.decode(inputFieldBackgroundColor));
            String fontColor = layoutTheme.findProperty("input-field-font-color");
            input.setForeground(Color.decode(fontColor));
        }
    }
}
