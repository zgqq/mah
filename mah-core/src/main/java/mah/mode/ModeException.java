package mah.mode;

import mah.plugin.PluginException;

/**
 * Created by zgq on 2017-01-09 09:18
 */
public class ModeException extends PluginException {
    public ModeException() {
    }

    public ModeException(String message) {
        super(message);
    }

    public ModeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ModeException(Throwable cause) {
        super(cause);
    }

    public ModeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
