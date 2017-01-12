package mah.action;

/**
 * Created by zgq on 2017-01-09 09:11
 */
public interface ActionHandler {

    default void handle(Action action) {
        ActionEvent actionEvent = new ActionEvent(this);
        try {
            action.actionPerformed(actionEvent);
        } catch (Exception e) {
            throw new ActionException(e);
        }
    }

}
