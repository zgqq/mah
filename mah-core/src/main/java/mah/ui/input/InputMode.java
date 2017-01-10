package mah.ui.input;

import mah.action.AbstractAction;
import mah.action.ActionEvent;
import mah.mode.AbstractMode;
import mah.mode.Mode;
import mah.mode.ModeManager;
import mah.ui.window.WindowMode;

/**
 * Created by zgq on 2017-01-09 10:33
 */
public class InputMode extends AbstractMode {

    public static final String NAME = "input_mode";


    public InputMode(Mode parent) {
        super(NAME, parent);
    }

    private static String FORWARD_CHAR = "ForwardChar";
    private static String BACKWARD_CHAR = "BackwardChar";
    private static String DELETE_CHAR = "DeleteChar";

    private static String FORWARD_WORD = "ForwardWord";
    private static String BACKWARD_WORD = "BackwardWord";
    private static String KILL_WORD = "KillWord";

    private static String BEGINNING_OF_LINE = "BeginningOfLine";
    private static String END_OF_LINE = "EndOfLine";
    private static String KILL_LINE = "KillLine";

    @Override
    public void init() {
        registerAction(new ForwardChar(FORWARD_CHAR));
        registerAction(new BackwardChar(BACKWARD_CHAR));
        registerAction(new DeleteChar(DELETE_CHAR));

        registerAction(new ForwardWord(FORWARD_WORD));
        registerAction(new BackwardWord(BACKWARD_WORD));
        registerAction(new KillWord(KILL_WORD));
        registerAction(new BackwardKillWord("BackwardKillWord"));

        registerAction(new BackwardDeleteChar("BackwardDeleteChar"));

        registerAction(new BeginningOfLine(BEGINNING_OF_LINE));
        registerAction(new EndOfLine(END_OF_LINE));
        registerAction(new KillLine(KILL_LINE));

        registerAction(new Undo("Undo"));
        registerAction(new Redo("Redo"));
    }

    public static InputMode trigger() {
        InputMode inputMode = getAndRegisterMode();
        ModeManager.getInstance().triggerMode(inputMode);
        return inputMode;
    }

    public static InputMode getAndRegisterMode() {
        return (InputMode) ModeManager.getInstance().getOrRegisterMode(new InputMode(WindowMode.getOrRegisterMode()));
    }

    static abstract class InputAction extends AbstractAction {
        public InputAction(String name) {
            super(name, Input.class);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            Input source = (Input) actionEvent.getSource();
            actionPerformed(source);
        }

        protected abstract void actionPerformed(Input source);
    }

    static class KillLine extends InputAction {

        public KillLine(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Input source) {
            source.killLine();
        }
    }

    static class BeginningOfLine extends InputAction {

        public BeginningOfLine(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Input source) {
            source.beginningOfLine();
        }
    }

    static class EndOfLine extends InputAction {

        public EndOfLine(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Input source) {
            source.endOfLine();
        }
    }

    static class BackwardKillWord extends InputAction {

        public BackwardKillWord(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Input source) {
            source.backwardKillWord();
        }
    }

    static class KillWord extends InputAction {
        public KillWord(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Input source) {
            source.killWord();
        }
    }

    static class ForwardWord extends InputAction {
        public ForwardWord(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Input source) {
            source.forwardWord();
        }
    }


    static class BackwardWord extends InputAction {

        public BackwardWord(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Input source) {
            source.backwardWord();
        }
    }


    static class DeleteChar extends InputAction {
        public DeleteChar(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Input source) {
            source.deleteChar();
        }
    }

    static class ForwardChar extends InputAction {
        public ForwardChar(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Input source) {
            source.forwardChar();
        }

    }

    static class BackwardChar extends InputAction {
        public BackwardChar(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Input source) {
            source.backwardChar();
        }
    }

    static class BackwardDeleteChar extends InputAction {
        public BackwardDeleteChar(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Input source) {
            source.backwardDeleteChar();
        }
    }


    static class Undo extends InputAction {

        public Undo(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Input source) {
            source.undo();
        }
    }


    static class Redo extends InputAction {

        public Redo(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(Input source) {
            source.redo();
        }
    }
}
