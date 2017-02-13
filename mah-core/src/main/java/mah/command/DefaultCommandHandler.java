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
package mah.command;

import mah.command.event.NotFoundCommandEvent;
import mah.event.AbstractEventHandler;
import mah.ui.layout.Layout;
import mah.ui.window.Window;
import mah.ui.window.WindowManager;

/**
 * Created by zgq on 2/13/17.
 */
public class DefaultCommandHandler extends AbstractEventHandler<NotFoundCommandEvent> {

    public DefaultCommandHandler(int priority) {
        super(priority);
    }

    @Override
    public void handle(NotFoundCommandEvent event) throws Exception {
        if (!event.isHandled()) {
            Window currentWindow = WindowManager.getInstance().getCurrentWindow();
            Layout currentLayout = currentWindow.getCurrentLayout();
            Layout defaultLayout = currentWindow.getDefaultLayout();
            if (currentLayout == defaultLayout) {
                return;
            }
            setDefaultLayout();
        }
    }

    private void setDefaultLayout() {
        WindowManager.getInstance().getCurrentWindow().useDefaultLayoutAsCurrentLayout();
    }
}
