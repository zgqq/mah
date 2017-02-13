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
import mah.event.EventHandler;
import mah.ui.key.KeyEvent;

/**
 * Created by zgq on 2017-01-09 13:13
 */
public abstract class LayoutWrapper implements Layout{
    private final Layout layout;

    protected LayoutWrapper(Layout layout) {
        this.layout = layout;
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
    public void setOnKeyPressed(EventHandler<? extends KeyEvent> keyPressedHandler) {
        layout.setOnKeyPressed(keyPressedHandler);
    }

    @Override
    public void setOnKeyReleased(EventHandler<? extends KeyEvent> keyReleasedHandler) {
        layout.setOnKeyReleased(keyReleasedHandler);
    }

    @Override
    public void init() {
        this.layout.init();
    }
    @Override
    public void onSetCurrentLayout() {
        layout.onSetCurrentLayout();
    }

    @Override
    public Mode getMode() {
        return layout.getMode();
    }

    @Override
    public void registerMode(Mode mode, ModeListener modeListener) {
        layout.registerMode(mode, modeListener);
    }

    public Layout getLayout() {
        return layout;
    }
}
