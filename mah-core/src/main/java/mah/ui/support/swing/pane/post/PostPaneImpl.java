package mah.ui.support.swing.pane.post;

import mah.ui.pane.post.Post;
import mah.ui.pane.post.PostPane;
import mah.ui.support.swing.pane.SwingPane;
import mah.ui.support.swing.theme.LayoutThemeImpl;
import mah.ui.support.swing.util.SwingUtils;
import mah.ui.theme.LayoutTheme;

import javax.swing.*;
import java.awt.*;

/**
 * Created by zgq on 2017-01-17 14:36
 */
public class PostPaneImpl implements SwingPane,PostPane{

    private final JTextArea title;
    private final JTextArea content;
    private final JPanel mainWindow;
    private final Font font = new Font(null,0,18);

    private PostPaneImpl() {
        mainWindow = SwingUtils.createPanelWithYBoxLayout();
        title = new JTextArea();
        content = new JTextArea();
        init();
    }

    private void initTop() {
        title.setFont(font);
        title.setEditable(false);
        title.setFocusable(false);
        title.setLineWrap(true);
        mainWindow.add(title);
    }

    private void init() {
        mainWindow.setPreferredSize(new Dimension(600,400));
        initTop();
        initBottom();
    }

    private void initBottom() {
        content.setLineWrap(true);
        content.setEditable(false);
        content.setFocusable(false);
        content.setFont(font);
        mainWindow.add(content);
    }

    @Override
    public void reset(Post post) {
        title.setText(post.getTitle().getText());
        content.setText(post.getContent().getText());
    }

    @Override
    public JPanel getPanel() {
        return mainWindow;
    }

    @Override
    public void apply(LayoutTheme theme) {
        if (theme instanceof LayoutThemeImpl) {
            LayoutThemeImpl layoutTheme = (LayoutThemeImpl) theme;
            String backgroundColor = layoutTheme.findProperty("background-color");
            title.setForeground(Color.WHITE);
            title.setBackground(Color.decode(backgroundColor));
            content.setForeground(Color.WHITE);
            content.setBackground(Color.decode(backgroundColor));
        }
    }

    public static PostPaneImpl instance() {
        return new PostPaneImpl();
    }
}
