package mah.plugin.support.orgnote;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import mah.action.AbstractAction;
import mah.action.ActionEvent;
import mah.mode.AbstractMode;
import mah.plugin.PluginException;
import mah.plugin.command.PluginCommandSupport;
import mah.plugin.support.orgnote.config.Config;
import mah.plugin.support.orgnote.source.Orgmode2;
import mah.plugin.support.orgnote.util.IOUtil;
import mah.ui.layout.ClassicPostLayout;
import mah.ui.pane.post.PostImpl;
import mah.ui.window.Window;
import mah.ui.window.WindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by zgq on 16-12-19.
 */
public class ReviewNoteCommand extends PluginCommandSupport {

    private static final Logger logger = LoggerFactory.getLogger(ReviewNoteCommand.class);
    private ClassicPostLayout layout;

    public ReviewNoteCommand() {
        init();
    }

    private void init() {
        addInitializeEventHandler(event -> {
            layout = getLayoutFactory().createClassicPostLayout();
            layout.registerMode(new OrgnoteReviewMode(),event1 -> event1.getMode().updateActionHandler(ReviewNoteCommand.this));
        });
        addTriggerEventHandler(event -> {
            trigger();
        });
    }


    private static JSONObject selectNode(List<JSONObject> list, Config config) {
        for (int i = config.getNodeIndex(); i < list.size(); i++) {
            JSONObject node = list.get(i);
            Long lastReviewTime = node.getLong("lastReviewTime");
            if (lastReviewTime == null || lastReviewTime == 0) {
                config.setNodeIndex(i);
                return node;
            }
            long lrTime = lastReviewTime;
            long ntime = Calendar.getInstance().getTimeInMillis();
            int familiarDegree = node.getIntValue("familiarDegree");
            if (ntime >= (familiarDegree * 24 * 3600) + lrTime) {
                config.setNodeIndex(i);
                return node;
            }
        }
        return null;
    }

    private String getDataDir() {
        return getPluginDataDir("import");
    }

    private String getImportDir() {
        return getPluginDataDir("import");
    }


    public int checkData(Config config) {
        //Import
        File importDataDir = new File(getImportDir());
        if (!importDataDir.exists()) {
            importDataDir.mkdirs();
            return 0;
        }
        if (importDataDir.isDirectory()) {
            int importQuantity = 0;
            for (File dataFile : importDataDir.listFiles()) {
                String filename = IOUtil.getFilename(dataFile.getName());
                if (!config.contains(filename + ".json")) {
                    String content;
                    try {
                        content = Orgmode2.toJson(dataFile.getAbsolutePath());
                        String filepath = getDataDir() + filename + ".json";
                        IOUtil.createFileIfNotExists(filepath);
                        IOUtil.writeToFile(filepath, content);
                        config.addDataFilename(filename + ".json");
                        ++importQuantity;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            return importQuantity;
        }
        return 0;
    }

    private String getConfigFile() {
        return getFileStoredInPluginDataDir("config.json");
    }

    private Config readConfig() {
        Config config;
        try {
            String con = IOUtil.toString(getConfigFile());
            config = JSONObject.parseObject(con, Config.class);
            if (config == null) {
                config = new Config();
            }
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                try {
                    IOUtil.createFileIfNotExists(getConfigFile());
                    config = new Config();
                } catch (IOException e1) {
                    throw new RuntimeException("Run error!", e1);
                }
            } else {
                throw new RuntimeException("Run error!", e);
            }
        }
        return config;
    }

    private void createDirectoryIfNotExists(String dir) {
        File dirObj = new File(dir);
        if (!dirObj.exists()) {
            dirObj.mkdirs();
        }
    }

    private void checkInstallDir() {
        createDirectoryIfNotExists(getDataDir());
        createDirectoryIfNotExists(getImportDir());
    }

    private void trigger() {
        synchronized (this) {
            logger.info("trigger note command");
            checkInstallDir();
            nextNode();
        }
    }

    public void nextNode() {
        Config config = readConfig();
        checkData(config);
        JSONObject note;
        List<JSONObject> list = new ArrayList<>();
        if (config.dataEmpty()) {
            note = new JSONObject();
            note.put("title", "没有数据");
            note.put("content", "没有数据");
        } else {
            String reviewContent = null;
            try {
                reviewContent = IOUtil.toString(getDataDir() + config.getCurrentFilename());
            } catch (IOException e) {
                throw new PluginException(e);
            }
            list = JSON.parseObject(reviewContent, List.class);
            note = selectNode(list, config);
            if (note == null) {
                note = new JSONObject();
                note.put("title", "No review item available!");
            }
        }
        Config.setSource(config, list);
        currentNode = note;
        JSONArray contentLine = note.getJSONArray("contentLines");
        final StringBuilder contentsb = new StringBuilder();
        if (contentLine != null) {
            contentLine.forEach(str->contentsb.append(str).append("\n"));
        }
        PostImpl post = new PostImpl.Builder(note.getString("title"), contentsb.toString()).build();
        layout.setPost(post);
        Config.update();
    }

    private JSONObject currentNode;

    public JSONObject getCurrentNote() {
        return currentNode;
    }

    @Override
    public String getName() {
        return "ReviewNote";
    }


    public static class OrgnoteReviewMode extends AbstractMode {

        public static final String NAME = "orgnote_review_mode";

        public OrgnoteReviewMode() {
            super(NAME);
        }

        @Override
        public void init() {
            registerAction(new Quit("Quit"));
            registerAction(new Forget("Forget"));
            registerAction(new Remember("Remember"));
        }
    }

    public abstract static class ReviewAction extends AbstractAction {
        public ReviewAction(String name) {
            super(name,ReviewNoteCommand.class);
        }
    }


    public static class Quit extends ReviewAction {

        public Quit(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ReviewNoteCommand source = (ReviewNoteCommand) actionEvent.getSource();
            JSONObject currentNote = source.getCurrentNote();
            int quitCount = currentNote.getIntValue("quitCount");
            currentNote.put("quitCount", quitCount + 1);
            Config.updateReviewList();
            Window currentWindow = WindowManager.getInstance().getCurrentWindow();
            currentWindow.useDefaultLayoutAsCurrentLayout();
        }
    }

    public static class Forget extends ReviewAction {
        public Forget(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ReviewNoteCommand source = (ReviewNoteCommand) actionEvent.getSource();
            JSONObject jsonObject = source.getCurrentNote();
            int forgetCount = jsonObject.getIntValue("forgetCount");
            int familiarDegree = 0;
            jsonObject.put("forgetCount", forgetCount + 1);
            jsonObject.put("familiarDegree", familiarDegree);
            jsonObject.put("lastReviewTime", new Date());
            Config.updateReviewList();
            source.nextNode();
        }
    }


    public static class Remember extends ReviewAction {

        public Remember(String name) {
            super(name);
        }

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ReviewNoteCommand source = (ReviewNoteCommand) actionEvent.getSource();
            JSONObject jsonObject = source.getCurrentNote();
            int familiarDegree = jsonObject.getIntValue("familiarDegree");
            int rememberCount = jsonObject.getIntValue("rememberCount");
            if (familiarDegree == 0) {
                familiarDegree = 10;
            }
            familiarDegree = (int) (familiarDegree * 1.3);
            jsonObject.put("rememberCount", rememberCount + 1);
            jsonObject.put("familiarDegree", familiarDegree);
            jsonObject.put("lastReviewTime", new Date());
            Config.updateReviewList();
            source.nextNode();
        }
    }

}
