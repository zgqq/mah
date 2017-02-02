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
package mah.openapi.ui.layout;

import mah.command.Command;
import mah.ui.UIException;
import mah.ui.layout.ClassicPostLayout;

import javax.swing.*;

/**
 * Created by zgq on 2017-01-09 17:21
 */
public class SwingLayoutFactory implements LayoutFactory {

    private final Command command;

    public SwingLayoutFactory(Command command) {
        this.command = command;
    }

    private interface Creator {
        void create();
    }

    private OpenClassicItemListLayoutImpl classicItemListLayout;
    private ClassicPostLayout classicPostLayout;

    private void createLayout(Creator creator) {
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                creator.create();
            } else {
                SwingUtilities.invokeAndWait(() -> {
                    creator.create();
                });
            }
        } catch (Exception e) {
            throw new UIException(e);
        }
    }

    @Override
    public OpenClassicItemListLayout createClassicItemListLayout() {
        if (classicItemListLayout != null) {
            return classicItemListLayout;
        }
        createLayout(() -> {
            classicItemListLayout = new OpenClassicItemListLayoutImpl(command);
        });
        return classicItemListLayout;
    }

    @Override
    public ClassicPostLayout createClassicPostLayout() {
        if (classicPostLayout != null) {
            return classicPostLayout;
        }
        createLayout(() -> {
            classicPostLayout = new mah.openapi.ui.layout.ClassicPostLayout(command);
        });
        return classicPostLayout;
    }
}
