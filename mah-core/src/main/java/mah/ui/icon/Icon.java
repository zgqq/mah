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
package mah.ui.icon;

import mah.common.util.HttpUtils;

import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by zgq on 2/9/17.
 */
public final class Icon {
    private static final ConcurrentMap<String, Icon> ICON_CACHES = new ConcurrentHashMap<>();
    private final String key;
    private final InputStream inputStream;

    private Icon(String path) {
        this.key = path;
        if (path.startsWith("http")) {
            this.inputStream = HttpUtils.getInputStream(path);
        } else {
            this.inputStream = getClass().getClassLoader().getResourceAsStream(path);
        }
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public String getKey() {
        return key;
    }

    public static Icon valueOf(String path) {
//        Icon icon = ICON_CACHES.get(path);
//        if (icon == null) {
//            icon = ICON_CACHES.putIfAbsent(path, new Icon(path));
//            if (icon != null) {
//                return icon;
//            }
//        }
        return new Icon(path);
    }
}
