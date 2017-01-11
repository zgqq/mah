package mah.ui.support.swing.pane.item;

import mah.ui.pane.item.*;
import mah.ui.support.swing.pane.SwingPane;
import mah.ui.support.swing.theme.LayoutThemeImpl;
import mah.ui.support.swing.util.SwingUtils;
import mah.ui.theme.LayoutTheme;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Created by zgq on 2017-01-08 14:01
 */
public class ItemListPaneImpl extends ItemListPaneSupport implements ItemListPane, SwingPane {

    private JPanel itemList;
    private final int itemGap = 10;

    public ItemListPaneImpl(List<? extends Item> items) {
        super(items);
        init();
    }

    private void init() {
        if (itemList == null) {
            itemList = SwingUtils.createPanelWithYBoxLayout();
            itemList.setBorder(BorderFactory.createEmptyBorder(5, 20, 0, 30));
        }
    }

    @Override
    public void apply(LayoutTheme theme) {
        if (theme instanceof LayoutThemeImpl) {
            LayoutThemeImpl layoutTheme = (LayoutThemeImpl) theme;
            String itemListBackgroundColor = layoutTheme.findProperty("item-list-background-color");
            if (itemList != null) {
                this.itemList.setBackground(Color.decode(itemListBackgroundColor));
            }
        }
        super.apply(theme);
    }

    private void addItemPane(SwingPane itemPanel) {
        itemList.add(itemPanel.getPanel());
        itemList.add(Box.createVerticalStrut(itemGap));
    }

//    @Override
//    protected void computerSize() {
//        this.itemList.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
//    }

    @Override
    protected ItemPane createItemPane(Item item) {
        init();
        if (item instanceof FullItemImpl) {
            FullItemImpl fullItem = (FullItemImpl) item;
            FullItemPane fullItemPane = new FullItemPane(fullItem);
            addItemPane(fullItemPane);
            return fullItemPane;
        }
        throw new IllegalStateException("Unsupport this kind of item " + item);
    }

    @Override
    public JPanel getPanel() {
        return itemList;
    }


}
