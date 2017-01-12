package mah.ui.support.swing.pane.item;

import mah.ui.UIException;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import java.util.List;

/**
 * Created by zgq on 2017-01-11 20:29
 */
public class HightlightHelper {

    public static DefaultStyledDocument createHightlightDocument(String content, List<Integer> highlightIndexs, Style matchedStyle, Style generalStyle) {
        DefaultStyledDocument defaultStyledDocument = new DefaultStyledDocument();
        int j = 0;
        try {
            for (int i = 0; i < content.length(); i++) {
                char c = content.charAt(i);
                if (j < highlightIndexs.size()) {
                    Integer integer = highlightIndexs.get(j);
                    if (integer == i) {
                        defaultStyledDocument.insertString(integer, String.valueOf(c), matchedStyle);
                        j++;
                        continue;
                    }
                }
                defaultStyledDocument.insertString(i, String.valueOf(c), generalStyle);
            }
        } catch (BadLocationException e) {
            throw new UIException(e);
        }
        return defaultStyledDocument;
    }

}
