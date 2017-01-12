package mah.plugin.support.github;

import mah.action.AbstractAction;
import mah.action.ActionEvent;
import mah.mode.AbstractMode;
import mah.plugin.support.github.entity.GithubRepository;
import mah.plugin.support.github.util.GithubUtils;
import mah.ui.layout.ClassicItemListLayout;
import mah.ui.pane.item.Item;

/**
 * Created by zgq on 16-12-25.
 */
public class GithubMode extends AbstractMode {

    private static final GithubMode INSTANCE = new GithubMode("github_mode");

    public GithubMode(String name) {
        super(name);
    }


    public static GithubMode getInstance() {
        return INSTANCE;
    }

    @Override
    public void init() {
        registerAction(new GoGithubIssues("GoGithubIssues"));
    }

    static class GithubStarred extends AbstractAction {

        public GithubStarred(String name) {
            super(name, GithubModeHandler.class);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) throws Exception{
            actionPerformed((GithubModeHandler) actionEvent.getSource());
        }

        protected void actionPerformed(GithubModeHandler command) throws Exception{

        }
    }


    static class GoGithubIssues extends GithubStarred {

        public GoGithubIssues(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(GithubModeHandler command) throws Exception {
            ClassicItemListLayout layout = command.getLayout();
            Item pendingItem = layout.getPendingItem();
            GithubUtils.openRepositoryIssues((GithubRepository) pendingItem.getAttachment());
            command.onGoGithubIssues(pendingItem);
        }
    }


}
