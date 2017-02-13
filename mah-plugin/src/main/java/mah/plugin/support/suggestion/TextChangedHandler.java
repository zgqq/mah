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
import mah.ui.UiManager;
import mah.ui.input.CaretMotionEvent;
import mah.ui.support.swing.pane.input.DocumentProvider;
import mah.ui.input.InputTextChangedEvent;
import mah.ui.input.TextState;
import mah.ui.pane.input.InputPane;
import mah.ui.support.swing.pane.input.InputPaneImpl;
import mah.ui.util.UiUtils;

import javax.swing.*;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.util.Optional;

/**
 * Created by zgq on 2/13/17.
 */
public class TextChangedHandler implements EventHandler<InputTextChangedEvent> {
    private final CommandHistories commandHistories;

    public TextChangedHandler(CommandHistories commandHistories) {
        this.commandHistories = commandHistories;
    }

    @Override
    public void handle(InputTextChangedEvent event) throws Exception {
        TextState newState = event.getNewState();
        final InputPane inputPane = UiUtils.getInputPane();
        final String input = newState.getText();
        if (StringUtils.isNotEmpty(input)) {
            Optional<CommandHistories.Node> command = commandHistories.historyStartWith(input);
            if (command.isPresent()) {
                if (inputPane instanceof InputPaneImpl) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
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
                    });
                }
            }
        }
    }
}
