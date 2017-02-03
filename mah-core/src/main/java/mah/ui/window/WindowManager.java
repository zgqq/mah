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

import mah.ui.UiManager;
import mah.ui.key.KeystateManager;
import mah.ui.layout.LayoutType;

/**
 * Created by zgq on 2017-01-08 11:39
 */
public class WindowManager {
    private static WindowManager ourInstance = new WindowManager();
    private WindowFactory windowFactory;
    private Window currentWindow;

    public static WindowManager getInstance() {
        return ourInstance;
    }

    private WindowManager() {
    }

    public void createWindow() {
        this.windowFactory = UiManager.getInstance().getWindowFactory();
        currentWindow = this.windowFactory.createWindow(new WindowProperties.Builder(LayoutType.DEFAULT).build());
    }

    public Window getCurrentWindow() {
        return currentWindow;
    }

    public void showWindow() {
        currentWindow.show();
    }

    public static void hideWindow() {
        UiManager.getInstance().runLater(() -> {
            KeystateManager.getInstance().reset();
            ourInstance.getCurrentWindow().hide();
        });
    }
}
