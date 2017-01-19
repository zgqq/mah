package mah.ui.pane;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by zgq on 2017-01-11 11:13
 */
public final class Text {

    private final String text;
    @Nullable
    private final List<Integer> highlightIndexs;

    public Text(String text) {
        this(text, null);
    }

    public Text(String text, List<Integer> highlightIndexs) {
        this.text = text;
        this.highlightIndexs = highlightIndexs;
    }

    public String getText() {
        return text;
    }

    @Nullable
    public List<Integer> getHighlightIndexs() {
        return highlightIndexs;
    }
}
