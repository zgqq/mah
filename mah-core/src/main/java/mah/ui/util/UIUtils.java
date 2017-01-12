package mah.ui.util;

import mah.ui.key.KeystateManager;
import mah.ui.layout.Layout;
import mah.ui.pane.input.InputPane;
import mah.ui.pane.input.InputPaneProvider;
import mah.ui.window.WindowManager;
import org.jetbrains.annotations.Nullable;

/**
 * Created by zgq on 2017-01-12 11:17
 */
public final class UIUtils {


    public static void hideWindow() {
        KeystateManager.getInstance().reset();
        WindowManager.getInstance().getCurrentWindow().hide();
    }

    @Nullable
    public static InputPane getInputPane() {
        Layout currentLayout = WindowManager.getInstance().getCurrentWindow().getCurrentLayout();
        if (currentLayout instanceof InputPaneProvider) {
            InputPaneProvider layout = (InputPaneProvider) currentLayout;
            return layout.getInputPane();
        }
        return null;
    }
}
