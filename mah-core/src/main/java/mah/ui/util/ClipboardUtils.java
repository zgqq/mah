package mah.ui.util;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Created by zgq on 2017-01-23 16:09
 */
public class ClipboardUtils {
    public static void copy(String text) {
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection;
        selection = new StringSelection(text);
        c.setContents(selection, selection);
    }
}
