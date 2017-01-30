package mah.plugin.command;

import mah.action.AbstractAction;
import mah.action.ActionEvent;
import mah.action.NoSourceAction;
import mah.command.Command;
import mah.command.CommandManager;
import mah.mode.AbstractMode;
import mah.mode.ModeManager;
import org.jetbrains.annotations.NotNull;

/**
 * Created by zgq on 2017-01-29 20:53
 */
public class CommandMode extends AbstractMode {

    private static final CommandMode INSTANCE = new CommandMode("command_mode");

    public static CommandMode triggerMode() {
        CommandMode inputMode = registerModeIfAbsent();
        ModeManager.getInstance().triggerMode(inputMode);
        return inputMode;
    }

    @NotNull
    public static CommandMode registerModeIfAbsent() {
        return (CommandMode) ModeManager.getInstance().getOrRegisterMode(INSTANCE);
    }

    public CommandMode(String name) {
        super(name);
    }

    @Override
    public void init() {
        registerAction(new ToggleLockCommand("ToggleLockCommand"));
    }

    static class ToggleLockCommand extends AbstractAction implements NoSourceAction {
        public ToggleLockCommand(String name) {
            super(name, null);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) throws Exception {
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
}
