package mah.openapi.command;

import mah.command.CommandEvent;
import mah.command.filterable.FilterableCommand;
import mah.plugin.command.PluginCommandSupport;

/**
 * Created by zgq on 2017-01-09 16:57
 */
@Deprecated
public abstract class FilterableCommandSupport extends PluginCommandSupport implements FilterableCommand {


    @Override
    public void filter(CommandEvent commandEvent, String content) throws Exception {
        triggerMode();
        filterHook(commandEvent, content);
    }

    protected void filterHook(CommandEvent commandEvent, String content) throws Exception {

    }


}
