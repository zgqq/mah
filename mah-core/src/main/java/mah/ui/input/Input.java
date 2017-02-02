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

/**
 * Created by zgq on 2017-01-09 10:35
 */
public interface Input {

    void setTextStateQuietly(TextState textState);

    void setTextState(TextState textState);

    void beginningOfLine();

    void endOfLine();

    void forwardChar();

    void backwardChar();

    void deleteChar();

    void forwardWord();

    void backwardWord();

    void killWord();

    void killWholeLine();

    void killLine();

    void setText(String text);

    void setCaretPosition(int position);

    void backwardKillWord();

    void backwardDeleteChar();

    String getText();

    void redo();

    void undo();

    void remove(int offset, int len);

    int getCaretPosition();

    void clear();

}
