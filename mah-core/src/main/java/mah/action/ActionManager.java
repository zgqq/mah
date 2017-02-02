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
package mah.action;

import mah.app.ExecutorServices;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zgq on 2017-01-09 09:24
 */
public class ActionManager {

    private static final ActionManager INSTANCE = new ActionManager();
    private final Map<Action, ActionHandler> ACTION_HANDLERS = new HashMap<>();

    public static ActionManager getInstance() {
        return INSTANCE;
    }

    public void updateActionHandler(Action action, ActionHandler actionHandler) {
        ACTION_HANDLERS.put(action, actionHandler);
    }

    public void handleAction(Action action) {
        ActionHandler actionHandler = ACTION_HANDLERS.get(action);
        if (actionHandler == null) {
            if (action instanceof NoSourceAction) {
                try {
                    action.actionPerformed(ActionEvent.newActionEvent(null));
                } catch (Exception e) {
                    throw new ActionException(e);
                }
                return;
            }
            throw new ActionException("Not found action handler for " + action);
        }
        ExecutorServices.submit(() -> actionHandler.handle(action));
    }
}
