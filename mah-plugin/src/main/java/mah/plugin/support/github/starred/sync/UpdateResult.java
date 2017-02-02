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

