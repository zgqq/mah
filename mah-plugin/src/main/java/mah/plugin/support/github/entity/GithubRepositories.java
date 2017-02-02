/**
 * MIT License
 *
 * Copyright (c) 2017 zgqq
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

