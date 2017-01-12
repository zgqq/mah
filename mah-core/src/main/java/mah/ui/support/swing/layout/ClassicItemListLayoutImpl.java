package mah.ui.support.swing.layout;

import mah.mode.Mode;
import mah.mode.ModeManager;
import mah.ui.UIException;
import mah.ui.event.EventHandler;
import mah.ui.layout.ClassicItemListLayout;
import mah.ui.layout.ModeEvent;
import mah.ui.layout.ModeListener;
import mah.ui.pane.item.Item;
import mah.ui.pane.item.ItemListPane;
import mah.ui.pane.item.ItemMode;
import mah.ui.pane.item.ItemSelectedEvent;
import mah.ui.support.swing.pane.item.ItemListPaneFactoryImpl;
import mah.ui.theme.LayoutTheme;
import mah.ui.theme.ThemeManager;
import mah.ui.theme.Themeable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zgq on 2017-01-08 13:51
 */
public class ClassicItemListLayoutImpl extends SwingAbstractClassicLayoutWrapper implements ClassicItemListLayout, SwingLayout, Themeable {
    private static final String NAME = "classic_item_list_layout";
    private List<EventHandler<? extends ItemSelectedEvent>> itemSelectedEventHandlers = new ArrayList<>();
    private final ItemListPaneFactoryImpl factory;
    private ItemListPane itemListPane;
    private List<ModeListener> modeListeners = new ArrayList<>();
    private Mode mode;

    public ClassicItemListLayoutImpl() {
        super(ClassicAbstractLayoutImpl.instance());
        this.factory = new ItemListPaneFactoryImpl();
    }

    private void applyTheme() {
        LayoutTheme layoutTheme = ThemeManager.getInstance().getLayoutTheme(NAME);
        apply(layoutTheme);
    }

    @Override
    public void init() {
        super.init();
        applyTheme();
    }

    @Override
    public Mode getMode() {
        return mode;
    }

    @Override
    public void registerMode(Mode mode, ModeListener modeListener) {
        mode.setParent(ItemMode.getAndRegisterMode());
        this.mode = mode;
        modeListeners.add(modeListener);
        ModeManager.getInstance().registerMode(mode);
    }

    @Override
    public void updateItems(List<? extends Item> items) {
        triggerMode();
        applyTheme();
        this.itemListPane = factory.createItemListPane(items, itemSelectedEventHandlers);
        getLayout().updatePane(this.itemListPane);
        if (items != null && items.size() > 0) {
            this.itemListPane.setPendingItemIndex(0);
        }
    }

    private void triggerMode() {
        ItemMode itemMode = ItemMode.triggerMode();
        itemMode.updateActionHandler(this);
        if (mode != null) {
            mode.trigger();
            ModeEvent modeEvent = new ModeEvent();
            modeEvent.setMode(mode);
            for (ModeListener modeListener : modeListeners) {
                modeListener.modeTriggered(modeEvent);
            }
        }
    }

    @Override
    public void updateItems(Item... items) {
        if (items == null) {
            throw new UIException("The items should not be null in layout " + getName());
        }
        applyTheme();
        updateItems(Arrays.asList(items));
    }

    @Override
    public void clear() {
        getLayout().updatePane(null);
    }

    @Override
    public void updateItem(Item item, int num) {
        if (this.itemListPane == null) {
            throw new UIException("Unable to update item");
        }
        triggerMode();
        this.itemListPane.updateItem(item, num);
    }

    @Override
    public String getName() {
        return NAME;
    }

    private ItemListPane getItemListPane() {
        return itemListPane;
    }


    @Override
    public <T extends Item> T getPendingItem() {
        return itemListPane.getPendingItem();
    }

    @Override
    public void selectItem(int index) {
        getItemListPane().selectItem(index);
    }

    @Override
    public boolean hasPendingItem() {
        return itemListPane.hasPendingItem();
    }

    @Override
    public int getPendingItemIndex() {
        return itemListPane.getPendingItemIndex();
    }

    @Override
    public void setPendingItemIndex(int i) {
        itemListPane.setPendingItemIndex(i);
    }

    @Override
    public void setOnItemSelected(EventHandler<? extends ItemSelectedEvent> handler) {
        itemSelectedEventHandlers.add(handler);
    }

    @Override
    public int getItemCount() {
        return itemListPane.getItemCount();
    }

    @Override
    public void unpendingItemIndex(int pendingItemIndex) {
        getItemListPane().unpendingItemIndex(pendingItemIndex);
    }


}
