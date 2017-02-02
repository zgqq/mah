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
package mah.keybind.listener;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.awt.event.KeyEvent;

/**
 * Adapter to convert NativeKeyEvents to Java KeyEvents.
 * The methods are empty so the super call is obsolete.
 *
 * @since 2.1
 */
public abstract class SwingKeyAdapter implements NativeKeyListener {


    protected int getJavaKeyCode(NativeKeyEvent nativeEvent) {
        int keyCode = KeyEvent.VK_UNDEFINED;
        switch (nativeEvent.getKeyCode()) {
            case NativeKeyEvent.VC_ESCAPE:
                keyCode = KeyEvent.VK_ESCAPE;
                break;

            // Begin Function Keys
            case NativeKeyEvent.VC_F1:
                keyCode = KeyEvent.VK_F1;
                break;

            case NativeKeyEvent.VC_F2:
                keyCode = KeyEvent.VK_F2;
                break;

            case NativeKeyEvent.VC_F3:
                keyCode = KeyEvent.VK_F3;
                break;

            case NativeKeyEvent.VC_F4:
                keyCode = KeyEvent.VK_F4;
                break;

            case NativeKeyEvent.VC_F5:
                keyCode = KeyEvent.VK_F5;
                break;

            case NativeKeyEvent.VC_F6:
                keyCode = KeyEvent.VK_F6;
                break;

            case NativeKeyEvent.VC_F7:
                keyCode = KeyEvent.VK_F7;
                break;

            case NativeKeyEvent.VC_F8:
                keyCode = KeyEvent.VK_F8;
                break;

            case NativeKeyEvent.VC_F9:
                keyCode = KeyEvent.VK_F9;
                break;

            case NativeKeyEvent.VC_F10:
                keyCode = KeyEvent.VK_F10;
                break;

            case NativeKeyEvent.VC_F11:
                keyCode = KeyEvent.VK_F11;
                break;

            case NativeKeyEvent.VC_F12:
                keyCode = KeyEvent.VK_F12;
                break;

            case NativeKeyEvent.VC_F13:
                keyCode = KeyEvent.VK_F13;
                break;

            case NativeKeyEvent.VC_F14:
                keyCode = KeyEvent.VK_F14;
                break;

            case NativeKeyEvent.VC_F15:
                keyCode = KeyEvent.VK_F15;
                break;

            case NativeKeyEvent.VC_F16:
                keyCode = KeyEvent.VK_F16;
                break;

            case NativeKeyEvent.VC_F17:
                keyCode = KeyEvent.VK_F17;
                break;

            case NativeKeyEvent.VC_F18:
                keyCode = KeyEvent.VK_F18;
                break;

            case NativeKeyEvent.VC_F19:
                keyCode = KeyEvent.VK_F19;
                break;
            case NativeKeyEvent.VC_F20:
                keyCode = KeyEvent.VK_F20;
                break;

            case NativeKeyEvent.VC_F21:
                keyCode = KeyEvent.VK_F21;
                break;

            case NativeKeyEvent.VC_F22:
                keyCode = KeyEvent.VK_F22;
                break;

            case NativeKeyEvent.VC_F23:
                keyCode = KeyEvent.VK_F23;
                break;

            case NativeKeyEvent.VC_F24:
                keyCode = KeyEvent.VK_F24;
                break;
            // End Function Keys


            // Begin Alphanumeric Zone
            case NativeKeyEvent.VC_BACKQUOTE:
                keyCode = KeyEvent.VK_BACK_QUOTE;
                break;

            case NativeKeyEvent.VC_1:
                keyCode = KeyEvent.VK_1;
                break;

            case NativeKeyEvent.VC_2:
                keyCode = KeyEvent.VK_2;
                break;

            case NativeKeyEvent.VC_3:
                keyCode = KeyEvent.VK_3;
                break;

            case NativeKeyEvent.VC_4:
                keyCode = KeyEvent.VK_4;
                break;

            case NativeKeyEvent.VC_5:
                keyCode = KeyEvent.VK_5;
                break;

            case NativeKeyEvent.VC_6:
                keyCode = KeyEvent.VK_6;
                break;

            case NativeKeyEvent.VC_7:
                keyCode = KeyEvent.VK_7;
                break;

            case NativeKeyEvent.VC_8:
                keyCode = KeyEvent.VK_8;
                break;

            case NativeKeyEvent.VC_9:
                keyCode = KeyEvent.VK_9;
                break;

            case NativeKeyEvent.VC_0:
                keyCode = KeyEvent.VK_0;
                break;


            case NativeKeyEvent.VC_MINUS:
                keyCode = KeyEvent.VK_MINUS;
                break;

            case NativeKeyEvent.VC_EQUALS:
                keyCode = KeyEvent.VK_EQUALS;
                break;

            case NativeKeyEvent.VC_BACKSPACE:
                keyCode = KeyEvent.VK_BACK_SPACE;
                break;


            case NativeKeyEvent.VC_TAB:
                keyCode = KeyEvent.VK_TAB;
                break;

            case NativeKeyEvent.VC_CAPS_LOCK:
                keyCode = KeyEvent.VK_CAPS_LOCK;
                break;


            case NativeKeyEvent.VC_A:
                keyCode = KeyEvent.VK_A;
                break;

            case NativeKeyEvent.VC_B:
                keyCode = KeyEvent.VK_B;
                break;

            case NativeKeyEvent.VC_C:
                keyCode = KeyEvent.VK_C;
                break;

            case NativeKeyEvent.VC_D:
                keyCode = KeyEvent.VK_D;
                break;

            case NativeKeyEvent.VC_E:
                keyCode = KeyEvent.VK_E;
                break;

            case NativeKeyEvent.VC_F:
                keyCode = KeyEvent.VK_F;
                break;

            case NativeKeyEvent.VC_G:
                keyCode = KeyEvent.VK_G;
                break;

            case NativeKeyEvent.VC_H:
                keyCode = KeyEvent.VK_H;
                break;

            case NativeKeyEvent.VC_I:
                keyCode = KeyEvent.VK_I;
                break;

            case NativeKeyEvent.VC_J:
                keyCode = KeyEvent.VK_J;
                break;

            case NativeKeyEvent.VC_K:
                keyCode = KeyEvent.VK_K;
                break;

            case NativeKeyEvent.VC_L:
                keyCode = KeyEvent.VK_L;
                break;

            case NativeKeyEvent.VC_M:
                keyCode = KeyEvent.VK_M;
                break;

            case NativeKeyEvent.VC_N:
                keyCode = KeyEvent.VK_N;
                break;

            case NativeKeyEvent.VC_O:
                keyCode = KeyEvent.VK_O;
                break;

            case NativeKeyEvent.VC_P:
                keyCode = KeyEvent.VK_P;
                break;

            case NativeKeyEvent.VC_Q:
                keyCode = KeyEvent.VK_Q;
                break;

            case NativeKeyEvent.VC_R:
                keyCode = KeyEvent.VK_R;
                break;

            case NativeKeyEvent.VC_S:
                keyCode = KeyEvent.VK_S;
                break;

            case NativeKeyEvent.VC_T:
                keyCode = KeyEvent.VK_T;
                break;

            case NativeKeyEvent.VC_U:
                keyCode = KeyEvent.VK_U;
                break;

            case NativeKeyEvent.VC_V:
                keyCode = KeyEvent.VK_V;
                break;

            case NativeKeyEvent.VC_W:
                keyCode = KeyEvent.VK_W;
                break;

            case NativeKeyEvent.VC_X:
                keyCode = KeyEvent.VK_X;
                break;

            case NativeKeyEvent.VC_Y:
                keyCode = KeyEvent.VK_Y;
                break;

            case NativeKeyEvent.VC_Z:
                keyCode = KeyEvent.VK_Z;
                break;


            case NativeKeyEvent.VC_OPEN_BRACKET:
                keyCode = KeyEvent.VK_OPEN_BRACKET;
                break;

            case NativeKeyEvent.VC_CLOSE_BRACKET:
                keyCode = KeyEvent.VK_CLOSE_BRACKET;
                break;

            case NativeKeyEvent.VC_BACK_SLASH:
                keyCode = KeyEvent.VK_BACK_SLASH;
                break;


            case NativeKeyEvent.VC_SEMICOLON:
                keyCode = KeyEvent.VK_SEMICOLON;
                break;

            case NativeKeyEvent.VC_QUOTE:
                keyCode = KeyEvent.VK_QUOTE;
                break;

            case NativeKeyEvent.VC_ENTER:
                keyCode = KeyEvent.VK_ENTER;
                break;


            case NativeKeyEvent.VC_COMMA:
                keyCode = KeyEvent.VK_COMMA;
                break;

            case NativeKeyEvent.VC_PERIOD:
                keyCode = KeyEvent.VK_PERIOD;
                break;

            case NativeKeyEvent.VC_SLASH:
                keyCode = KeyEvent.VK_SLASH;
                break;

            case NativeKeyEvent.VC_SPACE:
                keyCode = KeyEvent.VK_SPACE;
                break;
            // End Alphanumeric Zone


            case NativeKeyEvent.VC_PRINTSCREEN:
                keyCode = KeyEvent.VK_PRINTSCREEN;
                break;

            case NativeKeyEvent.VC_SCROLL_LOCK:
                keyCode = KeyEvent.VK_SCROLL_LOCK;
                break;

            case NativeKeyEvent.VC_PAUSE:
                keyCode = KeyEvent.VK_PAUSE;
                break;


            // Begin Edit Key Zone
            case NativeKeyEvent.VC_INSERT:
                keyCode = KeyEvent.VK_INSERT;
                break;

            case NativeKeyEvent.VC_DELETE:
                keyCode = KeyEvent.VK_DELETE;
                break;

            case NativeKeyEvent.VC_HOME:
                keyCode = KeyEvent.VK_HOME;
                break;

            case NativeKeyEvent.VC_END:
                keyCode = KeyEvent.VK_END;
                break;

            case NativeKeyEvent.VC_PAGE_UP:
                keyCode = KeyEvent.VK_PAGE_UP;
                break;

            case NativeKeyEvent.VC_PAGE_DOWN:
                keyCode = KeyEvent.VK_PAGE_DOWN;
                break;
            // End Edit Key Zone


            // Begin Cursor Key Zone
            case NativeKeyEvent.VC_UP:
                keyCode = KeyEvent.VK_UP;
                break;
            case NativeKeyEvent.VC_LEFT:
                keyCode = KeyEvent.VK_LEFT;
                break;
            case NativeKeyEvent.VC_RIGHT:
                keyCode = KeyEvent.VK_RIGHT;
                break;
            case NativeKeyEvent.VC_DOWN:
                keyCode = KeyEvent.VK_DOWN;
                break;
            // End Cursor Key Zone


            // Begin Numeric Zone
            case NativeKeyEvent.VC_NUM_LOCK:
                keyCode = KeyEvent.VK_NUM_LOCK;
                break;


            case NativeKeyEvent.VC_CONTEXT_MENU:
                keyCode = KeyEvent.VK_CONTEXT_MENU;
                break;
            // End Modifier and Control Keys


			/* Begin Media Control Keys
            case NativeKeyEvent.VC_POWER:
			case NativeKeyEvent.VC_SLEEP:
			case NativeKeyEvent.VC_WAKE:
			case NativeKeyEvent.VC_MEDIA_PLAY:
			case NativeKeyEvent.VC_MEDIA_STOP:
			case NativeKeyEvent.VC_MEDIA_PREVIOUS:
			case NativeKeyEvent.VC_MEDIA_NEXT:
			case NativeKeyEvent.VC_MEDIA_SELECT:
			case NativeKeyEvent.VC_MEDIA_EJECT:
			case NativeKeyEvent.VC_VOLUME_MUTE:
			case NativeKeyEvent.VC_VOLUME_UP:
			case NativeKeyEvent.VC_VOLUME_DOWN:
			case NativeKeyEvent.VC_APP_MAIL:
			case NativeKeyEvent.VC_APP_CALCULATOR:
			case NativeKeyEvent.VC_APP_MUSIC:
			case NativeKeyEvent.VC_APP_PICTURES:
			case NativeKeyEvent.VC_BROWSER_SEARCH:
			case NativeKeyEvent.VC_BROWSER_HOME:
			case NativeKeyEvent.VC_BROWSER_BACK:
			case NativeKeyEvent.VC_BROWSER_FORWARD:
			case NativeKeyEvent.VC_BROWSER_STOP:
			case NativeKeyEvent.VC_BROWSER_REFRESH:
			case NativeKeyEvent.VC_BROWSER_FAVORITES:
			// End Media Control Keys */


            // Begin Japanese Language Keys
            case NativeKeyEvent.VC_KATAKANA:
                keyCode = KeyEvent.VK_KATAKANA;
                break;

            case NativeKeyEvent.VC_UNDERSCORE:
                keyCode = KeyEvent.VK_UNDERSCORE;
                break;

            //case VC_FURIGANA:

            case NativeKeyEvent.VC_KANJI:
                keyCode = KeyEvent.VK_KANJI;
                break;

            case NativeKeyEvent.VC_HIRAGANA:
                keyCode = KeyEvent.VK_HIRAGANA;
                break;

            //case VC_YEN:
            // End Japanese Language Keys


            // Begin Sun keyboards
            case NativeKeyEvent.VC_SUN_HELP:
                keyCode = KeyEvent.VK_HELP;
                break;

            case NativeKeyEvent.VC_SUN_STOP:
                keyCode = KeyEvent.VK_STOP;
                break;

            //case VC_SUN_FRONT:

            //case VC_SUN_OPEN:

            case NativeKeyEvent.VC_SUN_PROPS:
                keyCode = KeyEvent.VK_PROPS;
                break;

            case NativeKeyEvent.VC_SUN_FIND:
                keyCode = KeyEvent.VK_FIND;
                break;

            case NativeKeyEvent.VC_SUN_AGAIN:
                keyCode = KeyEvent.VK_AGAIN;
                break;

            //case NativeKeyEvent.VC_SUN_INSERT:

            case NativeKeyEvent.VC_SUN_COPY:
                keyCode = KeyEvent.VK_COPY;
                break;

            case NativeKeyEvent.VC_SUN_CUT:
                keyCode = KeyEvent.VK_CUT;
                break;
            // End Sun keyboards
        }

        return keyCode;
    }
}
