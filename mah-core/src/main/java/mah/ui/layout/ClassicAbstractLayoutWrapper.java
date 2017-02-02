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

import mah.ui.event.EventHandler;
import mah.ui.key.KeyEvent;
import mah.ui.pane.Pane;
import mah.ui.pane.input.InputPane;

/**
 * Created by zgq on 2017-01-08 12:24
 */
public abstract class ClassicAbstractLayoutWrapper extends LayoutWrapper implements ClassicAbstractLayout {

    private final ClassicAbstractLayout layout;

    public ClassicAbstractLayoutWrapper(ClassicAbstractLayout layout) {
        super(layout);
        this.layout = layout;
    }

    @Override
    public void updatePane(Pane pane) {
        layout.updatePane(pane);
    }

    @Override
    public void setDefaultMode() {
        layout.setDefaultMode();
    }

    public ClassicAbstractLayout getLayout() {
        return layout;
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
    public InputPane getInputPane() {
        return layout.getInputPane();
    }
}
