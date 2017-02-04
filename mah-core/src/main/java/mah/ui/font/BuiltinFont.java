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
package mah.ui.font;

import java.awt.*;

/**
 * Created by zgq on 2/4/17.
 */
public final class BuiltinFont {
    private final String path;
    private final String name;
    private final String realName;
    private final Font font;

    private BuiltinFont(Builder builder) {
        this.path = builder.path;
        this.name = builder.name;
        this.realName = builder.realName;
        this.font = builder.font;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getRealName() {
        return realName;
    }

    public Font getFont() {
        return font;
    }

    public static class Builder {
        private final String path;
        private final String name;
        private final String realName;
        private Font font;


        public Builder(String path, String name, String realName) {
            if (path == null || name == null || realName == null) {
                throw new NullPointerException();
            }
            this.path = path;
            this.name = name;
            this.realName = realName;
        }

        public Builder font(Font font) {
            if (font == null) {
                throw new NullPointerException();
            }
            this.font = font;
            return this;
        }

        public String getPath() {
            return path;
        }

        public String getName() {
            return name;
        }

        public String getRealName() {
            return realName;
        }

        public Font getFont() {
            return font;
        }

        public BuiltinFont build() {
            return new BuiltinFont(this);
        }
    }
}
