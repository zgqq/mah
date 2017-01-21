package mah.openapi.ui.layout;

import mah.command.Command;
import mah.ui.event.EventHandler;
import mah.ui.pane.input.InputPane;
import mah.ui.pane.item.Item;
import mah.ui.pane.item.ItemSelectedEvent;
import mah.ui.support.swing.layout.ClassicItemListLayoutImpl;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by zgq on 2017-01-08 15:11
 */
public class ClassicItemListLayout extends AbstractCommandLayout implements mah.ui.layout.ClassicItemListLayout {

    public ClassicItemListLayout(Command command) {
        super(new ClassicItemListLayoutImpl(),command);
    }

    private ClassicItemListLayoutImpl getLayout() {
        return (ClassicItemListLayoutImpl) layout();
    }

    @Override
    public <T extends Item> T getPendingItem() {
        return getValue(new Callable<T>() {
            @Override
            public T call() throws Exception {
                return getLayout().getPendingItem();
            }
        });
    }

    @Override
    public void updateItems(List<? extends Item> items) {
        updateWholeLayout(() -> {
            getLayout().updateItems(items);
        });
    }

    @Override
    public void unpendingItemIndex(int pendingItemIndex) {
        updatePartLayout(() -> {
            getLayout().unpendingItemIndex(pendingItemIndex);
        });
    }

    @Override
    public void selectItem(int index) {
        updatePartLayout(() -> getLayout().selectItem(index));
    }

    @Override
    public boolean hasPendingItem() {
        return getValue(() -> getLayout().hasPendingItem());
    }

    @Override
    public int getPendingItemIndex() {
        return getValue(()->getLayout().getPendingItemIndex());
    }

    @Override
    public void setPendingItemIndex(int index) {
        updatePartLayout(()->getLayout().setPendingItemIndex(index));
    }

    @Override
    public void setOnItemSelected(EventHandler<? extends ItemSelectedEvent> eventHandler) {
        runSafely(()->{getLayout().setOnItemSelected(eventHandler);});
    }

    @Override
    public void updateItem(Item item, int num) {
        updateWholeLayout(()->{
            getLayout().updateItem(item,num);
        });
    }

    @Override
    public int getItemCount() {
        return getValue(()->getLayout().getItemCount());
    }

    @Override
    public void clear() {
        updateWholeLayout(()->{
            getLayout().clear();
        });
    }

    @Override
    public void refresh() {
        updateWholeLayout(()->{
            getLayout().refresh();
        });
    }

    @Override
    public void updateItems(Item... items) {
        updateWholeLayout(() -> {
            getLayout().updateItems(items);
        });
    }

    @Override
    public InputPane getInputPane() {
        return getValue(()->getLayout().getInputPane());
    }
}
