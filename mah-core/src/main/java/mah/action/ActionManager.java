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
