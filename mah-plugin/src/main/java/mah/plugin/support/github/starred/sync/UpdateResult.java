package mah.plugin.support.github.starred.sync;


import mah.plugin.support.github.entity.GithubRepositories;
import mah.plugin.support.github.entity.GithubRepository;

import java.util.List;

public class UpdateResult {

    private final int addRepositoryCount;
    private final GithubRepositories githubRepositories;

    public UpdateResult(int addRepositoryCount, GithubRepositories githubRepositories) {
        this.addRepositoryCount = addRepositoryCount;
        this.githubRepositories = githubRepositories;
    }

    public int getAddRepositoryCount() {
        return addRepositoryCount;
    }


    public List<GithubRepository> getRepositories() {
        return githubRepositories.getRepositories();
    }
}

