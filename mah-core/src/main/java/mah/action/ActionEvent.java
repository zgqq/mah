package mah.action;

/**
 * Created by zgq on 2017-01-09 09:13
 */
public class ActionEvent {

    private Object source;

    public ActionEvent(Object source) {
        this.source = source;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public static ActionEvent newActionEvent(Object source) {
        return new ActionEvent(source);
    }
}
