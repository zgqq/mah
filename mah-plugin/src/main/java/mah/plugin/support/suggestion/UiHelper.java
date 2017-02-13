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
import mah.ui.util.UiUtils;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * Created by zgq on 2017-01-11 20:29
 */
public class UiHelper {

    public static Style inputedStyle;
    public static Style hintStyle;

    static {
        JTextPane text = new JTextPane();
        Color inputedColor = Color.decode("#ffffff");
        inputedStyle = text.addStyle("inputedStyle", null);
        StyleConstants.setForeground(inputedStyle, inputedColor);

        Color hintColor = Color.decode("#24292E");
        hintStyle = text.addStyle("hintStyle", null);
        StyleConstants.setForeground(hintStyle, hintColor);
    }

    public static HintableDocument createHightlightDocument(String content, int endInputedIndex) {
        return createHightlightDocument(content, endInputedIndex, inputedStyle, hintStyle);
    }

    public static HintableDocument createHightlightDocument(String content, int endInputedIndex,
                                                            Style inputedStyle,
                                                            Style hintStyle) {
        HintableDocument defaultStyledDocument = new HintableDocument((InputPaneImpl) UiUtils.getInputPane());
        try {
            for (int i = 0; i < endInputedIndex; i++) {
                defaultStyledDocument.insertString(i, String.valueOf(content.charAt(i)), inputedStyle);
            }
            for (int i = endInputedIndex; i < content.length(); i++) {
                defaultStyledDocument.insertString(i, String.valueOf(content.charAt(i)), hintStyle);
            }
        } catch (BadLocationException e) {
            throw new UiException(e);
        }
        return defaultStyledDocument;
    }
}
