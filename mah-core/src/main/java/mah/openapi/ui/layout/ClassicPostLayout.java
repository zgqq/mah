package mah.openapi.ui.layout;

import mah.command.Command;
import mah.ui.pane.post.Post;
import mah.ui.support.swing.layout.ClassicPostLayoutImpl;

/**
 * Created by zgq on 2017-01-18 09:34
 */
public class ClassicPostLayout extends AbstractCommandLayout implements mah.ui.layout.ClassicPostLayout {

    protected ClassicPostLayout(Command command) {
        super(ClassicPostLayoutImpl.instance(), command);
    }

    private ClassicPostLayoutImpl getLayout() {
        return (ClassicPostLayoutImpl) layout();
    }

    @Override
    public void setPost(Post post) {
        updateWholeLayout((() -> {
                    getLayout().setPost(post);
                })
        );
    }
}
