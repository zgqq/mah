package mah.openapi.ui.layout;

import mah.command.Command;
import mah.command.CommandManager;
import mah.ui.UIException;
import mah.ui.UIManager;
import mah.ui.layout.Layout;
import mah.ui.window.Window;
import mah.ui.window.WindowManager;

import javax.swing.*;
import java.util.concurrent.Callable;

/**
 * Created by zgq on 2017-01-21 17:12
 */
public class LayoutUpdater {

    private final Command command;
    private final Object lock;
    private final Layout layout;

    public LayoutUpdater(Layout layout, Command command) {
        this.layout = layout;
        this.command = command;
        lock = CommandManager.getInstance();
    }

    private boolean allowUpdateLayout() {
        Command currentCommand = CommandManager.getInstance().getCurrentCommand();
        return (currentCommand != null && currentCommand.equals(command));
    }

    public void updateWholeLayout(Runnable runnable) {
        synchronized (lock) {
            if (allowUpdateLayout()) {
                UIManager.getInstance().runLater(() -> {
                    runnable.run();
                    updateWindow();
                });
            }
        }
    }

    public void runLater(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }

    public void updatePartLayout(Runnable runnable) {
        synchronized (lock) {
            if (allowUpdateLayout()) {
                UIManager.getInstance().runLater(runnable);
            }
        }
    }

    public  <T> T getValue(Callable<T> callable) {
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                return callable.call();
            } else {
                final ValueHolder<T> value = new ValueHolder<T>();
                SwingUtilities.invokeAndWait(() -> {
                    T returnVal = handleException(callable);
                    value.value = returnVal;
                });
                return value.value;
            }
        } catch (Exception e) {
            throw new UIException(e);
        }
    }

    private static class ValueHolder<V> {
        V value;
    }

    private <T> T handleException(Callable<T> callable) {
        try {
            return callable.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updateWindow() {
        Window currentWindow = WindowManager.getInstance().getCurrentWindow();
        currentWindow.setCurrentLayout(layout);
    }
}
