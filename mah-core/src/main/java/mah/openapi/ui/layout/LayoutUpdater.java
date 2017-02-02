/**
 * MIT License
 *
 * Copyright (c) 2017 zgqq
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
