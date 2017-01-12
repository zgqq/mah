package mah.app.system;

import mah.action.AbstractAction;
import mah.action.ActionEvent;
import mah.action.NoSourceAction;
import mah.app.ApplicationManager;
import mah.command.Command;
import mah.command.CommandManager;
import mah.mode.AbstractMode;

/**
 * Created by zgq on 2017-01-12 16:12
 */
public class SystemMode extends AbstractMode {

    public static final String NAME = "system_mode";

    public SystemMode() {
        super(NAME);
    }

    @Override
    public void init() {
        registerAction(new ExitSystem("ExitSystem"));
        registerAction(new ToggleLockCommand("ToggleLockCommand"));
    }

    static abstract class SystemAction extends AbstractAction implements NoSourceAction {
        public SystemAction(String name) {
            super(name, null);
        }
    }

    static class ToggleLockCommand extends SystemAction {
        public ToggleLockCommand(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Command lockedCommand = CommandManager.getInstance().getLockedCommand();
            if (lockedCommand != null) {
                CommandManager.getInstance().setLockedCommand(null);
            } else {
                Command currentCommand = CommandManager.getInstance().getCurrentCommand();
                if (currentCommand != null)
                    CommandManager.getInstance().setLockedCommand(currentCommand);
            }
        }
    }

    static class ExitSystem extends SystemAction {
        public ExitSystem(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ApplicationManager.getInstance().shutdown();
        }
    }
}
