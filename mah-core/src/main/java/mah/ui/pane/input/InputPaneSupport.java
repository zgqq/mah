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
package mah.ui.pane.input;

import mah.action.ActionHandler;
import mah.event.EventHandler;
import mah.ui.input.AbstractInput;
import mah.ui.input.CaretMotionEvent;
import mah.ui.input.InputTextChangedEvent;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by zgq on 2017-01-09 15:30
 */
public abstract class InputPaneSupport extends AbstractInput implements InputPane,ActionHandler {
    private final List<EventHandler<? extends InputTextChangedEvent>>
            inputTextChangedHandlers = new CopyOnWriteArrayList<>();
    private final List<EventHandler<? extends CaretMotionEvent>> caretMotionHandlers = new CopyOnWriteArrayList<>();

    @Override
    public void setOnInputTextChanged(EventHandler<? extends InputTextChangedEvent> inputTextChangedHandler) {
        inputTextChangedHandlers.add(inputTextChangedHandler);
    }

    public List<EventHandler<? extends InputTextChangedEvent>> getInputTextChangedHandlers() {
        return inputTextChangedHandlers;
    }

    @Override
    public void setOnCaretMotion(EventHandler<? extends CaretMotionEvent> caretMotionHandler) {
        caretMotionHandlers.add(caretMotionHandler);
    }

    public List<EventHandler<? extends CaretMotionEvent>> getCaretMotionHandlers() {
        return caretMotionHandlers;
    }
}
