package mah.openapi.ui.layout;

import mah.command.Command;
import mah.ui.UIException;
import mah.ui.layout.ClassicPostLayout;

import javax.swing.*;

/**
 * Created by zgq on 2017-01-09 17:21
 */
public class SwingLayoutFactory implements LayoutFactory {

    private final Command command;

    public SwingLayoutFactory(Command command) {
        this.command = command;
    }

    private interface Creator {
        void create();
    }

    private ClassicItemListLayout classicItemListLayout;
    private ClassicPostLayout classicPostLayout;

    private void createLayout(Creator creator) {
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                creator.create();
            } else {
                SwingUtilities.invokeAndWait(() -> {
                    creator.create();
                });
            }
        } catch (Exception e) {
            throw new UIException(e);
        }
    }

    @Override
    public mah.ui.layout.ClassicItemListLayout createClassicItemListLayout() {
        if (classicItemListLayout != null) {
            return classicItemListLayout;
        }
        createLayout(() -> {
            classicItemListLayout = new ClassicItemListLayout(command);
            classicItemListLayout.init();
        });
        return classicItemListLayout;
    }

    @Override
    public ClassicPostLayout createClassicPostLayout() {
        if (classicPostLayout != null) {
            return classicPostLayout;
        }
        createLayout(() -> {
            classicPostLayout = new mah.openapi.ui.layout.ClassicPostLayout(command);
            classicPostLayout.init();
        });
        return classicPostLayout;
    }
}
