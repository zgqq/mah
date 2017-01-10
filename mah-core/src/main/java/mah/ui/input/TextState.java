package mah.ui.input;

/**
 * Created by zgq on 2017-01-09 08:58
 */
public class TextState {

    private final String text;
    private final int position;

    public TextState(Builder builder) {
        this.text = builder.text;
        this.position = builder.position;
    }

    public int getPosition() {
        return position;
    }

    public String getText() {
        return text;
    }


    public static class Builder {
        private final String text;
        private final int position;

        public Builder(String text, int position) {
            this.text = text;
            this.position = position;
        }

        public TextState build() {
            return new TextState(this);
        }
    }
}
