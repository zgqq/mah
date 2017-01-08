package mah.ui.pane.item;

import mah.ui.UIException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by zgq on 2017-01-08 15:02
 */
public abstract class ItemListPaneFactorySupport implements ItemListPaneFactory{

    private static final Map<CacheKey, ItemListPane> ITEMSPANE = new HashMap<>();
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

    @Override
    public ItemListPane createItemListPane(List<? extends Item> items) {
        if (items == null) {
            throw new UIException("items could not be null");
        }
        CacheKey cacheKey = new CacheKey(items);
        ItemListPane itemListPane;
        readLock.lock();
        try {
            itemListPane = ITEMSPANE.get(cacheKey);
            if (itemListPane != null) {
                itemListPane.reset(items);
                return itemListPane;
            }
        } finally {
            readLock.unlock();
        }

        writeLock.lock();
        try {
            itemListPane = createItemsPane(items);
            ITEMSPANE.put(cacheKey, itemListPane);
            return itemListPane;
        } finally {
            writeLock.unlock();
        }

    }

    protected abstract ItemListPane createItemsPane(List<? extends Item> items);

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
