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

import java.util.LinkedList;

/**
 * Created by zgq on 2017-01-09 14:03
 */
public abstract class AbstractInput implements Input {
    private final LinkedList<TextState> undoStack = new LinkedList<>();
    private final LinkedList<TextState> redoStack = new LinkedList<>();
    private TextState textState;

    protected boolean canBackward() {
        return getCaretPosition() > 0;
    }

    private void push() {
        pushToUndo(newTextState(getText(), getCaretPosition()));
    }

    private void pushToUndo(TextState textState) {
        if (undoStack.size() > 100) {
            undoStack.pollLast();
        }
        undoStack.push(textState);
    }

    protected boolean canMotionForward() {
        return getCaretPosition() < textLength();
    }

    protected boolean canActionForward() {
        return getCaretPosition() < textLength();
    }

    @Override
    public void beginningOfLine() {
        setCaretPosition(0);
    }

    @Override
    public void endOfLine() {
        setCaretPosition(textLength());
    }

    @Override
    public void forwardChar() {
        if (canMotionForward()) {
            setCaretPosition(getCaretPosition() + 1);
        }
    }

    public void backwardChar() {
        if (getCaretPosition() > 0) {
            setCaretPosition(getCaretPosition() - 1);
        }
    }

    public void deleteChar() {
        int caretPosition = getCaretPosition();
        if (caretPosition >= textLength()) {
            return;
        }
        push();
        remove(caretPosition, 1);
    }

    @Override
    public void forwardWord() {
        if (!canMotionForward()) {
            return;
        }
        int caretPosition = forwardByWord();
        setCaretPosition(caretPosition);
    }

    protected int forwardByWord() {
        String text = getText();
        int caretPosition = getCaretPosition();
        boolean hasLetter = false;
        for (int i = caretPosition; i < motionLength(); i++) {
            char c = text.charAt(i);
            if (hasLetter) {
                if (!isWordLetter(c)) {
                    break;
                }
            } else {
                if (isWordLetter(c)) {
                    hasLetter = true;
                }
            }
            caretPosition++;
        }
        return caretPosition;
    }

    protected boolean isWordLetter(char c) {
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z';
    }

    @Override
    public void backwardWord() {
        if (!canBackward()) {
            return;
        }
        int caretPosition = backwardWord2();
        setCaretPosition(caretPosition);
    }

    protected int backwardWord2() {
        String text = getText();
        int caretPosition = getCaretPosition();
        int caret = caretPosition;
        boolean hasLetter = false;
        for (int i = caret - 1; i >= 0; i--) {
            char c = text.charAt(i);
            if (c == ' ' && i != caret - 1) {
                break;
            }

            if (hasLetter) {
                if (!isWordLetter(c)) {
                    break;
                }
            } else {
                if (isWordLetter(c)) {
                    hasLetter = true;
                }
            }
            caretPosition--;
        }
        return caretPosition;
    }


    public void killWord() {
        if (!canActionForward()) {
            return;
        }
        int origin = getCaretPosition();
        int forward = forwardByWord();
        if (forward > origin) {
            remove(origin, forward - origin);
            push();
        }
    }

    @Override
    public void killWholeLine() {
        String text = getText();
        if (text != null && textLength() > 0) {
            push();
            remove(0, textLength());
        }
    }

    @Override
    public void killLine() {
        int caret = getCaretPosition();
        if (caret < textLength()) {
            push();
            remove(caret, textLength() - caret);
        }
    }

    @Override
    public void backwardKillWord() {
        if (!canBackward()) {
            return;
        }
        int origin = getCaretPosition();
        int backwardWord = backwardWord2();
        if (backwardWord == origin) {
            return;
        }
        push();
        remove(backwardWord, origin - backwardWord);
    }

    @Override
    public void backwardDeleteChar() {
        if (!canBackward()) {
            return;
        }
        push();
        int caret = getCaretPosition();
        remove(caret - 1, 1);
        setCaretPosition(caret - 1);
    }

    @Override
    public final void undo() {
        TextState textState = undoStack.pollFirst();
        if (textState != null) {
            pushToRedo();
            setText(textState.getText());
            setCaretPosition(textState.getPosition());
        }
    }

    @Override
    public final void redo() {
        TextState textState = redoStack.pollFirst();
        if (textState != null) {
            setText(textState.getText());
            setCaretPosition(textState.getPosition());
            pushToUndo(textState);
        }
    }

    @Override
    public void clear() {
        String text = getText();
        if (text != null && !text.isEmpty()) {
            remove(0, textLength());
        }
    }

    public void setTextState(TextState textState) {
        this.textState = textState;
        setText(textState.getText());
        setCaretPosition(textState.getPosition());
    }

    public TextState getTextState() {
        return textState;
    }

    protected abstract int motionLength();

    protected abstract int textLength();


    private TextState newTextState(String text, int position) {
        return new TextState.Builder(text, position).build();
    }

    private void pushToRedo() {
        pushToRedo(newTextState(getText(), getCaretPosition()));
    }

    private void pushToRedo(TextState textState) {
        if (redoStack.size() > 100) {
            redoStack.pollLast();
        }
        redoStack.push(textState);
    }
}

