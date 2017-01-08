package mah.ui.layout;

import mah.ui.pane.item.Item;

import java.util.List;

/**
 * Created by zgq on 2017-01-08 15:10
 */
public class ClassicItemListLayoutWrapper implements ClassicItemListLayout{

    private final ClassicItemListLayout layout;

    public ClassicItemListLayoutWrapper(ClassicItemListLayout layout) {
        this.layout = layout;
    }

    @Override
    public void updateItems(List<? extends Item> items) {
        this.layout.updateItems(items);
    }

    public ClassicItemListLayout getLayout() {
        return layout;
    }

    @Override
    public String getName() {
        return layout.getName();
    }
}
