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
 *
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
        }
    }

    @Override
    public void apply(LayoutTheme theme) {
        if (theme instanceof LayoutThemeImpl) {
            LayoutThemeImpl layoutTheme = (LayoutThemeImpl) theme;
            String itemListBackgroundColor = layoutTheme.findProperty("background-color");
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


    @Override
    protected ItemPane createItemPane(Item item,int num) {
        init();
        if (item instanceof FullItem) {
            FullItem fullItem = (FullItem) item;
            FullItemPane fullItemPane = new FullItemPane(fullItem,num);
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
