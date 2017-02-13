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

import mah.ui.support.swing.theme.SwingLayoutTheme;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import java.awt.*;

/**
 * Created by zgq on 2017-01-14 13:55
 */
public final class ItemPaneTheme {
    private final SwingLayoutTheme theme;
    private JTextPane pane;
    private Style matchedTextStyle;
    private Style normalTextStyle;

    public ItemPaneTheme(SwingLayoutTheme theme) {
        this.theme = theme;
        pane = new JTextPane();
    }


    public Style getHighlightStyle() {
        if (matchedTextStyle == null) {
            Color highlightForegroundColor = Color.decode(theme.findProperty("text-highlight-foreground-color"));
            Color highlightBackgroundColor = Color.decode(theme.findProperty("text-highlight-background-color"));
            this.matchedTextStyle = pane.addStyle("highlight", null);
            StyleConstants.setForeground(this.matchedTextStyle, highlightForegroundColor);
            StyleConstants.setBackground(this.matchedTextStyle, highlightBackgroundColor);
            StyleConstants.setFontSize(this.matchedTextStyle, 14);
        }
        return matchedTextStyle;
    }

    public Style getNormalTextStyle() {
        if (normalTextStyle == null) {
            Color foregroundColor = Color.decode(theme.findProperty("text-foreground-color"));
            Color backgroundColor = Color.decode(theme.findProperty("text-background-color"));
            this.normalTextStyle = pane.addStyle("normalText", null);
            StyleConstants.setForeground(this.normalTextStyle, foregroundColor);
            StyleConstants.setBackground(this.normalTextStyle, backgroundColor);
            StyleConstants.setFontSize(this.normalTextStyle, 14);
            return this.normalTextStyle;
        }
        return normalTextStyle;
    }


    public Color getPendingBackgroundColor() {
        String property = theme.findProperty("pending-background-color");
        Color color = Color.decode(property);
        return color;
    }

    public Color getBackgroundColor() {
        String backgroundColor = theme.findProperty("background-color");
        return Color.decode(backgroundColor);
    }

    public Color getNumColor() {
        String numFontColor = theme.findProperty("num-font-color");
        return Color.decode(numFontColor);
    }

    public Color getTextForegroundColor() {
        String textForegroundColor = theme.findProperty("text-foreground-color");
        return Color.decode(textForegroundColor);
    }

    public Color getTextBackgroundColor() {
        String textBackgroundColor = theme.findProperty("text-background-color");
        return Color.decode(textBackgroundColor);
    }

}

