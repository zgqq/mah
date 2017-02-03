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
package mah.app.system;

import mah.action.AbstractAction;
import mah.action.ActionEvent;
import mah.action.NoSourceAction;
import mah.app.ApplicationManager;
import mah.mode.AbstractMode;

/**
 * Created by zgq on 2017-01-12 16:12
 */
public class SystemMode extends AbstractMode {

    public static final String NAME = "system_mode";

    public SystemMode() {
        super(NAME);
    }

    @Override
    public void init() {
        registerAction(new ExitSystem("ExitSystem"));
    }

    abstract static class SystemAction extends AbstractAction implements NoSourceAction {
        public SystemAction(String name) {
            super(name, null);
        }
    }

    static class ExitSystem extends SystemAction {
        public ExitSystem(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ApplicationManager.getInstance().shutdown();
        }
    }
}
