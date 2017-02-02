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
import mah.ui.event.EventHandler;
import mah.ui.pane.input.InputPane;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-09 14:36
 */
public class InputPaneFactoryBean {

    private static final InputPaneFactoryBean INSTANCE = new InputPaneFactoryBean();
    private List<EventHandler<? extends InputTextChangedEvent>> inputTextChangedHandlers = new ArrayList<>();

    public static InputPaneFactoryBean getInstance() {
        return INSTANCE;
    }

    public void setOnInputTextChanged(EventHandler<? extends InputTextChangedEvent> handler) {
        inputTextChangedHandlers.add(handler);
    }

    private void updateActionHandler(InputPane inputPane) {
        if (inputPane instanceof ActionHandler) {
            ActionHandler actionHandler = (ActionHandler) inputPane;
            ModeManager.getInstance().registerOrUpdateMode(InputMode.getAndRegisterMode(), actionHandler);
        }
    }

    public void initBean(InputPane inputPane) {
        for (EventHandler<? extends InputTextChangedEvent> inputTextChangedHandler : inputTextChangedHandlers) {
            inputPane.setOnInputTextChanged(inputTextChangedHandler);
        }
        updateActionHandler(inputPane);
    }

}
