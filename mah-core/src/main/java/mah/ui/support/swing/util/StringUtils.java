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
package mah.ui.support.swing.util;

/**
 * Created by zgq on 2017-01-09 19:05
 */
public final class StringUtils {
    private static final int IDO_LEN = 3;

    private StringUtils() {}

    public static int getLen(String origin, String text, int maxLen) {
        int length;
        if ((length = getLength(origin)) > maxLen) {
            return 0;
        }

        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isIdeographic(c)) {
                length += IDO_LEN;
            } else {
                length += 1;
            }
            if (length > maxLen) {
                return i;
            }
        }
        return text.length();
    }

    public static int getLength(String text) {
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isIdeographic(c)) {
                length += IDO_LEN;
            } else {
                length += 1;
            }
        }
        return length;
    }

    public static String getStrBySpecificLength(String text, int len) {
        if (len <= 0) {
            throw new IllegalArgumentException("length must be greater than 0");
        }
        int index = getIndexBySpecificLength(text, len);
        if (index < 0) {
            return "";
        }

        if (mah.common.util.StringUtils.isEmpty(text)) {
            return "";
        }

        if (index >= text.length() - 1) {
            return text;
        }

        return text.substring(0, index + 1) + "...";
    }

    public static int getIndexBySpecificLength(String text, int len) {
        if (text == null) {
            return -1;
        }
        int length = 0;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (Character.isIdeographic(c)) {
                length += IDO_LEN;
            } else {
                length += 1;
            }
            if (length >= len) {
                return i - 1;
            }
        }
        return text.length() - 1;
    }
}
