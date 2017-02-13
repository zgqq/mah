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

import mah.ui.UiException;
import mah.event.EventHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-08 14:08
 */
public abstract class ItemListPaneSupport implements ItemListPane {
    private List<EventHandler<? extends ItemSelectedEvent>> itemSelectedHandlers = new ArrayList<>();
    private final List<ItemPane> itemPanes;
    private int pendingIndex = -1;

    public ItemListPaneSupport(List<? extends Item> items) {
        itemPanes = new ArrayList<>(items.size());
    }

    protected final void initialize(List<? extends Item> items) {
        if (items == null || items.size() < 0) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            ItemPane itemPane = createItemPane(item,i + 1);
            itemPanes.add(itemPane);
        }
    }


    protected abstract ItemPane createItemPane(Item item,int num);

    public void reset(List<? extends Item> items) {
        if (items.size() != itemPanes.size()) {
            throw new IllegalStateException("Unable to update ItemListPane " + itemPanes.size() + "," + items.size());
        }
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            itemPanes.get(i).reset(item,i + 1);
        }
        pendingIndex = -1;
        itemSelectedHandlers.clear();
    }

    @Override
    public void setOnItemSelected(EventHandler<? extends ItemSelectedEvent> eventHandler) {
        itemSelectedHandlers.add(eventHandler);
    }

    public final int getItemCount() {
        return itemPanes.size();
    }

    public List<ItemPane> getItemPanes() {
        return itemPanes;
    }


    @Override
    public void updateItem(Item item, int num) {
        if (num < 1 || num > itemPanes.size()) {
            throw new IllegalArgumentException("Unable to update item " + num
                    + ",num must be greater than 1 and less than " + (itemPanes.size() + 1));
        }
        itemPanes.get(num - 1).reset(item, num);
    }

    @Override
    public void setPendingItemIndex(int i) {
        itemPanes.get(i).pending();
        pendingIndex = i;
    }

    @Override
    public int getPendingItemIndex() {
        return pendingIndex;
    }

    @Override
    public boolean hasPendingItem() {
        return pendingIndex >= 0;
    }

    @Override
    public void selectItem(int index) {
        ItemSelectedEvent itemSelectedEvent = new ItemSelectedEvent(itemPanes.get(index).getItem());
        for (EventHandler itemSelectedHandler : itemSelectedHandlers) {
            try {
                itemSelectedHandler.handle(itemSelectedEvent);
            } catch (Exception e) {
                throw new UiException(e);
            }
        }
    }

    @Override
    public <T extends Item> T getPendingItem() {
        ItemPane itemPane = itemPanes.get(pendingIndex);
        return (T) itemPane.getItem();
    }

    @Override
    public void unpendingItemIndex(int pendingItemIndex) {
        itemPanes.get(pendingItemIndex).unpending();
    }
}
