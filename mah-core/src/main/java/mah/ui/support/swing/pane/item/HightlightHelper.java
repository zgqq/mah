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
package mah.ui.support.swing.pane.item;

import mah.ui.UiException;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import java.util.List;

/**
 * Created by zgq on 2017-01-11 20:29
 */
public class HightlightHelper {

    public static DefaultStyledDocument createHightlightDocument(String content,
                                                                 List<Integer> highlightIndexs,
                                                                 Style matchedStyle,
                                                                 Style generalStyle) {
        DefaultStyledDocument defaultStyledDocument = new DefaultStyledDocument();
        int j = 0;
        try {
            for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i);
                if (j < highlightIndexs.size()) {
                    Integer integer = highlightIndexs.get(j);
                    if (integer == i) {
                        defaultStyledDocument.insertString(integer, String.valueOf(c), matchedStyle);
                        j++;
                        continue;
                    }
                }
                defaultStyledDocument.insertString(i, String.valueOf(c), generalStyle);
            }
        } catch (BadLocationException e) {
            throw new UiException(e);
        }
        return defaultStyledDocument;
    }
}
