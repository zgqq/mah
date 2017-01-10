package mah.ui.support.swing.window;

import mah.ui.layout.Layout;
import mah.ui.layout.LayoutType;
import mah.ui.support.swing.layout.DefaultLayout;
import mah.ui.support.swing.layout.SwingLayout;
import mah.ui.theme.ThemeManager;
import mah.ui.window.WindowProperties;
import mah.ui.window.WindowSupport;

import javax.swing.*;

/**
 * Created by zgq on 17-1-8 11:25.
 */
public class WindowImpl extends WindowSupport {

    private final WindowProperties properties;
    private JFrame frame;
    private DefaultLayout defaultLayout;
    private SwingLayout currentLayout;

    public WindowImpl(WindowProperties properties) {
        this.properties = properties;
    }

    @Override
    public void init() {
        initWindow();
        initLayout();
    }


    private void initLayout() {
        if (properties.getLayoutType() == LayoutType.DEFAULT) {
            defaultLayout = new DefaultLayout();
            defaultLayout.init();
            defaultLayout.setDefaultMode();
            addLayout(defaultLayout);
            ThemeManager.getInstance().applyTheme(defaultLayout);
            currentLayout = defaultLayout;
        }
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private void initWindow() {
        frame = new JFrame();
        frame.setUndecorated(true);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setType(java.awt.Window.Type.UTILITY);
    }

    @Override
    public void show() {
        frame.setVisible(true);
    }

    private void updateLayout(SwingLayout layout) {
        JPanel currentPanel = currentLayout.getPanel();
        JPanel panel = layout.getPanel();
        if (currentPanel.equals(panel)) {
            return;
        }
        removeLayout();
        addLayout(layout);
        layout.onSetCurrentLayout();
    }

    private void addLayout(SwingLayout layout) {
        frame.add(layout.getPanel());
    }

    private void removeLayout() {
        if (currentLayout != null)
            frame.remove(currentLayout.getPanel());
    }

    @Override
    public boolean isShowing() {
        return frame.isShowing();
    }

    @Override
    public boolean isFocused() {
        return frame.isFocused();
    }

    @Override
    public void hide() {
        frame.setVisible(false);
    }

    @Override
    public void moveToRight() {
        //
    }

    @Override
    public void centerOnScreen() {

    }

    @Override
    public void moveToLeft() {

    }

    @Override
    public void useDefaultLayoutAsCurrentLayout() {
        defaultLayout.use();
        frame.pack();
    }

    @Override
    public void setCurrentLayout(Layout layout) {

        if (layout instanceof SwingLayout) {
            SwingLayout swingLayout = (SwingLayout) layout;
            updateLayout(swingLayout);
            currentLayout = swingLayout;
            frame.pack();
        }
    }

    @Override
    public Layout getCurrentLayout() {
        return currentLayout;
    }

    @Override
    public Layout getDefaultLayout() {
        return defaultLayout;
    }
}
