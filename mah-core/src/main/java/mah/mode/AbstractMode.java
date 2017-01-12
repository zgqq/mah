package mah.mode;

import mah.action.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zgq on 2017-01-09 09:50
 */
public abstract class AbstractMode implements Mode {

    private final Map<String, Action> NAMED_ACTIONS = new HashMap<>();
    private final Map<Class<?>, List<Action>> HANDLERS_ACTIONS = new HashMap<>();
    private String name;
    private Mode parent;

    public AbstractMode(String name, Mode parent) {
        this.name = name;
        if (parent == null) {
            throw new ModeException("Parent mode must not be null,child mode is " + getName());
        }
        this.parent = parent;
    }

    public AbstractMode(String name) {
        this.name = name;
        this.parent = null;
    }

    @Override
    public String getName() {
        return name;
    }

    private void addNamedAction(Action action) {
        NAMED_ACTIONS.put(action.getName(), action);
    }

    public final void registerAction(Action action) {
        Class<?> handler = action.getHandler();
        if (!(action instanceof NoSourceAction)) {
            if (handler == null) {
                throw new ActionException("actions must have a handler");
            }
            List<Action> actions = HANDLERS_ACTIONS.get(handler);
            if (actions == null) {
                actions = new ArrayList<>();
                HANDLERS_ACTIONS.put(handler, actions);
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
        HANDLERS_ACTIONS.forEach((aClass, actions) -> {
                    if (aClass.isAssignableFrom(clazz)) {
                        for (Action action : actions) {
                            ActionManager.getInstance().updateActionHandler(action, actionHandler);
                        }
                    }
                }
        );
    }

    @Override
    public void setParent(Mode mode) {
        this.parent = mode;
    }

    @Override
    public final Action findAction(String actionName) {
        Action action = NAMED_ACTIONS.get(actionName);
        if (action == null && parent != null) {
            action = parent.findAction(actionName);
        }
        if (action == null) {
            throw new ActionException("Not found action " + actionName + " in mode " + getName());
        }
        return action;
    }

    @Override
    public void trigger() {
        ModeManager.getInstance().triggerMode(this);
    }

}
