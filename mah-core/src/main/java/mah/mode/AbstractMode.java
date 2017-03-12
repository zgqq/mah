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
package mah.mode;

import mah.action.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zgq on 2017-01-09 09:50
 */
public abstract class AbstractMode implements Mode {
    private final Map<String, Action> namedActios = new HashMap<>();
    private final Map<Class<?>, List<Action>> actionHandlers = new HashMap<>();
    private final String name;
    private final Lock lock = new ReentrantLock();
    private final List<Mode> children = new ArrayList<>();

    public AbstractMode(String name, Mode child) {
        this.name = name;
        if (children == null) {
            throw new ModeException("Parent mode must not be null,child mode is " + getName());
        }
        this.children.add(child);
    }

    public AbstractMode(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    private void addNamedAction(Action action) {
        namedActios.put(action.getName(), action);
    }

    public final void registerAction(Action action) {
        Class<?> handler = action.getHandler();
        if (!(action instanceof NoSourceAction)) {
            if (handler == null) {
                throw new ActionException("actions must have a handler");
            }
            List<Action> actions = actionHandlers.get(handler);
            if (actions == null) {
                actions = new ArrayList<>();
                actionHandlers.put(handler, actions);
            } else {
                if (actions.contains(action)) {
                    throw new ActionException("Action should be registered once");
                }
            }
            actions.add(action);
        }
        addNamedAction(action);
    }

    @Override
    public final void updateActionHandler(ActionHandler actionHandler) {
        Class<? extends ActionHandler> clazz = actionHandler.getClass();
        actionHandlers.forEach((aClass, actions) -> {
                    if (aClass.isAssignableFrom(clazz)) {
                        for (Action action : actions) {
                            ActionManager.getInstance().updateActionHandler(action, actionHandler);
                        }
                    }
                }
        );
    }

    @Override
    public void addChild(Mode mode) {
        lock.lock();
        try {
            if (this.children.contains(mode)) {
                throw new IllegalStateException("Child already existed");
            }
            this.children.add(mode);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public final Action findAction(String actionName) {
        Action action = lookupAction(actionName);
        if (action == null) {
            throw new ActionException("Not found action " + actionName + " in mode " + getName());
        }
        return action;
    }

    @Nullable
    public final Action lookupAction(String actionName) {
        Action action = namedActios.get(actionName);
        if (action != null) {
            return action;
        }
        for (Mode child : children) {
            action = child.lookupAction(actionName);
            if (action != null) {
                return action;
            }
        }
        return null;
    }

    @Override
    public void trigger() {
        ModeManager.getInstance().triggerMode(this);
    }

    @Override
    public List<Mode> getChildren() {
        lock.lock();
        try {
            return Collections.unmodifiableList(children);
        } finally {
            lock.unlock();
        }
    }
}
