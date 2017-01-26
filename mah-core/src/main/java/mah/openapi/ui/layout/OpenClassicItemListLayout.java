package mah.openapi.ui.layout;

import mah.ui.layout.ClassicItemListLayout;
import mah.ui.pane.item.Item;

import java.util.List;

/**
 * Created by zgq on 2017-01-26 09:42
 */
public interface OpenClassicItemListLayout extends ClassicItemListLayout {

    void compareAndUpdateItems(List<? extends Item> items, String expect);
}
