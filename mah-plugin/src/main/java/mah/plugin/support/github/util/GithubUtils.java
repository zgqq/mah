package mah.plugin.support.github.util;

import mah.openapi.util.DesktopUtils;
import mah.plugin.support.github.entity.GithubRepository;

/**
 * Created by zgq on 2017-01-10 18:03
 */
public final class GithubUtils {

    private GithubUtils() {

    }
    public static void openRepository(GithubRepository repository) {
        String name = repository.getName();
        String url = "https://github.com/" + name;
        DesktopUtils.openBrowser(url);
    }

}
