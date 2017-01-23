package mah.ui.window;

import mah.action.AbstractAction;
import mah.action.ActionEvent;
import mah.action.GlobalAction;
import mah.command.Command;
import mah.command.CommandManager;
import mah.mode.AbstractMode;
import mah.mode.Mode;
import mah.mode.ModeManager;
import mah.ui.UIManager;
import mah.ui.input.TextState;
import mah.ui.key.KeystateManager;
import mah.ui.pane.input.InputPane;
import mah.ui.util.UIUtils;

import java.util.List;

/**
 * Created by zgq on 2017-01-09 09:49
 */
public class WindowMode extends AbstractMode {

    public static final String NAME = "window_mode";

    public WindowMode(Mode parent) {
        super(NAME, parent);
    }

    public WindowMode(String name) {
        super(name);
    }

    @Override
    public void init() {
        registerAction(new HideWindow("HideWindow"));
        registerAction(new FocusWindow("FocusWindow"));
        registerAction(new MoveWindowToLeft("MoveWindowToLeft"));
        registerAction(new MoveWindowToRight("MoveWindowToRight"));
        registerAction(new MoveWindowToCenter("MoveWindowToCenter"));
        registerAction(new FocusWindow("FocusWindow"));
        registerAction(new CenterWindowInSceenWithCursor("CenterWindowInSceenWithCursor"));
        registerAction(new FocusWindowAndClearInput("FocusWindowAndClearInput"));
    }

    public static Mode getOrRegisterMode() {
        Mode window_mode = ModeManager.getInstance().getMode(NAME);
        if (window_mode == null) {
            window_mode = new WindowMode(NAME);
            ModeManager.getInstance().registerMode(window_mode);
        }
        return window_mode;
    }

    public static Mode triggerMode() {
        Mode windowMode = getOrRegisterMode();
        ModeManager.getInstance().triggerMode(windowMode);
        return windowMode;
    }


    static abstract class WindowAction extends AbstractAction {

        public WindowAction(String name) {
            super(name, Window.class);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            Window window = (Window) source;
            UIManager.getInstance().runLater(() -> actionPerformed(window));
        }

        protected abstract void actionPerformed(Window window);
    }

    static class HideWindow extends WindowAction {

        public HideWindow(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Window window) {
            KeystateManager.getInstance().reset();
            window.hide();
        }
    }

    static class FocusWindow extends WindowAction implements GlobalAction {

        public FocusWindow(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Window window) {
            window.hide();
            window.show();
            Command lockedCommand = CommandManager.getInstance().getLockedCommand();
            if (lockedCommand != null) {
                List<String> maps = CommandManager.getInstance().findCommandMaps(lockedCommand);
                if (!maps.isEmpty()) {
                    String triggerKey = maps.get(0) + " ";
                    InputPane inputPane = UIUtils.getInputPane();
                    if (inputPane != null) {
                        TextState.Builder builder = new TextState.Builder(triggerKey, triggerKey.length());
                        inputPane.setTextState(builder.build());
                    }
                }
            }
        }
    }


    static class MoveWindowToCenter extends WindowAction {

        public MoveWindowToCenter(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Window window) {
            window.centerOnScreen();
        }
    }


    static class MoveWindowToRight extends WindowAction {

        public MoveWindowToRight(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Window window) {
            window.moveToRight();
        }
    }

    static class MoveWindowToLeft
            extends WindowAction {
        public MoveWindowToLeft(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Window window) {
            window.moveToLeft();
        }
    }

    static class FocusWindowAndClearInput extends WindowAction implements GlobalAction {

        public FocusWindowAndClearInput(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Window window) {
            window.show();
        }
    }


    static class CenterWindowInSceenWithCursor extends WindowAction implements GlobalAction {

        public CenterWindowInSceenWithCursor(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Window window) {
            window.hide();
            window.moveToCusorScreen();
            window.show();
        }
    }
}
