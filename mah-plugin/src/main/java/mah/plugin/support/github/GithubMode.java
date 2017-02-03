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
package mah.plugin.support.github;

import mah.action.AbstractAction;
import mah.action.ActionEvent;
import mah.mode.AbstractMode;
import mah.plugin.support.github.entity.GithubRepository;
import mah.plugin.support.github.util.GithubUtils;
import mah.ui.layout.ClassicItemListLayout;
import mah.ui.pane.item.Item;

/**
 * Created by zgq on 16-12-25.
 */
public class GithubMode extends AbstractMode {
    private static final GithubMode INSTANCE = new GithubMode("github_mode");

    public GithubMode(String name) {
        super(name);
    }

    public static GithubMode getInstance() {
        return INSTANCE;
    }

    @Override
    public void init() {
        registerAction(new GoGithubIssues("GoGithubIssues"));
        registerAction(new ClearCache("ClearCache"));
    }

    static class GithubAction extends AbstractAction {

        public GithubAction(String name) {
            super(name, GithubModeHandler.class);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) throws Exception {
            actionPerformed((GithubModeHandler) actionEvent.getSource());
        }

        protected void actionPerformed(GithubModeHandler command) throws Exception{

        }
    }


    static class ClearCache extends GithubAction {

        public ClearCache(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(GithubModeHandler command) throws Exception {
            command.onClearCache();
        }
    }

    static class GoGithubIssues extends GithubAction {

        public GoGithubIssues(String name) {
            super(name);
        }

        @Override
        protected void actionPerformed(GithubModeHandler command) throws Exception {
            ClassicItemListLayout layout = command.getLayout();
            Item pendingItem = layout.getPendingItem();
            GithubUtils.openRepositoryIssues((GithubRepository) pendingItem.getAttachment());
            command.onGoGithubIssues(pendingItem);
        }
    }
}
