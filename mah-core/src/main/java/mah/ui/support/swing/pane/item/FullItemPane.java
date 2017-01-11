package mah.ui.support.swing.pane.item;

import mah.ui.UIException;
import mah.ui.pane.item.FullItemImpl;
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
import java.io.InputStream;

/**
 * Created by zgq on 2017-01-08 14:16
 */
public final class FullItemPane implements ItemPane<FullItemImpl>, SwingPane, Themeable {

    private JPanel panel;
    private JLabel iconLabel;
    private JPanel middlePanel;
    private JTextPane content;
    private JTextPane description;
    private JLabel numLabel;

    public FullItemPane(FullItemImpl fullItem) {
        init(fullItem);
    }

    private void init(FullItemImpl item) {
        try {
            this.panel = SwingUtils.createPanelWithXBoxLayout();
            this.middlePanel = SwingUtils.createPanelWithYBoxLayout();
            this.middlePanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
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


    private void setIcon(FullItemImpl item) throws IOException {
        InputStream iconInputStream = item.getIconInputStream();
        BufferedImage icon = ImageIO.read(iconInputStream);
        ImageIcon imageIcon = new ImageIcon(icon); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        Image newimg = image.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
        imageIcon = new ImageIcon(newimg);  // transform it back
        iconLabel.setIcon(imageIcon);
    }

    private void setDescription(FullItemImpl item) {
        String descriptionStr = item.getDescription().getText();
        description.setText(StringUtils.getStrBySpecificLength(descriptionStr, 40));
    }

    private void setContent(FullItemImpl item) {
        content.setText(StringUtils.getStrBySpecificLength(item.getContent().getText(), 40));
    }

    @Override
    public void reset(FullItemImpl item, int num) {
        try {
            setIcon(item);
            setContent(item);
            setDescription(item);
            setNum(num);
        } catch (Exception e) {
            throw new UIException(e);
        }
    }

    @Override
    public void reset(FullItemImpl item) {
        try {
            setIcon(item);
            setContent(item);
            setDescription(item);
            setNum(item);
        } catch (Exception e) {
            throw new UIException(e);
        }
    }

    @Deprecated
    private void setNum(FullItemImpl item) {
        this.numLabel.setText(String.valueOf(item.getNum()));
    }

    private void setNum(int num) {
        this.numLabel.setText(String.valueOf(num));
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
            decorateMiddleContainer(layoutTheme);
            decorateDescription(layoutTheme);
            decorateNum(layoutTheme);
            decoratePane(layoutTheme);
        }
    }

    private void decorateMiddleContainer(LayoutThemeImpl layoutTheme) {
        String numFontColor = layoutTheme.findProperty("middle-container-background-color");
        this.middlePanel.setBackground(Color.decode(numFontColor));
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
