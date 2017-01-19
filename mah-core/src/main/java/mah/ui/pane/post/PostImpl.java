package mah.ui.pane.post;

import mah.ui.pane.Text;

import java.util.List;

/**
 * Created by zgq on 2017-01-17 15:30
 */
public final class PostImpl implements Post{

    private final Text title;
    private final Text content;
    private List<Text> additions;

    private PostImpl(Builder builder) {
        this.title = builder.title;
        this.content = builder.content;
        this.additions = builder.additions;
    }

    @Override
    public Text getTitle() {
        return title;
    }

    @Override
    public Text getContent() {
        return content;
    }

    @Override
    public List<Text> getAdditions() {
        return additions;
    }

    public static class Builder {

        private final Text title;
        private final Text content;
        private List<Text> additions;

        public Builder(String title, String content) {
            this.title = new Text(title);
            this.content = new Text(content);
        }

        public PostImpl build() {
            return new PostImpl(this);
        }

    }


}
