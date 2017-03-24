package mah.mode;

import mah.action.Action;

/**
 * Created by zgq on 17-3-24.
 */
public class FindResult {
    private final Action action;
    private final ResultType resultType;

    public FindResult(Action action, ResultType resultType) {
        this.action = action;
        this.resultType = resultType;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public Action getAction() {
        return action;
    }

    public enum ResultType {
        FOUND,NOT_FOUND,EXCLUDED
    }
}
