package mah.ui.support.swing.pane.item;

import mah.ui.pane.item.Item;
import mah.ui.pane.item.ItemListPane;
import mah.ui.pane.item.ItemListPaneFactorySupport;

import java.util.List;

/**
 * Created by zgq on 2017-01-08 14:58
 */
public class ItemListPaneFactoryImpl extends ItemListPaneFactorySupport {

    @Override
    protected ItemListPane createItemsPane(List<? extends Item> items) {
        return new ItemListPaneImpl(items);
    }

}
