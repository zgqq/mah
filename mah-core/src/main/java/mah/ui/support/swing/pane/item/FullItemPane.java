package mah.ui.support.swing.pane.item;

import mah.ui.UIException;
import mah.ui.pane.item.FullItem;
import mah.ui.pane.item.ItemPane;
import mah.ui.support.swing.pane.SwingPane;
import mah.ui.support.swing.theme.LayoutThemeImpl;
import mah.ui.support.swing.util.StringUtils;
import mah.ui.support.swing.util.SwingUtils;
import mah.ui.theme.LayoutTheme;
import mah.ui.theme.Themeable;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by zgq on 2017-01-08 14:16
 */
public final class FullItemPane implements ItemPane<FullItem>,SwingPane,Themeable{

    private JPanel panel;
    private JLabel iconLabel;
    private JPanel middlePanel;
    private JTextPane content;
    private JTextPane description;
    private JLabel numLabel;

    public FullItemPane(FullItem fullItem) {
        init(fullItem);
    }

    private void init(FullItem item) {
        try {
            this.panel = SwingUtils.createPanelWithXBoxLayout();
            this.middlePanel = SwingUtils.createPanelWithYBoxLayout();
            iconLabel = new JLabel();
            this.panel.add(iconLabel);
            this.panel.add(middlePanel);
            initContent();
            initDescription();
            initNum();
            reset(item);
        } catch (Exception e) {
            throw new UIException(e);
        }
    }

    private void initNum() {
        this.numLabel = new JLabel("text");
        this.panel.add(this.numLabel);
    }

    private void initDescription() {
        this.description = new JTextPane();
        this.description.setEditable(false);
        this.middlePanel.add(this.description);
    }

    private void initContent() {
        this.content = new JTextPane();
        this.content.setEditable(false);
        this.middlePanel.add(this.content);
    }

    private void setIcon(FullItem item) throws IOException {
        BufferedImage icon = null;
        icon = ImageIO.read(item.getIconInputItem());
        iconLabel.setIcon(new ImageIcon(icon));
    }

    private void setDescription(FullItem item) {
        String descriptionStr = item.getDescription();
        description.setText(StringUtils.getStrBySpecificLength(descriptionStr,40));
    }

    private void setContent(FullItem item) {
        content.setText(StringUtils.getStrBySpecificLength(item.getContent(),40));
    }

    @Override
    public void reset(FullItem item) {
        try {
            setIcon(item);
            setContent(item);
            setDescription(item);
            setNum(item);
        } catch (Exception e) {
            throw new UIException(e);
        }
    }

    private void setNum(FullItem item) {
        this.numLabel.setText(String.valueOf(item.getNum()));
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }


    @Override
    public void apply(LayoutTheme theme) {
        if (theme instanceof LayoutThemeImpl) {
            LayoutThemeImpl layoutTheme = (LayoutThemeImpl) theme;
            decorateContent(layoutTheme);
            decorateDescription(layoutTheme);
            decorateNum(layoutTheme);
            decoratePane(layoutTheme);
        }
    }

    private void decorateNum(LayoutThemeImpl layoutTheme) {
        String numFontColor = layoutTheme.findProperty("num-font-color");
        numLabel.setForeground(Color.decode(numFontColor));
    }

    private void decoratePane(LayoutThemeImpl layoutTheme) {
        String itemBackgroundColor = layoutTheme.findProperty("item-pane-background-color");
        this.panel.setBackground(Color.decode(itemBackgroundColor));
    }

    private void decorateDescription(LayoutThemeImpl layoutTheme) {
        String descriptionColor = layoutTheme.findProperty("description-font-color");
        this.description.setForeground(Color.decode(descriptionColor));
        String descriptionBackgroundColor = layoutTheme.findProperty("description-background-color");
        this.description.setBackground(Color.decode(descriptionBackgroundColor));
    }

    private void decorateContent(LayoutThemeImpl layoutTheme) {
        String contentColor = layoutTheme.findProperty("content-font-color");
        String contentBackgroundColor = layoutTheme.findProperty("content-background-color");
        this.content.setForeground(Color.decode(contentColor));
        this.content.setBackground(Color.decode(contentBackgroundColor));
    }
}
