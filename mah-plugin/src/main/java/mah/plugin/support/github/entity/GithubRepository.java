package mah.plugin.support.github.entity;

import mah.common.search.Searchable;

import java.util.Arrays;
import java.util.List;

/**
 * Created by zgq on 16-12-24.
 */
public final class GithubRepository implements Searchable {

    private String name;
    private String description;
    private List<String> data;

    public GithubRepository() {

    }

    public GithubRepository(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public List<String> fetchData() {
        if (this.data == null) {
            this.data = Arrays.asList(name, description);
        }
        return data;
    }
}


