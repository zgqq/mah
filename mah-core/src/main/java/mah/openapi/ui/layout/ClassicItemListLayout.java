package mah.openapi.ui.layout;

import mah.ui.layout.ClassicItemListLayoutWrapper;
import mah.ui.pane.item.Item;
import mah.ui.support.swing.layout.ClassicItemListLayoutImpl;
import mah.ui.window.Window;
import mah.ui.window.WindowManager;

import javax.swing.*;
import java.util.List;

/**
 * Created by zgq on 2017-01-08 15:11
 */
public class ClassicItemListLayout extends ClassicItemListLayoutWrapper {

    public ClassicItemListLayout() {
        super(new ClassicItemListLayoutImpl());
    }


    private boolean allowUpdateLayout() {
        return true;
    }

    private void updateLayout() {
        Window currentWindow = WindowManager.getInstance().getCurrentWindow();
        currentWindow.update(getLayout());
    }

    @Override
    public void updateItems(List<? extends Item> items) {
        if (allowUpdateLayout()) {
            SwingUtilities.invokeLater(() -> {
                getLayout().updateItems(items);
                updateLayout();
            });
        }
    }
}
