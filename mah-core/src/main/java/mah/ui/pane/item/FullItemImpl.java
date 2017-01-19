package mah.ui.pane.item;

import mah.ui.pane.Text;

import java.io.InputStream;
import java.util.List;

/**
 * Created by zgq on 2017-01-08 14:18
 */
public class FullItemImpl implements FullItem{

    private final InputStream iconInputItem;
    private final Text content;
    private final Text description;
    private final Object attachment;
    private int num;

    public FullItemImpl(Builder builder) {
        this.content = builder.content;
        this.description = builder.description;
        this.iconInputItem = builder.iconInputStream;
        this.attachment = builder.attachment;
    }


    @Override
    public Object getAttachment() {
        return attachment;
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


    public static class Builder {
        private InputStream iconInputStream;
        private Text content;
        private Text description;
        private Object attachment;

        public Builder(String content,List<Integer> highlightIndexs) {
            this.content = new Text(content,highlightIndexs);
        }

        public Builder(String content) {
            this.content = new Text(content);
        }

        public Builder description(String description) {
            this.description = new Text(description);
            return this;
        }

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

        public Builder description(String description, List<Integer> matchedIndexs) {
            this.description = new Text(description, matchedIndexs);
            return this;
        }
    }
}
