/**
 * MIT License
 *
 * Copyright (c) 2017 zgqq
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package mah.ui.pane.item;

import mah.action.AbstractAction;
import mah.action.ActionEvent;
import mah.mode.AbstractMode;
import mah.mode.Mode;
import mah.mode.ModeManager;
import mah.ui.UiManager;
import mah.ui.input.InputMode;
import mah.ui.pane.Text;
import mah.ui.util.ClipboardUtils;
import mah.ui.window.WindowManager;

/**
 * Created by zgq on 2017-01-11 15:11
 */
public class ItemMode extends AbstractMode {

    private static final String NAME = "item_mode";
    private static final String PREVIOUS_ITEM = "PreviousItem";
    private static final String NEXT_ITEM = "NextItem";
    private static final String DEFAULT_SELECT_ITEM = "DefaultSelectItem";
    private static final String SELECT_ITEM = "SelectItem";
    private DefaultSelectItem defaultSelectItem;

    public ItemMode(Mode parent) {
        super(NAME, parent);
    }

    @Override
    public void init() {
        registerAction(new PreviousItem(PREVIOUS_ITEM));
        registerAction(new NextItem(NEXT_ITEM));
        this.defaultSelectItem = new DefaultSelectItem(DEFAULT_SELECT_ITEM);
        registerAction(this.defaultSelectItem);
        for (int i = 1; i < 10; i++) {
            String name = SELECT_ITEM + i;
            registerAction(new SelectItem(name, i - 1));
        }
        registerAction(new CopyContent("CopyContent"));
        registerAction(new CopyDescription("CopyDescription"));
    }

    public DefaultSelectItem getDefaultSelectItem() {
        return defaultSelectItem;
    }

    public static synchronized ItemMode triggerMode() {
        ItemMode itemMode = getMode();
        ModeManager.getInstance().triggerMode(itemMode);
        return itemMode;
    }

    public static synchronized ItemMode getMode() {
        return (ItemMode) ModeManager.getInstance().getOrRegisterMode(new ItemMode(InputMode.getAndRegisterMode()));
    }


    abstract static class ItemAction extends AbstractAction {

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


    public static class DefaultSelectItem extends ItemAction {

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
                UiManager.getInstance().runLater(() -> {
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
                UiManager.getInstance().runLater(() -> {
                    itemList.unpendingItemIndex(pendingItemIndex);
                    itemList.setPendingItemIndex(pendingItemIndex + 1);
                });
            }
        }
    }

    abstract static class CopyAction extends ItemAction {

        public CopyAction(String name) {
            super(name);
        }
    }

    static class CopyContent extends CopyAction {

        public CopyContent(String name) {
            super(name);
        }

        public void actionPerformed(ItemList layout) {
            FullItem item = layout.getPendingItem();
            Text content = item.getContent();
            ClipboardUtils.copy(content.getText());
            WindowManager.hideWindow();
        }
    }

    static class CopyDescription extends CopyAction {

        public CopyDescription(String name) {
            super(name);
        }

        public void actionPerformed(ItemList layout) {
            FullItem item = layout.getPendingItem();
            Text explains = item.getDescription();
            ClipboardUtils.copy(explains.getText());
            WindowManager.hideWindow();
        }
    }
}
