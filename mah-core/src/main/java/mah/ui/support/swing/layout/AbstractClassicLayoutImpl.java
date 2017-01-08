package mah.ui.support.swing.layout;

import mah.ui.layout.AbstractClassicLayout;
import mah.ui.pane.Pane;
import mah.ui.support.swing.pane.SwingPane;
import mah.ui.support.swing.pane.input.InputPaneImpl;
import mah.ui.support.swing.theme.LayoutThemeImpl;
import mah.ui.theme.LayoutTheme;
import mah.ui.theme.Themeable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by zgq on 2017-01-08 11:56
 */
public class AbstractClassicLayoutImpl implements AbstractClassicLayout, SwingLayout{

    private JPanel panel;
    private InputPaneImpl inputPane;
    private SwingPane bottomPane;
    private LayoutThemeImpl currentTheme;

    public AbstractClassicLayoutImpl() {
        init();
    }

    private void init() {
        this.panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        this.panel.setLayout(boxLayout);
        this.inputPane = new InputPaneImpl();
        this.panel.add(inputPane.getPanel());
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    private void updateBottomPane(SwingPane swingPane) {
        removeBottomPane();
        addBottomPane(swingPane);
    }

    private void addBottomPane(SwingPane swingPane) {
        this.panel.add(swingPane.getPanel());
    }

    private void removeBottomPane() {
        if (bottomPane != null)
            this.panel.remove(bottomPane.getPanel());
    }

    @Override
    public void updatePane(Pane pane) {
        if (pane instanceof SwingPane) {
            SwingPane swingPane = (SwingPane) pane;
            updateBottomPane(swingPane);
            bottomPane =swingPane;
            applyThemeToPane();
        }
    }

    private void applyToLayout() {
        String layoutColor = currentTheme.findProperty("layout-background-color");
        this.panel.setBackground(Color.decode(layoutColor));
    }

    private void applyThemeToPane() {
        if (bottomPane != null && bottomPane instanceof Themeable) {
            Themeable themeable = bottomPane;
            themeable.apply(currentTheme);
        }
    }

    @Override
    public void apply(LayoutTheme theme) {
        if (theme instanceof LayoutThemeImpl) {
            inputPane.apply(theme);
            currentTheme = (LayoutThemeImpl) theme;
            applyToLayout();
            applyThemeToPane();
        }
    }
}
