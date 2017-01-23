package mah.plugin.support.translation;

import mah.common.util.IOUtils;
import mah.common.util.StringUtils;
import mah.common.util.XMLUtils;
import mah.plugin.PluginException;
import mah.plugin.command.PluginCommandSupport;
import mah.plugin.config.XMLConfigurable;
import mah.plugin.support.translation.parser.WordParser;
import mah.plugin.support.translation.parser.YoudaoParser;
import mah.plugin.support.translation.repo.JSONUtils;
import mah.plugin.support.translation.repo.WordRepository;
import mah.ui.layout.ClassicItemListLayout;
import mah.ui.pane.item.FullItemImpl;
import mah.ui.pane.item.TextItemImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zgq on 16-12-24.
 */
public class TranslationCommand extends PluginCommandSupport implements XMLConfigurable {

    private ClassicItemListLayout layout;
    private InputStream translationIcon;
    private String keyfrom ;
    private String apiKey;
    private static final Logger logger = LoggerFactory.getLogger(TranslationCommand.class);
    private volatile WordRepository repository;
    private final WordParser parser = new YoudaoParser(JSONUtils.getJSONFactory());
    private String lastWord;

    public TranslationCommand() {
        init();
    }

    private void init() {
        addInitializeEventHandler(event -> {
            layout = getLayoutFactory().createClassicItemListLayout();
            layout.registerMode(new TranslationMode(), event1 -> event1.getMode().updateActionHandler(TranslationCommand.this));
            localWordReader();
        });
        addCommonFilterEventHandler(event -> {
            filterHook(event.getContent());
        });
        addTriggerEventHandler(event -> {
            triggerHook();
        });
    }

    private void localWordReader() {
        String pluginDataFile = "";
        try {
            pluginDataFile = getPluginDataDir("local_words.json");
            repository = new WordRepository(pluginDataFile);
            repository.init();
        } catch (Exception e) {
            if (e.getCause() instanceof FileNotFoundException) {
                try {
                    IOUtils.createFileIfNotExists(pluginDataFile);
                } catch (Exception e2) {
                    logger.error("Fail to create new file " + pluginDataFile, e2);
                }
            } else {
                logger.error("Fail to initialize repository", e);
            }
        }
    }


    private FullItemImpl createTranslationResult(String name, String description) {
        this.translationIcon = getInputStreamFromClasspath("translation/translate.png");
        FullItemImpl fullItem = new FullItemImpl.Builder(name).description(description).iconInputStream(translationIcon).build();
        return fullItem;
    }

    private FullItemImpl createTriggerResult() {
        return createTranslationResult("英汉互译", "Thanks to youdao api");
    }

    private void showConfigError() {
        TextItemImpl tip = new TextItemImpl.Builder("To use translation command," +
                "you are expected to provide own keyfrom and apikey "
        ).build();

        TextItemImpl tips2 = new TextItemImpl.Builder("You can go to http://fanyi.youdao.com/openapi.do to apply for them"
        ).build();

        layout.updateItems(tip,tips2);
    }

    private boolean checkConfig() {
        if (StringUtils.isEmpty(this.apiKey) || StringUtils.isEmpty(this.keyfrom)) {
            showConfigError();
            return false;
        }
        return true;
    }

    public void triggerHook() throws Exception {
        if(checkConfig()){
            layout.updateItems(createTriggerResult());
        }
    }

    @Override
    public String getName() {
        return "Translation";
    }

    public void filterHook(String content) throws Exception {
        if(checkConfig()){
            if (content.equals(" ")) {
                triggerHook();
            }
            String word = content;
            if (word.equals(this.lastWord)) {
                return;
            }
            this.lastWord = word;
            submit(new Queryer(keyfrom, apiKey, word));
        }
    }

    private Word parseWord(String content) {
        return parser.parseWord(content);
    }

    private String getLastWord() {
        return lastWord;
    }

