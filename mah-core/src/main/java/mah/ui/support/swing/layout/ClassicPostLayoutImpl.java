package mah.ui.support.swing.layout;

import mah.mode.Mode;
import mah.mode.ModeManager;
import mah.ui.layout.ClassicPostLayout;
import mah.ui.layout.LayoutFactoryBean;
import mah.ui.layout.ModeListener;
import mah.ui.pane.post.Post;
import mah.ui.support.swing.pane.post.PostPaneImpl;
import mah.ui.support.swing.theme.SwingLayoutTheme;
import mah.ui.theme.LayoutTheme;
import mah.ui.theme.ThemeManager;
import mah.ui.window.WindowMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Created by zgq on 2017-01-17 14:35
 */
public class ClassicPostLayoutImpl extends SwingLayoutSupport implements ClassicPostLayout {

    public static final String NAME = "classic_post_layout";
    private static final Logger logger = LoggerFactory.getLogger(ClassicPostLayoutImpl.class);
    private PostPaneImpl postPane;
    private boolean init;
    private SwingLayoutTheme theme;

    private ClassicPostLayoutImpl() {}

    @Override
    public void init() {
        if (init) {
            logger.warn("The layout has already been initialized");
            return;
        }
        postPane = PostPaneImpl.instance();
        JPanel panel = postPane.getPanel();
        panel.addKeyListener(new KeyHandler());
        init = true;
    }

    @Override
    public void registerMode(Mode mode, ModeListener modeListener) {
        Mode windowMode = WindowMode.getOrRegisterMode();
        mode.setParent(windowMode);
        setMode(mode);
        addModeListener(modeListener);
        ModeManager.getInstance().registerMode(mode);
    }

    @Override
    public void setPost(Post post) {
        WindowMode.triggerMode();
        triggerMode();
        postPane.reset(post);
        applyTheme();
        SwingUtilities.invokeLater(() -> {
            postPane.getPanel().setFocusable(true);
            postPane.getPanel().requestFocusInWindow();
        });
    }

    @Override
    public JPanel getPanel() {
        return postPane.getPanel();
    }

    @Override
    public SwingLayoutTheme getCurrentTheme() {
        return theme;
    }

    private static ClassicPostLayoutImpl instance;

    public static ClassicPostLayoutImpl instance() {
        if (instance == null) {
            instance = new ClassicPostLayoutImpl();
            LayoutFactoryBean.getInstance().initBean(instance);
        }
        return instance;
    }

    private void applyTheme() {
        if (theme != null) {
            apply(theme);
        } else {
            LayoutTheme layoutTheme = ThemeManager.getInstance().getLayoutTheme(NAME);
            apply(layoutTheme);
        }
    }

    @Override
    protected void applyTheme(SwingLayoutTheme layoutTheme) {
        postPane.apply(layoutTheme);
    }
}
