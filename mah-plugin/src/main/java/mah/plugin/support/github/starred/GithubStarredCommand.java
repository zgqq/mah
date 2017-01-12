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
import mah.mode.Mode;
import mah.mode.ModeManager;
import mah.openapi.search.CacheSearcher;
import mah.plugin.PluginException;
import mah.plugin.command.PluginCommandSupport;
import mah.plugin.config.XMLConfigurable;
import mah.plugin.support.github.entity.GithubRepositories;
import mah.plugin.support.github.entity.GithubRepositories.Listener;
import mah.plugin.support.github.entity.GithubRepository;
import mah.plugin.support.github.starred.sync.RepositoryUpdater;
import mah.plugin.support.github.starred.sync.UpdateResult;
import mah.plugin.support.github.util.GithubUtils;
import mah.ui.layout.ClassicItemListLayout;
import mah.ui.pane.item.FullItemImpl;
import mah.ui.pane.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


/**
 * Created by zgq on 16-12-24.
 */
public class GithubStarredCommand extends PluginCommandSupport implements XMLConfigurable {

    private ClassicItemListLayout layout;
    private String localRepositoryFile;
    private String starredRepositoryAPI;
    private String username;
    private String token;
    private String command;
    private volatile List<GithubRepository> repositoryData;
    private Logger logger = LoggerFactory.getLogger(GithubStarredCommand.class);
    private volatile boolean updating;
    private volatile CacheSearcher<List<SearchResult>> searcher;
    private RepositoryUpdater repositoryUpdater;

    public GithubStarredCommand() {
        init();
    }


    private void openRepository(GithubRepository repository) throws IOException {
        GithubUtils.openRepository(repository);
        Runtime.getRuntime().exec(this.command);
        hideWindow();
    }

    public void init() throws CommandException {

        addInitializeHandler();

        addCommonFilterEventHandler(event -> {
            if (updating || StringUtils.isEmpty(event.getContent())) {
                return;
            }
            List<SearchResult> searchResults = searcher.smartFuzzySearch(event.getContent());
            logger.info("searched {} results", searchResults.size());
            updateSearchView(searchResults);
        });


        addTriggerEventHandler(new EventHandler<TriggerEvent>() {

            @Override
            public void handle(TriggerEvent event) throws Exception {
                if (repositoryData == null || repositoryData.size() <= 0) {
                    showUpdating();
                    updating = true;
                    UpdateResult updateResult = repositoryUpdater.getUpdateResult();
                    updating = false;
                    if (updateResult == null || updateResult.getAddRepositoryCount() == 0) {
                        showNoRepositoryTips();
                    } else {
                        updateTriggerView(updateResult.getRepositories());
                    }
                } else {
                    updateTriggerView(repositoryData);
                }
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


            private void updateTriggerView(List<GithubRepository> githubRepositories) {
                List<FullItemImpl> fullItems = new ArrayList<>();
                for (int i = 0; i < getItemSize(githubRepositories); i++) {
                    GithubRepository githubRepository = githubRepositories.get(i);
                    fullItems.add(convertToItem(githubRepository));
                }
                layout.updateItems(fullItems);
            }
        });
    }

    private FullItemImpl convertToItem(GithubRepository githubRepository) {
        FullItemImpl item = new FullItemImpl.Builder(githubRepository.getName())//
                .description(githubRepository.getDescription()) //
                .attachment(githubRepository) //
                .iconInputStream(getIconInputStream())
                .build();
        return item;
    }

    private FullItemImpl convertToSearchItem(SearchResult searchResult) {
        GithubRepository githubRepository = (GithubRepository) searchResult.getDataRow();
        MatchedResult matchedResult = searchResult.getMatchedResult();
        FullItemImpl item = new FullItemImpl.Builder(githubRepository.getName(),matchedResult.findMatchedIndex(0))//
                .description(githubRepository.getDescription(),matchedResult.findMatchedIndex(1)) //
                .attachment(githubRepository) //
                .iconInputStream(getIconInputStream())
                .build();
        return item;
    }

    private int getItemSize(List<?> list) {
        int size = list.size();
        return size > 9 ? 9 : size;
    }

    private void updateSearchView(List<SearchResult> searchResults) {
        List<FullItemImpl> items = new ArrayList<>();
        int itemSize = getItemSize(searchResults);
        for (int i = 0; i < itemSize; i++) {
            SearchResult searchResult = searchResults.get(i);
            items.add(convertToSearchItem(searchResult));
        }
        layout.updateItems(items);
    }

    private InputStream getIconInputStream() {
        return getInputStreamFromClasspath("github/icon.png");
    }


    @Override
    public void configure(Node node) throws Exception {
        if (node == null) {
            throw new PluginException("Are username and token provided in config?");
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


    private static final String MODE = "github_starred";


    private void updateRepositories() {
        List<GithubRepository> prevData = this.repositoryData;
        List<GithubRepository> data;
        if (this.repositoryData == null) {
            data = new ArrayList<>();
        } else {
            data = new ArrayList<>(prevData);
        }
        GithubRepositories githubRepositories = new GithubRepositories(data);
        githubRepositories.addListener(new Listener() {
            @Override
            public void onRepositoryAdded(GithubRepository repository) {
                if (updating) {
                    layout.updateItem(convertToItem(repository), 2);
                }
            }
        });
        sychronizeRepositories(githubRepositories);
        try {
            repositoryData = repositoryUpdater.getUpdateResult().getRepositories();
            searcher = new CacheSearcher(repositoryData);
        } catch (Exception e) {
            throw new PluginException(e);
        }
    }

    private void sychronizeRepositories(GithubRepositories githubRepositories) {
        repositoryUpdater.checkRemote(githubRepositories);
        repositoryUpdater.updateLocal();
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
            }

            @Override
            public void handle(InitializeEvent event) throws Exception {
                initUI();
                localRepositoryFile = getFileStoredInPluginDataDir("starred_repositories.json");
                starredRepositoryAPI = "https://api.github.com/users/" + username + "/starred?access_token=" + token;
                repositoryUpdater = new RepositoryUpdater(getExecutor(), localRepositoryFile, starredRepositoryAPI);
                IOUtils.createFileIfNotExists(localRepositoryFile);
                repositoryData = JSONUtils.parseArrFromLocalFile(localRepositoryFile, GithubRepository.class);
                if (repositoryData != null) {
                    searcher = new CacheSearcher(repositoryData);
                }
                ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
                scheduledExecutorService.scheduleAtFixedRate(() -> {
                    updateRepositories();
                }, 0, 5, TimeUnit.MINUTES);
            }
        });
    }

    @Override
    protected Mode registerModeHook(ModeManager modeManager) {
        return new GithubStarredMode(MODE, modeManager.getMode("input_mode"));
    }

}

