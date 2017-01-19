package mah.mode;

import mah.action.Action;
import mah.action.ActionHandler;

/**
 * Created by zgq on 2017-01-09 09:14
 */
public interface Mode {

    default void init(){};

    Mode getParent();

    void setParent(Mode mode);

    Action findAction(String actionName);

    void updateActionHandler(ActionHandler actionHandler);

    String getName();

    void trigger();

}
