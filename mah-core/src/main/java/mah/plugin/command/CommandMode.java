/**
 * MIT License
 *
 * Copyright (c) 2017 zgqq
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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
                if (currentCommand != null) {
                    CommandManager.getInstance().setLockedCommand(currentCommand);
                }
            }
        }
    }
}
