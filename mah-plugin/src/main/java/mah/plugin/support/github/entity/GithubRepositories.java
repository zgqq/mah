package mah.plugin.support.github.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GithubRepositories {

    private final List<GithubRepository> repositories;
    private final List<Listener> listeners=new ArrayList<>(3);

    private HashSet<String> nameSet;

    public GithubRepositories(List<GithubRepository> repositories) {
        this.repositories = repositories;
    }

    public void updateNewRepository(String name, String description) {
        updateRepository(false, name, description);
    }

    private void updateRepository(boolean add, String name, String description) {
        if (description!=null && description.length() > 50) {
            description = description.substring(0, 50);
        }
        GithubRepository githubRepository = new GithubRepository(name, description);
        for (Listener listener : listeners) {
            listener.onRepositoryAdded(githubRepository);
        }
        if (add) {
            repositories.add(githubRepository);
        } else {
            repositories.add(0, githubRepository);
        }
    }

    public void addRepository(String name, String description) {
        updateRepository(true, name, description);
    }

    public GithubRepository get(int index) {
        return repositories.get(index);
    }

    public int size() {
        return repositories.size();
    }

    public String getFirstRepositoryName() {
        if (repositories.size() > 0) {
            return repositories.get(0).getName();
        }
        return null;
    }

    public boolean contains(String name) {
        Set<String> nameSet = getNameSet();
        return nameSet.contains(name);
    }

    private Set<String> getNameSet() {
        if (this.nameSet == null) {
            this.nameSet = new HashSet<>();
            repositories.forEach(s -> {
                this.nameSet.add(s.getName());
            });
        }
        return this.nameSet;
    }

    public List<GithubRepository> getRepositories() {
        return repositories;
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public interface Listener {

        default void onRepositoryAdded(GithubRepository repository) {

        }
    }
}

