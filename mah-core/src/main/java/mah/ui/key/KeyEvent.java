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
package mah.ui.key;

/**
 * Created by zgq on 2017-01-09 09:46
 */
public class KeyEvent {
    private final int keyCode;
    private final java.awt.event.KeyEvent keyEvent;
    private KeystateManager keyState = KeystateManager.getInstance();
    private boolean modifierKey;

    public KeyEvent(java.awt.event.KeyEvent keyEvent) {
        this.keyEvent = keyEvent;
        this.keyCode = keyEvent.getKeyCode();
    }


    public int getKeyCode() {
        return keyCode;
    }

    public boolean altPressed() {
        return keyState.altPressed();
    }

    public boolean metaPressed() {
        return keyState.metaPressed();
    }

    public boolean ctrlPressed() {
        return keyState.ctrlPressed();
    }

    public boolean shiftPressed() {
        return keyEvent.isShiftDown();
//        return keyState.shiftPressed();
    }


    public boolean isModifierKey() {
        switch (keyCode) {
            case java.awt.event.KeyEvent.VK_CONTROL:
            case java.awt.event.KeyEvent.VK_ALT:
            case java.awt.event.KeyEvent.VK_META:
            case java.awt.event.KeyEvent.VK_SHIFT:
                return true;
            default:
                return false;
        }
    }
}


