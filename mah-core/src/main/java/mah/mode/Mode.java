package mah.mode;

import mah.action.Action;
import mah.action.ActionHandler;

/**
 * Created by zgq on 2017-01-09 09:14
 */
public interface Mode {

    default void init(){

    }

    Action findAction(String actionName);

    void updateActionHandler(ActionHandler actionHandler);

    String getName();


}
