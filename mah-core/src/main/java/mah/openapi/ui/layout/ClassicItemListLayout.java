package mah.openapi.ui.layout;

import mah.command.Command;
import mah.command.CommandManager;
import mah.ui.UIManager;
import mah.ui.layout.ClassicItemListLayoutWrapper;
import mah.ui.pane.item.Item;
import mah.ui.support.swing.layout.ClassicItemListLayoutImpl;
import mah.ui.window.Window;
import mah.ui.window.WindowManager;

import java.util.List;

/**
 * Created by zgq on 2017-01-08 15:11
 */
public class ClassicItemListLayout extends ClassicItemListLayoutWrapper {

    private final Command command;
    private final Object lock = CommandManager.getInstance();

    public ClassicItemListLayout(Command command) {
        super(new ClassicItemListLayoutImpl());
        this.command = command;
    }


    private boolean allowUpdateLayout() {
        Command currentCommand = CommandManager.getInstance().getCurrentCommand();
        return (currentCommand != null && currentCommand.equals(command));
    }

    private void updateWindowLayout() {
        Window currentWindow = WindowManager.getInstance().getCurrentWindow();
        currentWindow.setCurrentLayout(getLayout());
    }

    private void updateLayout(Runnable runnable) {
        synchronized (lock) {
            if (allowUpdateLayout()) {
                UIManager.getInstance().runLater(runnable);
            }
        }
    }

    @Override
    public void updateItems(List<? extends Item> items) {
        updateLayout(() -> {
            getLayout().updateItems(items);
            updateWindowLayout();
        });
    }

    @Override
    public void updateItems(Item... items) {
        updateLayout(() -> {
            getLayout().updateItems(items);
            updateWindowLayout();
        });
    }
}
