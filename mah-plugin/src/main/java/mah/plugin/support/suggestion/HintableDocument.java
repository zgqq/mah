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

import mah.ui.UiException;
import mah.ui.support.swing.pane.input.InputPaneImpl;
import mah.ui.support.swing.pane.input.StyledDocument;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 * Created by zgq on 2/17/17.
 */
public class HintableDocument extends StyledDocument {
    private int separateIndex = -1;

    public HintableDocument(InputPaneImpl inputPane) {
        super(inputPane);
    }

    public String getInputedText() throws BadLocationException {
        if (separateIndex == -1) {
            return getText(0, getLength());
        }
        return getText(0, separateIndex);
    }


    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        incrementSeparateIndex(str.length());
        super.insertString(offs, str, a);
    }

    public void remove(int offs, int len) throws BadLocationException {
        minusSeparateIndex(len);
        super.remove(offs, len);
    }

    public void setSeparateIndex(int index) {
        check(index);
        this.separateIndex = index;
    }

    public void removeHint() throws BadLocationException {
        if (separateIndex != -1) {
            super.remove(separateIndex, super.getLength() - separateIndex);
            separateIndex = -1;
        }
    }

    @Override
    protected int length() {
        if (checkHint()) {
            return separateIndex;
        }
        return getLength();
    }

    public boolean checkHint() {
        check();
        return separateIndex != -1;
    }

    private void check() {
        check(separateIndex);
    }

    private void check(int separeteIndex) {
        if (separeteIndex < 0 && separeteIndex != -1) {
            throw new IllegalStateException("Illegal index");
        }
    }

    private void incrementSeparateIndex(int len) throws BadLocationException {
        if (checkHint()) {
            removeHint();
            separateIndex = -1;
        }
    }

    private void minusSeparateIndex(int len) throws BadLocationException {
        if (checkHint()) {
            removeHint();
            separateIndex = -1;
        }
    }

    public boolean replaceHint(int newPosition) {
        if (checkHint()) {
            try {
                if (newPosition > separateIndex) {
                    boolean notifyTextChanged = isNotifyTextChanged();
                    setNotifyTextChanged(false);
                    String text = super.getText(separateIndex, newPosition - separateIndex);
                    super.remove(separateIndex, newPosition - separateIndex);
                    int insertIndex = this.separateIndex;
                    removeHint();
                    setNotifyTextChanged(notifyTextChanged);
                    super.insertString(insertIndex, text, UiHelper.inputedStyle);
                    return true;
                }
            } catch (Exception e) {
                throw new UiException(e);
            }
        }
        return false;
    }
}
