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
package mah.plugin.support.github.util;

import mah.openapi.util.DesktopUtils;
import mah.plugin.support.github.entity.GithubRepository;

/**
 * Created by zgq on 2017-01-10 18:03
 */
public final class GithubUtils {

    private GithubUtils() {}

    public static void openRepository(GithubRepository repository) {
        String name = repository.getName();
        String url = "https://github.com/" + name;
        DesktopUtils.openBrowser(url);
    }

    public static void openRepositoryIssues(GithubRepository repository) {
        String name = repository.getName();
        String url = "https://github.com/" + name + "/issues";
        DesktopUtils.openBrowser(url);
    }
}
