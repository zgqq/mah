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
package mah.plugin.support.suggestion;

import mah.common.util.StringUtils;
import mah.event.EventHandler;
import mah.ui.input.InputTextChangedEvent;
import mah.ui.input.TextState;
import mah.ui.pane.input.InputPane;
import mah.ui.support.swing.pane.input.InputPaneImpl;
import mah.ui.util.UiUtils;

import javax.swing.*;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zgq on 2/13/17.
 */
public class TextChangedHandler implements EventHandler<InputTextChangedEvent> {
    private final CommandHistories commandHistories;
    private final ScheduledExecutorService scheduledExecutor;

    public TextChangedHandler(CommandHistories commandHistories) {
        this.commandHistories = commandHistories;
        this.scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    @Override
    public void handle(InputTextChangedEvent event) throws Exception {
        TextState newState = event.getNewState();
        final InputPane inputPane = UiUtils.getInputPane();
        final String input = newState.getText();
        this.commandHistories.addIfAbsent(input);
        scheduledExecutor.schedule(() -> {
                    String text = inputPane.getText();
                    if (StringUtils.isNotEmpty(text) && text.equals(input) && !text.endsWith(" ")) {
                        TextChangedHandler.this.commandHistories.access(input);
                    }
                },
                300, TimeUnit.MILLISECONDS);

        if (StringUtils.isNotEmpty(input)) {
            Optional<CommandHistories.Node> command = commandHistories.historyStartWith(input);
            if (command.isPresent()) {
                if (inputPane instanceof InputPaneImpl) {
                    final InputPaneImpl swingInputPane = (InputPaneImpl) inputPane;
                    swingInputPane.setTextProcessor(HintTextProcessor.INSTANCE);
                    int caretPosition = swingInputPane.getCaretPosition();
                    HintableDocument document = UiHelper.createHightlightDocument(command.get().getText(),
                            input.length());
                    swingInputPane.setDocument(document);
                    document.setSeparateIndex(input.length());
                    if (caretPosition > input.length()) {
                        caretPosition = input.length();
                    }
                    swingInputPane.setCaretPosition(caretPosition);
                }
            }
        }
    }
}
