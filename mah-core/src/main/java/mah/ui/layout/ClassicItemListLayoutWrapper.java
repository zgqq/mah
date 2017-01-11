package mah.ui.layout;

import mah.ui.event.EventHandler;
import mah.ui.pane.input.InputPane;
import mah.ui.pane.item.Item;
import mah.ui.pane.item.ItemSelectedEvent;

import java.util.List;

/**
 * Created by zgq on 2017-01-08 15:10
 */
public abstract class ClassicItemListLayoutWrapper extends LayoutWrapper implements ClassicItemListLayout{

    private final ClassicItemListLayout layout;

    public ClassicItemListLayoutWrapper(ClassicItemListLayout layout) {
        super(layout);
        this.layout = layout;
    }

    @Override
    public void updateItems(List<? extends Item> items) {
        this.layout.updateItems(items);
    }

    @Override
    public void updateItems(Item... items) {
        this.layout.updateItems(items);
    }

    public ClassicItemListLayout getLayout() {
        return layout;
    }

    @Override
    public String getName() {
        return layout.getName();
    }

    @Override
    public void setDefaultMode() {
        layout.setDefaultMode();
    }

    @Override
    public InputPane getInputPane() {
        return layout.getInputPane();
    }

    @Override
    public void setOnItemSelected(EventHandler<? extends ItemSelectedEvent> eventHandler) {
        layout.setOnItemSelected(eventHandler);
    }

    @Override
    public void updateItem(Item item, int num) {
        layout.updateItem(item, num);
    }
}
