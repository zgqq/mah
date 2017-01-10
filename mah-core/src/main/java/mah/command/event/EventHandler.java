package mah.command.event;

import java.util.EventListener;

/**
 * Created by zgq on 2017-01-10 10:52
 */
public interface EventHandler<T> extends EventListener{

    void handle(T event) throws Exception;

}
