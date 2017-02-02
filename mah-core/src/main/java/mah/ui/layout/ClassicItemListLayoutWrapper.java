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
package mah.ui.layout;

import mah.mode.Mode;
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

    @Override
    public boolean hasPendingItem() {
        return layout.hasPendingItem();
    }

    @Override
    public int getPendingItemIndex() {
        return layout.getPendingItemIndex();
    }

    @Override
    public void setPendingItemIndex(int i) {
        layout.setPendingItemIndex(i);
    }

    @Override
    public int getItemCount() {
        return layout.getItemCount();
    }

    @Override
    public void selectItem(int index) {
        layout.selectItem(index);
    }

    @Override
    public void registerMode(Mode mode, ModeListener modeListener) {
        layout.registerMode(mode, modeListener);
    }

    @Override
    public Mode getMode() {
        return layout.getMode();
    }


    @Override
    public <T extends Item> T getPendingItem() {
        return layout.getPendingItem();
    }
}
