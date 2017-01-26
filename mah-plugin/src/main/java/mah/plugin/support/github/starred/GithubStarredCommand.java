package mah.plugin.support.github.starred;


import mah.command.CommandException;
import mah.command.event.EventHandler;
import mah.command.event.InitializeEvent;
import mah.command.event.TriggerEvent;
import mah.common.json.JSONUtils;
import mah.common.search.MatchedResult;
import mah.common.search.SearchResult;
import mah.common.util.IOUtils;
import mah.common.util.StringUtils;
import mah.openapi.search.CacheSearcher;
import mah.openapi.ui.layout.OpenClassicItemListLayout;
import mah.plugin.command.PluginCommandSupport;
import mah.plugin.config.XMLConfigurable;
import mah.plugin.support.github.GithubMode;
import mah.plugin.support.github.GithubModeHandler;
import mah.plugin.support.github.entity.GithubRepository;
import mah.plugin.support.github.starred.sync.RepositorySynchronizer;
import mah.plugin.support.github.starred.sync.SynchronizerListener;
import mah.plugin.support.github.starred.sync.UpdateResult;
import mah.plugin.support.github.util.GithubUtils;
import mah.ui.layout.ClassicItemListLayout;
import mah.ui.pane.item.FullItemImpl;
import mah.ui.pane.item.Item;
import mah.ui.pane.item.TextItemImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * Created by zgq on 16-12-24.
 */
public class GithubStarredCommand extends PluginCommandSupport implements XMLConfigurable, GithubModeHandler {

    private OpenClassicItemListLayout layout;
    private String localRepositoryFile;
    private String starredRepositoryAPI;
    private String username;
    private String token;
    private String command;
    private Logger logger = LoggerFactory.getLogger(GithubStarredCommand.class);
    private RepositorySynchronizer synchronizer;

    public GithubStarredCommand() {
        init();
    }

    private void openRepository(GithubRepository repository) throws IOException {
        GithubUtils.openRepository(repository);
        hideWindow();
        Runtime.getRuntime().exec(this.command);
    }


