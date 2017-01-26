package mah.openapi.ui.layout;

import mah.command.Command;
import mah.command.CommandManager;
import mah.ui.event.EventHandler;
import mah.ui.pane.input.InputPane;
import mah.ui.pane.item.Item;
import mah.ui.pane.item.ItemSelectedEvent;
import mah.ui.support.swing.layout.ClassicItemListLayoutImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by zgq on 2017-01-08 15:11
 */
public class OpenClassicItemListLayoutImpl extends AbstractCommandLayout implements OpenClassicItemListLayout{

    private static final Logger LOG = LoggerFactory.getLogger(OpenClassicItemListLayoutImpl.class);

    public OpenClassicItemListLayoutImpl(Command command) {
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
    public void compareAndUpdateItems(final List<? extends Item> items, final String expect) {
        updateWholeLayout(() -> {
            final String actual = CommandManager.getInstance().getCurrentQuery();
            LOG.debug("expect {},actual:{}", expect, actual);
            if (actual!=null && actual.equals(expect)) {
                getLayout().updateItems(items);
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
