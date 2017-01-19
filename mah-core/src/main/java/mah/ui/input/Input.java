package mah.ui.input;

/**
 * Created by zgq on 2017-01-09 10:35
 */
public interface Input {

    void setTextStateQuietly(TextState textState);

    void setTextState(TextState textState);

    void beginningOfLine();

    void endOfLine();

    void forwardChar();

    void backwardChar();

    void deleteChar();

    void forwardWord();

    void backwardWord();

    void killWord();

    void killWholeLine();

    void killLine();

    void setText(String currentKey);

    void setCaretPosition(int position);

    void backwardKillWord();

    void backwardDeleteChar();

    String getText();

    void redo();

    void undo();

    int getCaretPosition();

    void clear();

}
