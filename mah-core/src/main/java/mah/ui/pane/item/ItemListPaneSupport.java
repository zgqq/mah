package mah.ui.pane.item;

import mah.ui.theme.LayoutTheme;
import mah.ui.theme.Themeable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-08 14:08
 */
public abstract class ItemListPaneSupport implements ItemListPane,Themeable{

    private final List<ItemPane> itemPanes;

    public ItemListPaneSupport(List<? extends Item> items) {
        itemPanes = new ArrayList<>(items.size());
        init(items);
    }

    protected void init(List<? extends Item> items) {
        if (items == null || items.size() < 0) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            item.setNum(i + 1);
            ItemPane itemPane = createItemPane(items.get(i));
            itemPanes.add(itemPane);
        }
        computerSize();
    }

    protected abstract void computerSize();

    protected abstract ItemPane createItemPane(Item item);

    public void reset(List<? extends Item> items) {
        if (items.size() != itemPanes.size()) {
            throw new IllegalStateException("Unable to update ItemListPane "+itemPanes.size()+","+items.size());
        }
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            item.setNum(i + 1);
            itemPanes.get(i).reset(item);
        }
        computerSize();
    }

    protected final int getItemCount() {
        return itemPanes.size();
    }

    @Override
    public void apply(LayoutTheme theme) {
        for (ItemPane itemPane : itemPanes) {
            if (itemPane instanceof Themeable) {
                Themeable themeable = (Themeable) itemPane;
                themeable.apply(theme);
            }
        }
    }
}
