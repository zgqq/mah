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
package mah.ui.support.swing.layout;

import mah.ui.UiException;
import mah.event.EventHandler;
import mah.ui.key.KeyEvent;
import mah.ui.layout.LayoutSupport;
import mah.ui.support.swing.theme.SwingLayoutTheme;
import mah.ui.theme.LayoutTheme;

import java.awt.*;
import java.awt.event.KeyAdapter;

/**
 * Created by zgq on 2017-01-09 12:17
 */
public abstract class SwingLayoutSupport extends LayoutSupport implements SwingLayout {

    private SwingLayoutTheme currentTheme;

    private static KeyEvent convertEvent(java.awt.event.KeyEvent event) {
        KeyEvent keyEvent = new KeyEvent(event);
        return keyEvent;
    }

    protected class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(java.awt.event.KeyEvent e) {
            KeyEvent keyEvent = convertEvent(e);
            for (EventHandler eventHandler : getKeyPressedHandlers()) {
                try {
                    eventHandler.handle(keyEvent);
                } catch (Exception e1) {
                    throw new UiException(e1);
                }
            }
        }

        @Override
        public void keyReleased(java.awt.event.KeyEvent e) {
            KeyEvent keyEvent = convertEvent(e);
            for (EventHandler eventHandler : getKeyReleasedHandlers()) {
                try {
                    eventHandler.handle(keyEvent);
                } catch (Exception e1) {
                    throw new UiException(e1);
                }
            }
        }
    }

    @Override
    public final void apply(LayoutTheme theme) {
        this.currentTheme = ((SwingLayoutTheme) theme);
        applyTheme(this.currentTheme);
    }

    protected abstract void applyTheme(SwingLayoutTheme layoutTheme);

    public SwingLayoutTheme getCurrentTheme() {
        return currentTheme;
    }

    protected Color getColorByProperty(String property) {
        return currentTheme.getColorByProperty(property);
    }
}
