package mah.plugin.support.github;


import mah.ui.layout.ClassicItemListLayout;
import mah.ui.pane.item.Item;

/**
 * Created by zgq on 2017-01-12 11:35
 */
public interface GithubModeHandler {

    void onGoGithubIssues(Item item) throws Exception;

    ClassicItemListLayout getLayout();

}
