package mah.command.filterable;

import mah.command.Command;
import mah.command.CommandEvent;

/**
 * Created by zgq on 2017-01-09 09:06
 */
@Deprecated
public interface FilterableCommand extends Command {

    void filter(CommandEvent event, String content) throws Exception;

}
