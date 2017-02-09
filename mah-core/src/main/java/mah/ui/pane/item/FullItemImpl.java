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

import mah.ui.icon.Icon;
import mah.ui.pane.Text;

import java.io.InputStream;
import java.util.List;

/**
 * Created by zgq on 2017-01-08 14:18
 */
public class FullItemImpl implements FullItem {

    private final Icon icon;
    private final InputStream iconInputItem;
    private final Text content;
    private final Text description;
    private final Object attachment;
    private final String iconName;

    public FullItemImpl(Builder builder) {
        this.content = builder.content;
        this.description = builder.description;
        this.iconInputItem = builder.iconInputStream;
        this.attachment = builder.attachment;
        this.iconName = builder.iconName;
        this.icon = builder.icon;
    }


    @Override
    public Object getAttachment() {
        return attachment;
    }

    @Override
    public String getIconName() {
        return iconName;
    }

    @Override
    public Icon getIcon() {
        return icon;
    }

    public InputStream getIconInputStream() {
        return iconInputItem;
    }


    public Text getContent() {
        return content;
    }


    public Text getDescription() {
        return description;
    }

    public static Builder builder(String content) {
        return new Builder(content);
    }

    public static class Builder {
        private InputStream iconInputStream;
        private Icon icon;
        private Text content;
        private Text description;
        private Object attachment;
        private String iconName;

        public Builder(String content,List<Integer> highlightIndexs) {
            this.content = new Text(content,highlightIndexs);
        }

        public Builder(String content) {
            this.content = new Text(content);
        }

        @Deprecated
        public Builder iconName(String iconName) {
            this.iconName = iconName;
            return this;
        }

        public Builder icon(Icon icon) {
            this.icon = icon;
            return this;
        }

        @Deprecated
        public Builder iconInputStream(InputStream inputStream) {
            this.iconInputStream = inputStream;
            return this;
        }

        public Builder attachment(Object attachment) {
            this.attachment = attachment;
            return this;
        }

        public FullItemImpl build() {
            return new FullItemImpl(this);
        }

        public Builder description(String description) {
            this.description = new Text(description);
            return this;
        }

        public Builder description(String description, List<Integer> matchedIndexs) {
            this.description = new Text(description, matchedIndexs);
            return this;
        }
    }
}
