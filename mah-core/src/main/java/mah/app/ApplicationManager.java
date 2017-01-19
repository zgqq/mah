package mah.app;

import mah.app.config.Config;
import mah.app.config.XMLConfigParser;
import mah.command.CommandManager;
import mah.common.util.IOUtils;
import mah.keybind.KeybindManager;
import mah.mode.ModeManager;
import mah.plugin.PluginManager;
import mah.ui.UIManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-08 11:35
 */
public class ApplicationManager {

    private static final ApplicationManager INSTANCE = new ApplicationManager();
    private static final List<ApplicationListener> APPLICATION_LISTENER = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(ApplicationManager.class);

    static {
        APPLICATION_LISTENER.add(PluginManager.getInstance());
        APPLICATION_LISTENER.add(ModeManager.getInstance());
        APPLICATION_LISTENER.add(KeybindManager.getInstance());
        APPLICATION_LISTENER.add(CommandManager.getInstance());
        APPLICATION_LISTENER.add(UIManager.getInstance());
    }

    private ApplicationManager() {}

    public void start() {
        try {
            init();
            XMLConfigParser configParser = new XMLConfigParser(configPath);
            Config config = configParser.parse();
            ApplicationEvent applicationEvent = new ApplicationEvent(config);
            for (ApplicationListener applicationListener : APPLICATION_LISTENER) {
                applicationListener.start(applicationEvent);
            }
            for (ApplicationListener applicationListener : APPLICATION_LISTENER) {
                applicationListener.afterStart(applicationEvent);
            }
        } catch (Exception e) {
            if (e instanceof ApplicationException) {
                ApplicationException applicationException = (ApplicationException) e;
                throw applicationException;
            }
            throw new ApplicationException(e);
        }

    }

    private void init() throws Exception {
        initApplicationDir();
        initPluginDir();
        initConfig();
    }

    private String applicationDir;
    private String configPath;
    private File pluginDir;
    private File pluginDataDir;

    private void initPluginDir() {
        pluginDir = new File(applicationDir + File.separator + "plugins");
        if (!pluginDir.exists()) {
            pluginDir.mkdirs();
        }
        pluginDataDir = new File(applicationDir + File.separator + "data");
        if (!pluginDataDir.exists()) {
            pluginDataDir.mkdirs();
        }
    }

    private void initConfig() throws IOException {
        configPath = applicationDir + File.separator + "conf.xml";
        File file = new File(configPath);
        if (!file.exists()) {
            file.createNewFile();
            IOUtils.writeToFile(configPath, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<config>\n</config>");
        }
    }

    private void initApplicationDir() {
        applicationDir = System.getProperty("user.home") + File.separator + ".config" + File.separator + "mah";
        File file = new File(applicationDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public File getPluginDataDir() {
        return pluginDataDir;
    }


    public File getPluginDir() {
        return pluginDir;
    }


    public static ApplicationManager getInstance() {
        return INSTANCE;
    }

    public void shutdown() {
        try {
            for (ApplicationListener applicationListener : APPLICATION_LISTENER) {
                applicationListener.shutdown();
            }
            System.exit(0);
        } catch (Exception e) {
            logger.error("fail to shutdown", e);
            System.exit(-1);
        }
    }
}
