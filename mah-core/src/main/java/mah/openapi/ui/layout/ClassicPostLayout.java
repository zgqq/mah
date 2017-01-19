package mah.openapi.ui.layout;

import mah.command.Command;
import mah.command.CommandManager;
import mah.ui.UIManager;
import mah.ui.layout.LayoutWrapper;
import mah.ui.pane.post.Post;
import mah.ui.support.swing.layout.ClassicPostLayoutImpl;
import mah.ui.window.Window;
import mah.ui.window.WindowManager;

/**
 * Created by zgq on 2017-01-18 09:34
 */
public class ClassicPostLayout extends LayoutWrapper implements mah.ui.layout.ClassicPostLayout {

    private final Command command;
    private final Object lock = CommandManager.getInstance();


    protected ClassicPostLayout(Command command) {
        super(ClassicPostLayoutImpl.instance());
        this.command = command;
    }

    private ClassicPostLayoutImpl layout() {
        return (ClassicPostLayoutImpl) getLayout();
    }

    private boolean allowUpdateLayout() {
        Command currentCommand = CommandManager.getInstance().getCurrentCommand();
        return (currentCommand != null && currentCommand.equals(command));
    }

    private void updateWindowLayout() {
        Window currentWindow = WindowManager.getInstance().getCurrentWindow();
        currentWindow.setCurrentLayout(getLayout());
    }

    private void updateLayout(Runnable runnable) {
        synchronized (lock) {
            if (allowUpdateLayout()) {
                UIManager.getInstance().runLater(runnable);
            }
        }
    }

    @Override
    public void setPost(Post post) {
        updateLayout(() -> {
                    layout().setPost(post);
                    updateWindowLayout();
                }
        );
    }
}
