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
package mah.ui.pane.item;

import mah.ui.pane.Text;

/**
 * Created by zgq on 2017-01-14 13:35
 */
public class TextItemImpl implements TextItem{

    private final Text text;
    private final Object attachment;

    public TextItemImpl(Builder builder) {
        this.text = builder.text;
        this.attachment = builder.attachment;
    }

    public Text getText() {
        return text;
    }

    public Object getAttachment() {
        return attachment;
    }

    public static class Builder {
        private Text text;
        public Object attachment;

        public Builder(String text) {
            this.text = new Text(text);
        }

        public Builder attachment(Object attachment) {
            this.attachment = attachment;
            return this;
        }

        public TextItemImpl build() {
            return new TextItemImpl(this);
        }
    }
}
