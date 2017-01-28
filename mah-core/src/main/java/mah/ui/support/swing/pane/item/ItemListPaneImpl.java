package mah.ui.support.swing.pane.item;

import mah.ui.pane.item.*;
import mah.ui.support.swing.pane.SwingPane;
import mah.ui.support.swing.theme.SwingLayoutTheme;
import mah.ui.support.swing.util.SwingUtils;
import mah.ui.theme.LayoutTheme;
import mah.ui.theme.Themeable;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 *
 * Created by zgq on 2017-01-08 14:01
 */
public class ItemListPaneImpl extends ItemListPaneSupport implements ItemListPane, SwingPane {

    private JPanel itemList;
    private final int itemGap = 10;
    private SwingLayoutTheme theme;

    public ItemListPaneImpl(List<? extends Item> items,SwingLayoutTheme theme) {
        super(items);
        if (items == null) {
            throw new NullPointerException();
        }
        this.theme = theme;
        init(items);
    }

    private void init(List<? extends Item> items) {
        if (itemList == null) {
            JPanel panel = new JPanel();
            SwingUtils.setYBoxLayout(panel);
            itemList = panel;
            super.initialize(items);
        }
    }

    private void applyThemeToItems(LayoutTheme theme) {
        for (ItemPane itemPane : getItemPanes()) {
            if (itemPane instanceof Themeable) {
                Themeable themeable = (Themeable) itemPane;
                themeable.apply(theme);
            }
        }
        this.theme = (SwingLayoutTheme) theme;
    }

    @Override
    public void apply(LayoutTheme theme) {
        if (theme instanceof SwingLayoutTheme) {
            SwingLayoutTheme layoutTheme = (SwingLayoutTheme) theme;
            String itemListBackgroundColor = layoutTheme.findProperty("background-color");
            if (itemList != null) {
                this.itemList.setBackground(Color.decode(itemListBackgroundColor));
            }
        }
        applyThemeToItems(theme);
    }

    private void addItemPane(SwingPane itemPanel) {
        itemList.add(itemPanel.getPanel());
        itemList.add(Box.createVerticalStrut(itemGap));
    }

    @Override
    protected ItemPane createItemPane(Item item,int num) {
        if (item instanceof FullItem) {
            FullItem fullItem = (FullItem) item;
            FullItemPane fullItemPane = new FullItemPane(fullItem,num,theme);
            addItemPane(fullItemPane);
            return fullItemPane;
        } else if (item instanceof TextItem) {
            TextItem textItem = (TextItem) item;
            TextItemPane textItemPane = new TextItemPane(textItem,num);
            addItemPane(textItemPane);
            return textItemPane;
        }
        throw new IllegalStateException("Unsupport this kind of item " + item);
    }

    @Override
    public JPanel getPanel() {
        return itemList;
    }
}
