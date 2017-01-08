package mah.ui;

import mah.app.ApplicationException;

/**
 * Created by zgq on 2017-01-08 14:29
 */
public class UIException extends ApplicationException{
    public UIException() {
    }

    public UIException(String message) {
        super(message);
    }

    public UIException(String message, Throwable cause) {
        super(message, cause);
    }

    public UIException(Throwable cause) {
        super(cause);
    }

    public UIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
