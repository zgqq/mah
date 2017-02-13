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
package mah.ui.support.swing.pane.input;

import mah.ui.input.TextState;
import mah.ui.support.swing.util.StringUtils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;

/**
 * Created by zgq on 2/13/17.
 */
public class StyledDocument extends DefaultStyledDocument {
    private static final ThreadLocal<TextState> OLD_STATE = new ThreadLocal<>();
    private static final int MAX_NUMBER_OF_CHARACTERS = 45;
    private final int maxNumberOfCharacters;
    private InputPaneImpl inputPane;

    public StyledDocument(InputPaneImpl inputPane) {
        this(MAX_NUMBER_OF_CHARACTERS);
        this.inputPane = inputPane;
    }

    public StyledDocument(int maxNumberOfCharacters) {
        this.maxNumberOfCharacters = maxNumberOfCharacters;
    }

    public static TextState getOldState() {
        return OLD_STATE.get();
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        setOldState();
        final String origin = getText(0, getLength());
        final int index = StringUtils.getLen(origin, str, maxNumberOfCharacters);
        if (index > 0) {
            super.insertString(offs, str.substring(0, index), a);
        }
    }

    @Override
    public void remove(int offs, int len) throws BadLocationException {
        setOldState();
        super.remove(offs, len);
    }

    public void setNotifyTextChanged(boolean notifyTextChanged) {
        inputPane.setNotifyTextChanged(notifyTextChanged);
    }

    public boolean isNotifyTextChanged() {
        return inputPane.isNotifyTextChanged();
    }

    protected int length() {
        return getLength();
    }

    private void setOldState() throws BadLocationException {
        TextState oldState = new TextState.Builder(getText(0, super.getLength()), inputPane.getCaretPosition()).build();
        OLD_STATE.set(oldState);
    }
}
