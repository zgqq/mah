package mah.openapi.ui.layout;

import mah.command.Command;
import mah.mode.Mode;
import mah.ui.event.EventHandler;
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
        this.layoutUpdater = new LayoutUpdater(layout,command);
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
        runSafely(() -> layout().registerMode(mode, modeListener));
    }

    @Override
    public void setDefaultMode() {
        runSafely(()->layout().setDefaultMode());
    }

    @Override
    public Mode getMode() {
        return getValue(()->layout().getMode());
    }

    @Override
    public void init() {
        throw new IllegalStateException("Unable to invoke initilize method");
    }

    @Override
    public void setOnKeyPressed(EventHandler<? extends KeyEvent> keyPressedHandler) {
        runSafely(()->{layout().setOnKeyPressed(keyPressedHandler);});
    }

    @Override
    public void setOnKeyReleased(EventHandler<? extends KeyEvent> keyReleasedHandler) {
        runSafely(()->{layout().setOnKeyReleased(keyReleasedHandler);});
    }

    @Override
    public void onSetCurrentLayout() {
        runSafely(() -> layout().onSetCurrentLayout());
    }

    @Override
    public String getName() {
        return getValue(()->layout().getName());
    }
}
