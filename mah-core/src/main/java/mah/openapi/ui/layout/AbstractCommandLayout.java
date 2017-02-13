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
import mah.mode.Mode;
import mah.plugin.command.CommandMode;
import mah.event.EventHandler;
import mah.ui.key.KeyEvent;
import mah.ui.layout.Layout;
import mah.ui.layout.ModeListener;

import java.util.concurrent.Callable;

/**
 * Created by zgq on 2017-01-21 18:12
 */
public class AbstractCommandLayout implements Layout {

    private final LayoutUpdater layoutUpdater;
    private final Layout layout;

    public AbstractCommandLayout(Layout layout, Command command) {
        this.layoutUpdater = new LayoutUpdater(layout, command);
        this.layout = layout;
        this.layout.init();
    }

    public Layout layout() {
        return layout;
    }

    protected final void updateWholeLayout(Runnable runnable) {
        layoutUpdater.updateWholeLayout(runnable);
    }

    protected final void updatePartLayout(Runnable runnable) {
        layoutUpdater.updatePartLayout(runnable);
    }

    protected final void runSafely(Runnable runnable) {
        layoutUpdater.runLater(runnable);
    }

    protected final <T> T getValue(Callable<T> callable) {
        return layoutUpdater.getValue(callable);
    }

    @Override
    public void registerMode(Mode mode, ModeListener modeListener) {
        runSafely(() -> {
            mode.addChild(CommandMode.registerModeIfAbsent());
            layout().registerMode(mode, modeListener);
        });
    }

    @Override
    public void setDefaultMode() {
        runSafely(() -> layout().setDefaultMode());
    }

    @Override
    public Mode getMode() {
        return getValue(() -> layout().getMode());
    }

    @Override
    public void init() {
        throw new IllegalStateException("Unable to invoke initilize method");
    }

    @Override
    public void setOnKeyPressed(EventHandler<? extends KeyEvent> keyPressedHandler) {
        runSafely(() -> {
            layout().setOnKeyPressed(keyPressedHandler);
        });
    }

    @Override
    public void setOnKeyReleased(EventHandler<? extends KeyEvent> keyReleasedHandler) {
        runSafely(() -> {
            layout().setOnKeyReleased(keyReleasedHandler);
        });
    }

    @Override
    public void onSetCurrentLayout() {
        runSafely(() -> layout().onSetCurrentLayout());
    }

    @Override
    public String getName() {
        return getValue(() -> layout().getName());
    }
}
