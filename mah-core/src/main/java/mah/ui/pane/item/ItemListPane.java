package mah.ui.pane.item;

import mah.ui.pane.Pane;

import java.util.List;

/**
 * Created by zgq on 2017-01-08 13:50
 */
public interface ItemListPane extends ItemList,Pane{

    void reset(List<? extends Item> items);

}
