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
package mah.keybind.util;

import javax.swing.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-09 09:27
 */
public class SimpleParser {

    private static final int getModifer(char c) {
        switch (c) {
            case 'c':
            case 'C':
                return InputEvent.CTRL_MASK;
            case 's':
                return InputEvent.META_MASK;
            case 'S':
                return InputEvent.SHIFT_MASK;
            case 'm':
            case 'M':
                return InputEvent.ALT_MASK;
            default:
                break;
        }
        throw new RuntimeException("Could not translate key code");
    }

    public static List<KeyStroke> parse(String keys) {
        List<KeyStroke> keyStrokes = new ArrayList<>();
        for (String comp : keys.split(" ")) {
            int modifierMask = 0;
            int keycode;
            if (comp.contains("-")) {
                String[] arrs = comp.split("-");
                for (int i = 0; i < arrs.length -1; i++) {
                    modifierMask |= getModifer(arrs[i].charAt(0));
                }
                String cs= arrs[arrs.length - 1];
                keycode = getKeyCode(cs);
            } else {
                keycode = getKeyCode(comp);
            }
            keyStrokes.add(KeyStroke.getKeyStroke(keycode, modifierMask));
        }
        return keyStrokes;
    }

    private static int getKeyCode(String cs) {
        int keycode;
        if (cs.length() > 1) {
            if (cs.equalsIgnoreCase("enter")) {
                keycode = KeyEvent.VK_ENTER;
            } else if (cs.equalsIgnoreCase("space")) {
                keycode = KeyEvent.VK_SPACE;
            } else if (cs.equalsIgnoreCase("backspace")) {
                keycode = KeyEvent.VK_BACK_SPACE;
            } else {
                throw new RuntimeException("Not supported key sequence" + cs);
            }
        }else {
            keycode = SwingUtils.getKeyCode(cs.charAt(0));
        }
        return keycode;
    }

}