    private boolean checkConfig() {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(token)) {
            TextItemImpl textItem = new TextItemImpl.Builder("Are username and token provided in config?").build();
            layout.updateItems(textItem);
            return false;
        }
        return true;
    }

    public void init() throws CommandException {

        addInitializeHandler();

        addCommonFilterEventHandler(event -> {
            if (mah.common.util.StringUtils.isEmpty(event.getContent())) {
                return;
            }
            if (checkConfig()) {
                CacheSearcher<List<SearchResult>> searcher = synchronizer.getSearcher();
                List<SearchResult> searchResults = searcher.smartFuzzySearch(event.getContent());
                logger.info("searched {} results", searchResults.size());
                updateSearchView(searchResults,event.getContent());
            }
        });


        addTriggerEventHandler(new EventHandler<TriggerEvent>() {
            @Override
            public void handle(TriggerEvent event) throws Exception {
                trigger();
            }

            private void trigger() throws ExecutionException, InterruptedException {
                if (checkConfig()) {
                    if (synchronizer.isInit()) {
                        showUpdating();
                    }
                    synchronizer.fetchRepositories(9);
                }
            }
        });
    }


    private FullItemImpl convertToSearchItem(SearchResult searchResult) {
        GithubRepository githubRepository = (GithubRepository) searchResult.getDataRow();
        MatchedResult matchedResult = searchResult.getMatchedResult();
        FullItemImpl item = new FullItemImpl.Builder(githubRepository.getName(), matchedResult.findMatchedIndex(0))//
                .description(githubRepository.getDescription(), matchedResult.findMatchedIndex(1)) //
                .attachment(githubRepository) //
                .iconName("github_star_search") // will improve performance
                .iconInputStream(getIconInputStream())
                .build();
        return item;
    }

    private int getItemSize(List<?> list) {
        if (list == null) {
            return 0;
        }
        int size = list.size();
        return size > 9 ? 9 : size;
    }

    private void updateSearchView(List<SearchResult> searchResults,String expect) {
        List<FullItemImpl> items = new ArrayList<>();
        int itemSize = getItemSize(searchResults);
        for (int i = 0; i < itemSize; i++) {
            SearchResult searchResult = searchResults.get(i);
            items.add(convertToSearchItem(searchResult));
        }
        layout.compareAndUpdateItems(items,expect);
    }

    private InputStream getIconInputStream() {
        return getInputStreamFromClasspath("github/icon.png");
    }


    @Override
    public void configure(Node node) throws Exception {
        if (node == null) {
            return;
        }
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node item = childNodes.item(i);
            if (item.getNodeName().equals("username")) {
                this.username = item.getTextContent();
            } else if (item.getNodeName().equals("token")) {
                this.token = item.getTextContent();
            } else if (item.getNodeName().equals("postCommand")) {
                command = item.getTextContent();
            }
        }
    }

    @Override
    public String getName() {
        return "GithubStarredList";
    }


    private void addInitializeHandler() {
        addInitializeEventHandler(new EventHandler<InitializeEvent>() {

            private void initUI() {
                layout = getLayoutFactory().createClassicItemListLayout();
                layout.setOnItemSelected(e -> {
                    Item item = e.getItem();
                    GithubRepository attachment = (GithubRepository) item.getAttachment();
                    openRepository(attachment);
                });
                layout.registerMode(GithubMode.getInstance(), event -> {
                    GithubMode.getInstance().updateActionHandler(GithubStarredCommand.this);
                });
            }

            @Override
            public void handle(InitializeEvent event) throws Exception {
                initUI();
                localRepositoryFile = getFileStoredInPluginDataDir("starred_repositories.json");
                starredRepositoryAPI = "https://api.github.com/users/" + username + "/starred?access_token=" + token;
                IOUtils.createFileIfNotExists(localRepositoryFile);
                List<GithubRepository> repositoryData = JSONUtils.parseArrFromLocalFile(localRepositoryFile, GithubRepository.class);
                synchronizer = new RepositorySynchronizer(repositoryData, getExecutor(), localRepositoryFile, starredRepositoryAPI, new UpdatingUI());
            }
        });
    }


    @Override
    public void onGoGithubIssues(Item item) throws Exception {
        Runtime.getRuntime().exec(this.command);
        hideWindow();
    }

    @Override
    public ClassicItemListLayout getLayout() {
        return layout;
    }

    @Override
    public void onClearCache() {
        synchronizer.clear();
    }

    private void showUpdating() {
        layout.updateItems(Arrays.asList(createUpdatingItem(), createBlankItem()));
    }

    private FullItemImpl createBlankItem() {
        FullItemImpl item = new FullItemImpl.Builder("Waiting data ")//
                .description("Fetching data from github...") //
                .iconInputStream(getIconInputStream()) //
                .build();
        return item;
    }

    private FullItemImpl createUpdatingItem() {
        FullItemImpl item = new FullItemImpl.Builder("Please waiting!")//
                .description("Synchronizing repositories...") //
                .iconInputStream(getIconInputStream()) //
                .build();
        return item;
    }


    /**
     * Created by zgq on 2017-01-13 13:28
     */
    public class UpdatingUI implements SynchronizerListener {


        private void showNoRepositoryTips() {
            layout.updateItems(createNoRepositoryTips());
        }

        private FullItemImpl createNoRepositoryTips() {
            FullItemImpl item = new FullItemImpl.Builder("No repository added!") //
                    .description("Is the github token configured?") //
                    .iconInputStream(getIconInputStream()) //
                    .build();
            return item;
        }

        private FullItemImpl convertToItem(GithubRepository githubRepository) {
            FullItemImpl item = new FullItemImpl.Builder(githubRepository.getName())//
                    .description(githubRepository.getDescription()) //
                    .attachment(githubRepository) //
                    .iconInputStream(getIconInputStream())
                    .build();
            return item;
        }

        @Override
        public void fetchRepositories(List<GithubRepository> repositories) {
            List<FullItemImpl> fullItems = new ArrayList<>();
            for (int i = 0; i < getItemSize(repositories); i++) {
                GithubRepository githubRepository = repositories.get(i);
                fullItems.add(convertToItem(githubRepository));
            }
            layout.updateItems(fullItems);
        }

        @Override
        public void repositoryAdded(GithubRepository repository) {
            if (synchronizer.isInit()) {
                if (layout.getItemCount() == 0) {
                    logger.info("layout has no any item");
                    showUpdating();
                }
                layout.updateItem(convertToItem(repository), 2);
            }
        }

        @Override
        public void startInitialization() {
            showUpdating();
        }

        @Override
        public void endInitialization(UpdateResult result) {
            int count = result.getAddRepositoryCount();
            if (count == 0) {
                showNoRepositoryTips();
            }
        }
    }
}

