package mah.openapi.ui.layout;

import mah.command.Command;

/**
 * Created by zgq on 2017-01-09 17:21
 */
public class SwingLayoutFactory implements LayoutFactory {

    private final Command command;

    public SwingLayoutFactory(Command command) {
        this.command = command;
    }

    @Override
    public mah.ui.layout.ClassicItemListLayout createClassicItemListLayout() {
        ClassicItemListLayout classicItemListLayout = new ClassicItemListLayout(command);
        classicItemListLayout.init();
        return classicItemListLayout;
    }

}
