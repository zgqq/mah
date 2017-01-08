package mah.ui.pane.item;

import java.util.List;

/**
 * Created by zgq on 16-12-24.
 */
public interface ItemListPaneFactory {

     ItemListPane createItemListPane(List<? extends Item> items);

}
