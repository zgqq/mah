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
@Deprecated
public abstract class AbstractInput2 implements Input{

    private final LinkedList<TextState> undoStack = new LinkedList<>();
    private final LinkedList<TextState> redoStack = new LinkedList<>();
    private boolean fireEvent=true;
    private boolean selectable=true;
    private TextState textState;


    protected boolean canBackward() {
        return getCaretPosition() > 0;
    }

    private void push() {
        pushToUndo(newTextState(getText(),getCaretPosition()));
    }

    private void pushToUndo(TextState textState) {
        if (undoStack.size() > 100) {
            undoStack.pollLast();
        }
        undoStack.push(textState);
    }


    protected boolean canFoward() {
        return getCaretPosition() < getText().length();
    }

    @Override
    public void beginningOfLine() {
        setCaretPosition(0);
    }

    @Override
    public void endOfLine() {
        setCaretPosition(getText().length());
    }

    @Override
    public void forwardChar() {
        if (getCaretPosition() < getText().length()) {
            setCaretPosition(getCaretPosition() + 1);
        }
    }

    public void backwardChar() {
        if (getCaretPosition() > 0) {
            setCaretPosition(getCaretPosition() - 1);
        }
    }

    public void deleteChar() {
        String text = getText();
        int caretPosition = getCaretPosition();
        if (caretPosition >= text.length()) {
            return;
        }
        push();
        String newTxt = "";
        if (caretPosition> 0) {
            String beforeStr = text.substring(0, caretPosition);
            String afterStr = "";
            if (caretPosition + 1 < text.length()) {
                afterStr = text.substring(caretPosition + 1, getText().length());
            }
            newTxt = beforeStr + afterStr;
        } else {
            if (text.length() > 1) {
                newTxt  = text.substring(1, text.length());
            } else {
                newTxt = "";
            }
        }
        setText(newTxt);
        setCaretPosition(caretPosition);
    }



    public void forwardWord() {
        if (!canFoward()) {
            return;
        }
        int caretPosition = forwardByWord();
        setCaretPosition(caretPosition);
    }

    protected int forwardByWord() {
        String text = getText();
        int caretPosition = getCaretPosition();
        boolean hasLetter = false;
        for (int i = caretPosition; i < text.length(); i++) {
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
        boolean hasLetter = false;
        for (int i = caretPosition - 1; i >= 0; i--) {
            char c = text.charAt(i);
            if (c == ' ' && i != caretPosition - 1) {
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
        if (!canFoward()) {
            return;
        }
        String text = getText();
        int origin = getCaretPosition();
        int forward = forwardByWord();
        if (forward > origin) {
            String before = "";
            String after = "";
            if (origin > 0) {
                before = text.substring(0, origin);
            }
            if (forward < text.length()) {
                after = text.substring(forward, text.length());
            }
            push();
            setText(before + after);
        }
        setCaretPosition(origin);
    }

    @Override
    public void killLine() {
        int caret = getCaretPosition();
        String text = getText();
        if (caret < text.length()) {
            push();
            if (caret > 0) {
                setText(text.substring(0, caret));
            } else {
                setText("");
            }
        }
        setCaretPosition(caret);
    }

    @Override
    public void backwardKillWord() {
        if (!canBackward()) {
            return;
        }
        push();
        String text = getText();
        int origin = getCaretPosition();
        int backwardWord = backwardWord2();
        if (backwardWord == origin) {
            return;
        }
        String before = "";
        String after = "";
        if (backwardWord > 0) {
            before = text.substring(0, backwardWord);
        }
        if (origin < getText().length()) {
            after = text.substring(origin, getText().length());
        }
        setText(before + after);
        setCaretPosition(backwardWord);
    }

    @Override
    public void backwardDeleteChar() {
        if (!canBackward()) {
            return;
        }
        push();
        String text = getText();
        int origin = getCaretPosition();
        String before = "";
        String after = "";
        if (origin - 1 > 0) {
            before = text.substring(0, origin - 1);
        }

        if (origin < text.length()) {
            after = text.substring(origin, text.length());
        }

        setText(before + after);
        setCaretPosition(origin - 1);
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

    protected boolean isSelectable() {
        return this.selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public void setTextState(TextState textState) {
        this.selectable = false;
        this.textState = textState;
        setText(textState.getText());
        setCaretPosition(textState.getPosition());
    }

    public TextState getTextState() {
        return textState;
    }

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
    public void setTextStateQuietly(TextState textState) {
        fireEvent = false;
        setTextState(textState);
        fireEvent = true;
    }

    public boolean isFireEvent() {
        return fireEvent;
    }
}

