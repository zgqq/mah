package mah.openapi.ui.layout;

import mah.command.Command;
import mah.ui.UIException;

import javax.swing.*;

/**
 * Created by zgq on 2017-01-09 17:21
 */
public class SwingLayoutFactory implements LayoutFactory {

    private final Command command;

    public SwingLayoutFactory(Command command) {
        this.command = command;
    }

    private ClassicItemListLayout classicItemListLayout;

    @Override
    public mah.ui.layout.ClassicItemListLayout createClassicItemListLayout() {
        try {
            if (classicItemListLayout != null) {
                return classicItemListLayout;
            }
            SwingUtilities.invokeAndWait(() -> {
                classicItemListLayout = new ClassicItemListLayout(command);
                classicItemListLayout.init();
            });
            return classicItemListLayout;
        } catch (Exception e) {
            throw new UIException(e);
        }
    }

}
