package mah.ui.support.swing.pane.item;

import mah.common.util.CollectionUtils;
import mah.ui.UIException;
import mah.ui.pane.item.ItemPane;
import mah.ui.pane.Text;
import mah.ui.pane.item.TextItem;
import mah.ui.support.swing.pane.SwingPane;
import mah.ui.support.swing.theme.LayoutThemeImpl;
import mah.ui.support.swing.util.StringUtils;
import mah.ui.support.swing.util.SwingUtils;
import mah.ui.theme.LayoutTheme;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyledDocument;
import java.awt.*;

/**
 * Created by zgq on 2017-01-14 13:36
 */
public class TextItemPane implements ItemPane<TextItem>, SwingPane {

    private static final int TEXT_LEN = 150;
    private JPanel panel;
    private JPanel container;
    private JPanel middlePanel;
    private JTextPane text;
    private JLabel numLabel;
    private TextItem item;
    private ItemPaneTheme theme;
    private static final int ITEM_HEIGHT = 58;

    public TextItemPane(TextItem item, int num) {
        init(item, num);
    }

    private void init(TextItem item, int num) {
        try {
            this.panel = new JPanel();
            this.panel.setLayout(new BorderLayout());
            this.panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
            this.container = SwingUtils.createPanelWithXBoxLayout();
            this.container.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 5));
            this.middlePanel = SwingUtils.createPanelWithYBoxLayout();
            this.middlePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
            this.container.add(middlePanel);
            this.panel.add(container);
            this.panel.setPreferredSize(new Dimension(580, ITEM_HEIGHT));
            initText();
            initNum();
            this.item = item;
            reset(item, num);
        } catch (Exception e) {
            throw new UIException(e);
        }
    }

    private void initNum() {
        this.numLabel = new JLabel("text");
        this.container.add(this.numLabel);
    }

    private void initText() {
        this.text = new JTextPane();
        this.text.setEditable(false);
        this.middlePanel.add(this.text);
    }

    @Override
    public TextItem getItem() {
        return item;
    }

    private void applyBackground(Color color) {
        this.panel.setBackground(color);
        applyBackgroundToMiddlePanel(color);
    }

    private void applyBackgroundToMiddlePanel(Color color) {
        this.text.setBackground(color);
        this.container.setBackground(color);
        this.middlePanel.setBackground(color);
    }

    @Override
    public void pending() {
        applyBackgroundToMiddlePanel(theme.getPendingBackgroundColor());
        Text content = item.getText();
        java.util.List<Integer> highlightIndexs = content.getHighlightIndexs();
        if (CollectionUtils.isNotEmpty(highlightIndexs)) {
            this.text.setDocument(highlightText(highlightIndexs, content));
        }
    }

    private DefaultStyledDocument highlightText(java.util.List<Integer> highlightIndexs, Text textObj) {
        String text = textObj.getText();
        String con = StringUtils.getStrBySpecificLength(text, TEXT_LEN);
        DefaultStyledDocument hightlightDocument = HightlightHelper.createHightlightDocument(con, highlightIndexs, theme.getHighlightStyle(), theme.getNormalTextStyle());
        return hightlightDocument;
    }

    @Override
    public void reset(TextItem item, int num) {
        setContent(item);
        setNum(num);
        this.item = item;
    }

    private void setNum(int num) {
        this.numLabel.setText(String.valueOf(num));
    }

    private void setContent(TextItem item) {
        setNormalText(item.getText());
    }

    @Override
    public void unpending() {
        applyBackground(this.theme);
        setNormalText(this.item.getText());
    }

    private void setNormalText(Text text) {
        String qualifyStr = StringUtils.getStrBySpecificLength(text.getText(), TEXT_LEN);
        this.text.setDocument(createNormalDocument(qualifyStr));
    }

    private StyledDocument createNormalDocument(String text) {
        StyledDocument document = new DefaultStyledDocument();
        try {
            Style style = theme != null ? theme.getNormalTextStyle() : null;
            document.insertString(0, text, style);
            return document;
        } catch (BadLocationException e) {
            throw new UIException(e);
        }
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void apply(LayoutTheme theme) {
        if (theme instanceof LayoutThemeImpl) {
            LayoutThemeImpl layoutTheme = (LayoutThemeImpl) theme;
            this.theme = new ItemPaneTheme(layoutTheme);
            setNormalText(item.getText());
            decorateNum(this.theme);
            applyBackground(this.theme);
        }
    }

    private void applyBackground(ItemPaneTheme theme) {
        applyBackground(theme.getBackgroundColor());
    }

    private void decorateNum(ItemPaneTheme theme) {
        numLabel.setForeground(theme.getNumColor());
    }


}
