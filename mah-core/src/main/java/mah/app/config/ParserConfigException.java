package mah.app.config;

import mah.app.ApplicationException;

/**
 * Created by zgq on 2017-01-08 20:01
 */
public class ParserConfigException extends ApplicationException{

    public ParserConfigException() {
    }

    public ParserConfigException(String message) {
        super(message);
    }

    public ParserConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParserConfigException(Throwable cause) {
        super(cause);
    }

    public ParserConfigException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
