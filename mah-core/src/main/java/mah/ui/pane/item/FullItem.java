package mah.ui.pane.item;

import java.io.InputStream;

/**
 * Created by zgq on 2017-01-08 14:18
 */
public class FullItem implements Item{

    private final InputStream iconInputItem;
    private final String content;
    private final String description;
    private int num;

    public FullItem(Builder builder) {
        this.content = builder.content;
        this.description = builder.description;
        this.iconInputItem = builder.iconInputStream;
    }

    @Override
    public void setNum(int num) {
        this.num = num;
    }

    public InputStream getIconInputItem() {
        return iconInputItem;
    }


    public String getContent() {
        return content;
    }


    public String getDescription() {
        return description;
    }


    @Override
    public int getNum() {
        return num;
    }

    public static class Builder {
        private InputStream iconInputStream;
        private String content;
        private String description;

        public Builder(String content) {
            this.content = content;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder iconInputStream(InputStream inputStream) {
            this.iconInputStream = inputStream;
            return this;
        }

        public FullItem build() {
            return new FullItem(this);
        }

    }
}
