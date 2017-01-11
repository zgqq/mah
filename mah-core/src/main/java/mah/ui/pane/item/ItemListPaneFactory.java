package mah.ui.pane.item;

import mah.ui.event.EventHandler;

import java.util.List;

/**
 * Created by zgq on 16-12-24.
 */
public interface ItemListPaneFactory {

     ItemListPane createItemListPane(List<? extends Item> items);

     ItemListPane createItemListPane(List<? extends Item> items, List<EventHandler<? extends ItemSelectedEvent>> itemSelectedEventHandlers);
}