    private void update(Word word) {
        List<String> translations = word.getTranslations();
        if (translations == null) {
            layout.updateItems(createTranslationResult("查询出错", "找不到这个词"));
            return;
        }
        List<String> explains = word.getExplains();
        String description = "";
        if (explains != null) {
            StringBuilder explainStr = new StringBuilder();
            for (Object explain : explains) {
                String expl = ((String) explain);
                explainStr.append(expl + ",");
            }
            explainStr.deleteCharAt(explainStr.length() - 1);
            description = explainStr.toString();
        }
        List<FullItemImpl> items = new ArrayList<>();
        items.add(createTranslationResult(buildDescription(translations), description));
        List<Word.MeaningPair> meaningPairs = word.getMeaningPairs();
        if (meaningPairs != null) {
            for (Word.MeaningPair meaningPair : meaningPairs) {
                items.add(createTranslationResult(meaningPair.getKey(), buildDescription(meaningPair.getValue())));
            }
        }
        layout.updateItems(items);
    }

    private String buildDescription(List<String> items) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            sb.append(items.get(i));
            if (i != items.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
    }


    private Word getWord(String word) {
        return repository.findWord(word);
    }

    @Override
    public void configure(Node node) throws Exception {
        if (node == null) {
            return;
        }
        String keyfrom = XMLUtils.getChildNodeText(node, "keyfrom");
        String apikey = XMLUtils.getChildNodeText(node, "apikey");
        this.keyfrom = keyfrom;
        this.apiKey = apikey;
    }

    public ClassicItemListLayout getLayout() {
        return layout;
    }


    class Queryer implements Runnable {

        private final String keyfrom;
        private final String apiKey;
        private final String word;

        public Queryer(String keyfrom, String apiKey, String word) {
            this.keyfrom = keyfrom;
            this.apiKey = apiKey;
            this.word = word;
        }


        private boolean currentWord() {
            return getLastWord().equals(word);
        }

        private void updateUI(Word word) {
            if (currentWord()) {
                update(word);
            }
        }

        private boolean qualifySaveWord() {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new PluginException(e);
            }
            return currentWord();

        }

        private void delayToUpdateWord(Word word) {
            if (!qualifySaveWord()) {
                return;
            }
            logger.info("save word {}", word);
            word.incrementQueryCount();
            word.updateQueryTime();
            repository.save();
            logger.info("saved word", word);
        }

        @Override
        public void run() {
            try {
                if (StringUtils.isEmpty(word)) {
                    return;
                }
                Word wordObj = getWord(word);
                if (wordObj != null) {
                    updateUI(wordObj);
                    delayToUpdateWord(wordObj);
                    return;
                }

                Thread.sleep(300);
                if (!currentWord()) {
                    return;
                }

                String apirURL = "http://fanyi.youdao.com/openapi.do";
                Map<String, String> params = new HashMap<>();
                params.put("keyfrom", keyfrom);
                params.put("key", apiKey);
                params.put("type", "data");
                params.put("doctype", "json");
                params.put("version", "1.1");
                params.put("q", this.word);

                StringBuilder paramsStr = new StringBuilder();
                params.forEach((s, s2) -> {
                    try {
                        paramsStr.append(s + "=" + URLEncoder.encode(s2, "UTF-8") + "&");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                });
                paramsStr.deleteCharAt(paramsStr.length() - 1);
                HttpURLConnection urlConnection = null;
                URL url = new URL(apirURL + "?" + paramsStr.toString());
                logger.debug("fetch " + url);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.connect();
                InputStream inputStream = urlConnection.getInputStream();
                String content = IOUtils.toString(inputStream);
                wordObj = parseWord(content);
                updateUI(wordObj);
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (qualifySaveWord()) {
                    if (wordObj.getExplains() != null) {
                        repository.addWord(wordObj);
                    }
                }
            } catch (Exception e) {
                showFailure();
                logger.error("query fail", e);
            }
        }

        private void showFailure() {
            layout.updateItems(new FullItemImpl.Builder("查询失败，请检查网络是否连接").build());
        }
    }

}

