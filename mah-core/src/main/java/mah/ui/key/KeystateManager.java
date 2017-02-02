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
 * Created by zgq on 2017-01-09 09:40
 */
public class KeystateManager {

    private boolean ctrl;
    private boolean alt;
    private boolean meta;
    private boolean shift;
    private long releaseCtrlTime;
    private long releaseAltTime;
    private long releaseMetaTime;
    private long releaseShiftTime;
    private long interval = 0;
    private long pressedKeyTime;
    private int key;
    private static final KeystateManager INSTANCE = new KeystateManager();

    public int getPressedKey() {
        long l = System.currentTimeMillis() - pressedKeyTime;
        if (l < 30) {
            return key;
        }
        return 0;
    }

    public boolean shiftPressed() {
        return isPressed(shift, releaseShiftTime);
    }

    public boolean ctrlPressed() {
        return isPressed(ctrl, releaseCtrlTime);
    }

    private boolean isPressed(boolean keyPressed, long releaseTime) {
        if (keyPressed || (System.currentTimeMillis() - releaseTime) < interval) {
            return true;
        }
        return false;
    }

    public boolean metaPressed() {
        return isPressed(meta, releaseMetaTime);
    }

    public boolean altPressed() {
        return isPressed(alt, releaseAltTime);
    }

    public void setPressedKey(int key) {
        pressedKeyTime = System.currentTimeMillis();
        this.key = key;
    }

    public void setCtrl(boolean pressed) {
        if (pressed == false) {
            releaseCtrlTime = System.currentTimeMillis();
        }
        this.ctrl = pressed;
    }

    public void setMeta(boolean pressed) {
        if (pressed == false) {
            releaseMetaTime = System.currentTimeMillis();
        }
        this.meta = pressed;
    }

    public void setAlt(boolean pressed) {
        if (pressed == false) {
            releaseAltTime = System.currentTimeMillis();
        }
        this.alt = pressed;
    }

    public void setShift(boolean pressed) {
        if (pressed == false) {
            releaseShiftTime = System.currentTimeMillis();
        }
        this.shift = pressed;
    }

    public void reset() {
        this.ctrl = false;
        this.alt = false;
        this.meta = false;
        this.shift = false;
    }

    public static KeystateManager getInstance() {
        return INSTANCE;
    }

}
