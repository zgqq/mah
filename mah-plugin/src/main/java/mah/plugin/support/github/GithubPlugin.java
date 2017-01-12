package mah.plugin.support.github;

import mah.openapi.plugin.PluginSupport;
import mah.plugin.support.github.starred.GithubStarredCommand;

/**
 * Created by zgq on 16-12-24.
 */
public class GithubPlugin extends PluginSupport{



    @Override
    public void init() {
        registerCommand(new GithubStarredCommand());
    }

}
