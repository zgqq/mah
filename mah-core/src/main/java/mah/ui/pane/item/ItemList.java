package mah.ui.pane.item;

import mah.ui.event.EventHandler;

/**
 * Created by zgq on 2017-01-10 15:00
 */
public interface ItemList {

    void setOnItemSelected(EventHandler<? extends ItemSelectedEvent> eventHandler);

    void updateItem(Item item, int num);

}
