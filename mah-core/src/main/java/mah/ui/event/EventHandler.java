package mah.ui.event;

import java.util.EventListener;

/**
 * Created by zgq on 2017-01-09 09:44
 */
public interface EventHandler<T> extends EventListener {

    void handle(T event);

}

