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

import mah.event.EventHandler;
import mah.ui.input.CaretMotionEvent;
import mah.ui.pane.input.InputPane;
import mah.ui.support.swing.pane.input.InputPaneImpl;

import javax.swing.text.Document;

/**
 * Created by zgq on 2/19/17.
 */
public class CaretMotionHandler implements EventHandler<CaretMotionEvent> {
    private final CommandHistories commandHistories;

    public CaretMotionHandler(CommandHistories commandHistories) {
        this.commandHistories = commandHistories;
    }

    @Override
    public void handle(CaretMotionEvent event) throws Exception {
        InputPane inputPane = event.getInputPane();
        int newPosition = event.getNewPosition();
        int oldPosition = event.getOldPosition();
        if (newPosition == oldPosition) {
            return;
        }
        if (inputPane instanceof InputPaneImpl) {
            InputPaneImpl pane = (InputPaneImpl) inputPane;
            Document document = pane.getDocument();
            if (document instanceof HintableDocument) {
                HintableDocument hintableDocument = (HintableDocument) document;
                if (hintableDocument.replaceHint(newPosition)) {
                    String inputedText = hintableDocument.getInputedText();
                    commandHistories.increaseAccessCount(inputedText);
                }
            }
        }
    }
}
