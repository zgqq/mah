package mah.mode;

import mah.action.Action;
import mah.action.ActionHandler;

import java.util.List;

/**
 * Created by zgq on 2017-01-09 09:14
 */
public interface Mode {

    default void init() {}

    List<Mode> getChildren();

    void addChild(Mode child);

    Action findAction(String actionName);

    Action lookupAction(String actionName);

    void updateActionHandler(ActionHandler actionHandler);

    String getName();

    void trigger();

}
