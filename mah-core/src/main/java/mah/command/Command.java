package mah.command;

import mah.command.event.CommonFilterEvent;
import mah.command.event.EventHandler;
import mah.command.event.InitializeEvent;
import mah.command.event.TriggerEvent;

import java.util.List;

/**
 * Created by zgq on 2017-01-08 15:16
 */
public interface Command {

    List<EventHandler<? extends InitializeEvent>> getInitializeHandlers();

    List<EventHandler<? extends TriggerEvent>> getTriggerEventHandlers();

    List<EventHandler<? extends CommonFilterEvent>> getCommonFilterEventHandlers();

    default void idle() throws Exception {}

    String getName();

}


