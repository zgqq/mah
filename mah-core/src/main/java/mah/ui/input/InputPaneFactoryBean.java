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
package mah.ui.input;

import mah.action.ActionHandler;
import mah.mode.ModeManager;
import mah.event.EventHandler;
import mah.ui.pane.input.InputPane;

import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Created by zgq on 2017-01-09 14:36
 */
public class InputPaneFactoryBean {
    private static final InputPaneFactoryBean INSTANCE = new InputPaneFactoryBean();
    private final List<WeakReference<InputPane>> inputPanes = new ArrayList<>();
    private List<EventHandler<? extends InputTextChangedEvent>> inputTextChangedHandlers = new ArrayList<>();
    private List<EventHandler<? extends CaretMotionEvent>> caretMotionHandlers = new ArrayList<>();
    private final Object lock = new Object();

    public static InputPaneFactoryBean getInstance() {
        return INSTANCE;
    }

    public void setOnCaretMotionChanged(EventHandler<? extends CaretMotionEvent> handler) {
        synchronized (lock) {
            caretMotionHandlers.add(handler);
            List<InputPane> inputPanes = updateReferences(null);
            for (InputPane inputPane : inputPanes) {
                inputPane.setOnCaretMotion(handler);
            }
        }
    }

    public void setOnInputTextChanged(EventHandler<? extends InputTextChangedEvent> handler) {
        synchronized (lock) {
            inputTextChangedHandlers.add(handler);
            List<InputPane> inputPanes = updateReferences(null);
            for (InputPane inputPane : inputPanes) {
                inputPane.setOnInputTextChanged(handler);
            }
        }
    }

    public void initBean(InputPane inputPane) {
        synchronized (lock) {
            for (EventHandler<? extends InputTextChangedEvent> inputTextChangedHandler : inputTextChangedHandlers) {
                inputPane.setOnInputTextChanged(inputTextChangedHandler);
            }
            for (EventHandler<? extends CaretMotionEvent> caretMotionHandler : caretMotionHandlers) {
                inputPane.setOnCaretMotion(caretMotionHandler);
            }
            updateReferences(inputPane);
        }
        updateActionHandler(inputPane);
    }

    private List<InputPane> updateReferences(InputPane inputPane) {
        Iterator<WeakReference<InputPane>> iterator = inputPanes.iterator();
        List<InputPane> validIPanes = new ArrayList<>();
        boolean exists = false;
        while (iterator.hasNext()) {
            WeakReference<InputPane> next = iterator.next();
            InputPane pane = next.get();
            if (pane == null) {
                iterator.remove();
            } else {
                if (pane.equals(inputPane)) {
                    exists = true;
                }
                validIPanes.add(pane);
            }
        }
        if (!exists) {
            inputPanes.add(new WeakReference<>(inputPane));
        }
        return validIPanes;
    }

    private void updateActionHandler(InputPane inputPane) {
        if (inputPane instanceof ActionHandler) {
            ActionHandler actionHandler = (ActionHandler) inputPane;
            ModeManager.getInstance().registerOrUpdateMode(InputMode.getAndRegisterMode(), actionHandler);
        }
    }
}
