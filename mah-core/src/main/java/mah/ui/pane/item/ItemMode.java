package mah.ui.pane.item;

import mah.action.AbstractAction;
import mah.action.ActionEvent;
import mah.mode.AbstractMode;
import mah.mode.Mode;
import mah.mode.ModeManager;
import mah.ui.UIManager;
import mah.ui.input.InputMode;

/**
 * Created by zgq on 2017-01-11 15:11
 */
public class ItemMode extends AbstractMode {

    private static final String NAME = "item_mode";
    private static final String PREVIOUS_ITEM = "PreviousItem";
    private static final String NEXT_ITEM = "NextItem";
    private static final String DEFAULT_SELECT_ITEM = "DefaultSelectItem";
    private static final String SELECT_ITEM = "SelectItem";

    public ItemMode( Mode parent) {
        super(NAME, parent);
    }

    @Override
    public void init() {
        registerAction(new PreviousItem(PREVIOUS_ITEM));
        registerAction(new NextItem(NEXT_ITEM));
        registerAction(new DefaultSelectItem(DEFAULT_SELECT_ITEM));
        for (int i = 1; i <= 10; i++) {
            String name = SELECT_ITEM + i;
            registerAction(new SelectItem(name, i));
        }
    }

    public static ItemMode triggerMode() {
        ItemMode itemMode = getAndRegisterMode();
        ModeManager.getInstance().triggerMode(itemMode);
        return itemMode;
    }

    public static ItemMode getAndRegisterMode() {
        return (ItemMode) ModeManager.getInstance().getOrRegisterMode(new ItemMode(InputMode.getAndRegisterMode()));
    }


    static abstract class ItemAction extends AbstractAction {

        public ItemAction(String name) {
            super(name, ItemList.class);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            actionPerformed((ItemList) actionEvent.getSource());
        }

        protected void actionPerformed(ItemList itemList) {

        }
    }

    static class SelectItem extends ItemAction {

        private int selectIndex;

        public SelectItem(String name, int selectIndex) {
            super(name);
            this.selectIndex = selectIndex;
        }

        @Override
        public void actionPerformed(ItemList itemList) {
            itemList.selectItem(selectIndex);
        }
    }


    static class DefaultSelectItem extends ItemAction {

        public DefaultSelectItem(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ItemList itemList) {
            if (itemList.hasPendingItem()) {
                itemList.selectItem(itemList.getPendingItemIndex());
            }
        }
    }

    static class PreviousItem extends ItemAction {
        public PreviousItem(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ItemList itemList) {
            if (itemList.hasPendingItem()) {
                int pendingItemIndex = itemList.getPendingItemIndex();
                if (pendingItemIndex <= 0) {
                    return;
                }
                UIManager.getInstance().runLater(() -> {
                    itemList.unpendingItemIndex(pendingItemIndex);
                    itemList.setPendingItemIndex(pendingItemIndex - 1);
                });
            }
        }
    }

    static class NextItem extends ItemAction {

        public NextItem(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ItemList itemList) {
            if (itemList.hasPendingItem()) {
                int pendingItemIndex = itemList.getPendingItemIndex();
                if (pendingItemIndex >= itemList.getItemCount() - 1) {
                    return;
                }
                UIManager.getInstance().runLater(() -> {
                    itemList.unpendingItemIndex(pendingItemIndex);
                    itemList.setPendingItemIndex(pendingItemIndex + 1);
                });
            }
        }
    }
}
