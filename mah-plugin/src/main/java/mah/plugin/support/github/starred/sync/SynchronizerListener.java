package mah.plugin.support.github.starred.sync;

import mah.plugin.support.github.entity.GithubRepository;

import java.util.List;

/**
 * Created by zgq on 2017-01-13 13:31
 */
public interface SynchronizerListener {

    void fetchRepositories(List<GithubRepository> repositories);

    void repositoryAdded(GithubRepository repository);

    void startInitialization();

    void endInitialization(UpdateResult result);


}
