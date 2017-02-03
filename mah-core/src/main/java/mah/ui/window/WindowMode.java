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
package mah.ui.window;

import mah.action.AbstractAction;
import mah.action.ActionEvent;
import mah.action.GlobalAction;
import mah.command.Command;
import mah.command.CommandManager;
import mah.mode.AbstractMode;
import mah.mode.Mode;
import mah.mode.ModeManager;
import mah.ui.UiManager;
import mah.ui.input.TextState;
import mah.ui.key.KeystateManager;
import mah.ui.pane.input.InputPane;
import mah.ui.util.UiUtils;

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
        Mode windowMode = ModeManager.getInstance().getMode(NAME);
        if (windowMode == null) {
            windowMode = new WindowMode(NAME);
            ModeManager.getInstance().registerMode(windowMode);
        }
        return windowMode;
    }

    public static Mode triggerMode() {
        Mode windowMode = getOrRegisterMode();
        ModeManager.getInstance().triggerMode(windowMode);
        return windowMode;
    }


    abstract static class WindowAction extends AbstractAction {
        public WindowAction(String name) {
            super(name, Window.class);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Object source = actionEvent.getSource();
            Window window = (Window) source;
            UiManager.getInstance().runLater(() -> actionPerformed(window));
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
            KeystateManager.getInstance().reset();
            window.hide();
            window.show();
            Command lockedCommand = CommandManager.getInstance().getLockedCommand();
            if (lockedCommand != null) {
                List<String> maps = CommandManager.getInstance().findCommandMaps(lockedCommand);
                if (!maps.isEmpty()) {
                    String triggerKey = maps.get(0) + " ";
                    InputPane inputPane = UiUtils.getInputPane();
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
