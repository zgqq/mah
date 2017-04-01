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
package mah.ui.support.swing.layout;

import mah.mode.Mode;
import mah.mode.ModeManager;
import mah.ui.UiException;
import mah.event.EventHandler;
import mah.ui.input.InputConfirmedEvent;
import mah.ui.layout.ClassicItemListLayout;
import mah.ui.layout.ModeListener;
import mah.ui.pane.item.Item;
import mah.ui.pane.item.ItemListPane;
import mah.ui.pane.item.ItemMode;
import mah.ui.pane.item.ItemSelectedEvent;
import mah.ui.support.swing.pane.item.ItemListPaneFactory;
import mah.ui.support.swing.theme.SwingLayoutTheme;
import mah.ui.theme.LayoutTheme;
import mah.ui.theme.ThemeManager;
import mah.ui.theme.Themeable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zgq on 2017-01-08 13:51
 */
public class ClassicItemListLayoutImpl extends SwingAbstractClassicLayoutWrapper
        implements ClassicItemListLayout, SwingLayout, Themeable {
    private static final String NAME = "classic_item_list_layout";
    private final List<EventHandler<? extends ItemSelectedEvent>> itemSelectedEventHandlers = new ArrayList<>();
    private final List<EventHandler<? extends InputConfirmedEvent>> inputConfirmedHandlers = new ArrayList<>();
    private final ItemListPaneFactory factory;
    private ItemListPane itemListPane;
    private final List<ModeListener> modeListeners = new ArrayList<>();
    private Mode mode;

    public ClassicItemListLayoutImpl() {
        super(ClassicAbstractLayoutImpl.instance());
        this.factory = new ItemListPaneFactory();
    }

    private SwingLayoutTheme applyTheme() {
        LayoutTheme layoutTheme = ThemeManager.getInstance().getLayoutTheme(NAME);
        apply(layoutTheme);
        return (SwingLayoutTheme) layoutTheme;
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
    public void fireInputConfirmedEvent(InputConfirmedEvent event) {
        try {
            for (EventHandler inputConfirmedHandler : inputConfirmedHandlers) {
                inputConfirmedHandler.handle(event);

            }
        } catch (Exception e) {
            throw new UiException("Failed to handle event", e);
        }
    }

    @Override
    public void registerMode(Mode mode, ModeListener modeListener) {
        mode.addChild(ClassicItemLayoutMode.getAndRegisterMode());
        this.mode = mode;
        modeListeners.add(modeListener);
        ModeManager.getInstance().registerMode(mode);
    }

    @Override
    public void setOnInputConfirmed(EventHandler<? extends InputConfirmedEvent> eventHandler) {
        inputConfirmedHandlers.add(eventHandler);
    }

    @Override
    public void updateItems(List<? extends Item> items) {
        updateItems(items, true);
    }

    private void updateItems(List<? extends Item> items, boolean pending) {
        triggerMode();
        SwingLayoutTheme layoutTheme = applyTheme();
        this.itemListPane = factory.createItemListPane(items, itemSelectedEventHandlers, layoutTheme);
        getLayout().updatePane(this.itemListPane);
        if (items != null && items.size() > 0 && pending) {
            this.itemListPane.setPendingItemIndex(0);
        }
    }

    @Override
    public void updateItems(Item... items) {
        if (items == null) {
            throw new UiException("The items should not be null in layout " + getName());
        }
        applyTheme();
        updateItems((List<? extends Item>) Arrays.asList(items));
    }


    private void triggerMode() {
        ItemMode itemMode = ItemMode.getMode();
        itemMode.updateActionHandler(this);
        ClassicItemLayoutMode layoutMode = ClassicItemLayoutMode.triggerMode();
        layoutMode.updateActionHandler(this);
        if (mode != null) {
            LayoutUtils.triggerMode(mode, modeListeners);
        }
    }

    @Override
    public void clear() {
        getLayout().updatePane(null);
    }

    private void updateBottomPane() {
        getLayout().updatePane(itemListPane);
    }

    @Override
    public void refresh() {
        updateBottomPane();
    }

    @Override
    public void updateItem(Item item, int num) {
        if (this.itemListPane == null) {
            throw new UiException("Unable to update item");
        }
        triggerMode();
        this.itemListPane.updateItem(item, num);
        getLayout().updatePane(this.itemListPane);
    }

    @Override
    public void showFirstItem(Item item) {
        updateItems(Arrays.asList(item), false);
    }

    @Override
    public String getName() {
        return NAME;
    }

    private ItemListPane getItemListPane() {
        return itemListPane;
    }


    @Override
    public <T extends Item> T getItem(int index) {
        return itemListPane.getItem(index);
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
        if (itemListPane == null) {
            return 0;
        }
        return itemListPane.getItemCount();
    }

    @Override
    public void unpendingItemIndex(int pendingItemIndex) {
        getItemListPane().unpendingItemIndex(pendingItemIndex);
    }
}
