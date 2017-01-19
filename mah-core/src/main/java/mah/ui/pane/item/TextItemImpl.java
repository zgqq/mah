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
