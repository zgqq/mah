package mah.ui.support.swing.window;

import mah.ui.layout.Layout;
import mah.ui.layout.LayoutType;
import mah.ui.support.swing.layout.DefaultLayout;
import mah.ui.support.swing.layout.SwingLayout;
import mah.ui.window.Window;
import mah.ui.window.WindowProperties;

import javax.swing.*;

/**
 * Created by zgq on 17-1-8 11:25.
 */
public class WindowImpl implements Window{

    private final WindowProperties properties;
    private JFrame frame;
    private SwingLayout defaultLayout;
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
            this.defaultLayout = new DefaultLayout();
            addLayout(defaultLayout);
            currentLayout = defaultLayout;
        }
    }

    private void initWindow() {
        frame = new JFrame();
//        frame.setPreferredSize(new Dimension(600,200));
        frame.setUndecorated(false);
        frame.setAlwaysOnTop(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo( null );
        frame.setType(java.awt.Window.Type.UTILITY);
        frame.pack();
    }

    @Override
    public void show() {
        frame.setVisible(true);
    }

    private void updateLayout(SwingLayout layout) {
        removeLayout();
        addLayout(layout);
    }

    private void addLayout(SwingLayout layout) {
        frame.add(layout.getPanel());
    }

    private void removeLayout() {
        if(currentLayout!=null)
        frame.remove(currentLayout.getPanel());
    }

    @Override
    public void update(Layout layout) {
        if (layout instanceof SwingLayout) {
            SwingLayout swingLayout = (SwingLayout) layout;
            updateLayout(swingLayout);
            currentLayout = swingLayout;
            frame.pack();
        }
    }
}
