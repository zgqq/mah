package mah.ui.support.swing.pane.item;

import mah.ui.UIException;
import mah.ui.event.EventHandler;
import mah.ui.pane.item.Item;
import mah.ui.pane.item.ItemListPane;
import mah.ui.pane.item.ItemSelectedEvent;
import mah.ui.support.swing.theme.SwingLayoutTheme;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zgq on 2017-01-08 14:58
 */
public class ItemListPaneFactory {

    private static final Map<CacheKey, ItemListPane> ITEMSPANE = new HashMap<>();

    public ItemListPane createItemListPane(List<? extends Item> items, List<EventHandler<? extends ItemSelectedEvent>> itemSelectedEventHandlers, SwingLayoutTheme theme) {
        ItemListPane itemListPane = createItemListPane(items, theme);
        for (EventHandler<? extends ItemSelectedEvent> itemSelectedEventHandler : itemSelectedEventHandlers) {
            itemListPane.setOnItemSelected(itemSelectedEventHandler);
        }
        return itemListPane;
    }

    public ItemListPane createItemListPane(List<? extends Item> items, SwingLayoutTheme theme) {
        if (items == null) {
            throw new UIException("items could not be null");
        }
        CacheKey cacheKey = new CacheKey(items);
        ItemListPane itemListPane;
        itemListPane = ITEMSPANE.get(cacheKey);
        if (itemListPane != null) {
            itemListPane.reset(items);
            return itemListPane;
        }
        itemListPane = new ItemListPaneImpl(items, theme);
        ITEMSPANE.put(cacheKey, itemListPane);
        return itemListPane;
    }

    static class CacheKey {
        private List<? extends Item> items;

        public CacheKey(List<? extends Item> items) {
            this.items = items;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CacheKey cacheKey = (CacheKey) o;
            List<? extends Item> items2 = cacheKey.items;
            if (items == null || items2 == null) {
                return false;
            }
            if (items2.size() != items.size()) {
                return false;
            }
            for (int i = 0; i < items2.size(); i++) {
                if (!items.get(i).getClass().equals(items2.get(i).getClass())) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            int hashCode = 1;
            for (Item e : items)
                hashCode = 31 * hashCode + (e == null ? 0 : e.getClass().hashCode());
            return hashCode;
        }
    }
}
