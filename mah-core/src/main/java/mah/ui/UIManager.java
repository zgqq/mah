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
package mah.ui;

import mah.app.ApplicationEvent;
import mah.app.ApplicationListener;
import mah.app.system.SystemMode;
import mah.mode.ModeManager;
import mah.ui.input.InputMode;
import mah.ui.pane.item.ItemMode;
import mah.ui.support.swing.FactoryHelper;
import mah.ui.theme.ThemeManager;
import mah.ui.window.WindowFactory;
import mah.ui.window.WindowManager;
import mah.ui.window.WindowMode;

import javax.swing.*;

/**
 * Created by zgq on 2017-01-08 11:38
 */
public class UIManager implements ApplicationListener{

    private static UIManager ourInstance = new UIManager();
    private FactoryHelper helper = new FactoryHelper();

    public static UIManager getInstance() {
        return ourInstance;
    }

    private UIManager() {
    }

    private void registerMode() {
        ModeManager.getInstance().registerMode(new SystemMode());
        ModeManager.getInstance().registerMode(new WindowMode(ModeManager.getInstance().getMode(SystemMode.NAME)));
        ModeManager.getInstance().registerMode(new InputMode(ModeManager.getInstance().getMode(WindowMode.NAME)));
        ModeManager.getInstance().registerMode(new ItemMode(ModeManager.getInstance().getMode(InputMode.NAME)));
    }

    @Override
    public void start(ApplicationEvent applicationEvent) {
        registerMode();
        ThemeManager.getInstance().start(applicationEvent);
    }

    @Override
    public void afterStart(ApplicationEvent applicationEvent) {
        runLater(()->{
            WindowManager.getInstance().createWindow();
            WindowManager.getInstance().showWindow();
        });
    }

    public WindowFactory getWindowFactory() {
        return helper.createWindowFactory();
    }

    public void runLater(Runnable runnable) {
        SwingUtilities.invokeLater(runnable);
    }
}
