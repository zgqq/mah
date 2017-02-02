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
import mah.ui.key.KeyEvent;
import mah.ui.support.swing.layout.LayoutUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-09 12:11
 */
public abstract class LayoutSupport implements Layout {

    private List<EventHandler<? extends KeyEvent>> keyReleasedHandlers = new ArrayList<>();
    private List<EventHandler<? extends KeyEvent>> keyPressedHandlers = new ArrayList<>();
    private final List<ModeListener> modeListeners = new ArrayList<>();
    private Mode mode;

    @Override
    public void setOnKeyReleased(EventHandler<? extends KeyEvent> eventHandler) {
        keyReleasedHandlers.add(eventHandler);
    }

    @Override
    public void setOnKeyPressed(EventHandler<? extends KeyEvent> eventHandler) {
        keyPressedHandlers.add(eventHandler);
    }

    public List<EventHandler<? extends KeyEvent>> getKeyPressedHandlers() {
        return keyPressedHandlers;
    }

    public List<EventHandler<? extends KeyEvent>> getKeyReleasedHandlers() {
        return keyReleasedHandlers;
    }

    protected final void setMode(Mode mode) {
        this.mode = mode;
    }

    protected final void addModeListener(ModeListener modeListener) {
        modeListeners.add(modeListener);
    }

    protected final void triggerMode() {
        LayoutUtils.triggerMode(mode, modeListeners);
    }


}
