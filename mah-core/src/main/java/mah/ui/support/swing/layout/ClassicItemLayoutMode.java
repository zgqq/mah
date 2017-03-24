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

import mah.action.AbstractAction;
import mah.action.ActionEvent;
import mah.mode.AbstractMode;
import mah.mode.Mode;
import mah.mode.ModeManager;
import mah.ui.input.InputConfirmedEvent;
import mah.ui.layout.ClassicItemListLayout;
import mah.ui.pane.item.ItemMode;

/**
 * Created by zgq on 17-3-11.
 */
public class ClassicItemLayoutMode extends AbstractMode{

    public ClassicItemLayoutMode(Mode child) {
        super("classic_item_layout_mode",child);
    }

    @Override
    public void init() {
        registerAction(new InputConfirmed("InputConfirmed"));
    }

    public static ClassicItemLayoutMode triggerMode() {
        ClassicItemLayoutMode layoutMode = getAndRegisterMode();
        ModeManager.getInstance().triggerMode(layoutMode);
        return layoutMode;
    }

    public static ClassicItemLayoutMode getAndRegisterMode() {
        return (ClassicItemLayoutMode) ModeManager.getInstance()
                .getOrRegisterMode(new ClassicItemLayoutMode(ItemMode.getMode()));
    }

    static class InputConfirmed extends AbstractAction<ClassicItemListLayout> {

        public InputConfirmed(String name) {
            super(name, ClassicItemListLayout.class);
        }

        @Override
        public void actionPerformed(ActionEvent<ClassicItemListLayout> actionEvent) throws Exception {
            ClassicItemListLayout source = actionEvent.getSource();
            InputConfirmedEvent event = new InputConfirmedEvent(source.getInputPane().getText());
            source.fireInputConfirmedEvent(event);
        }
    }
}
