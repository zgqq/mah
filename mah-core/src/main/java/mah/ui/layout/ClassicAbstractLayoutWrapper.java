package mah.ui.layout;

import mah.ui.event.EventHandler;
import mah.ui.key.KeyEvent;
import mah.ui.pane.Pane;
import mah.ui.pane.input.InputPane;

/**
 * Created by zgq on 2017-01-08 12:24
 */
public abstract class ClassicAbstractLayoutWrapper extends LayoutWrapper implements ClassicAbstractLayout {

    private final ClassicAbstractLayout layout;

    public ClassicAbstractLayoutWrapper(ClassicAbstractLayout layout) {
        super(layout);
        this.layout = layout;
    }

    @Override
    public void updatePane(Pane pane) {
        layout.updatePane(pane);
    }

    @Override
    public void setDefaultMode() {
        layout.setDefaultMode();
    }

    public ClassicAbstractLayout getLayout() {
        return layout;
    }

    @Override
    public void setOnKeyPressed(EventHandler<? extends KeyEvent> keyPressedHandler) {
        layout.setOnKeyPressed(keyPressedHandler);
    }

    @Override
    public void setOnKeyReleased(EventHandler<? extends KeyEvent> keyReleasedHandler) {
        layout.setOnKeyReleased(keyReleasedHandler);
    }

    @Override
    public InputPane getInputPane() {
        return layout.getInputPane();
    }
}
