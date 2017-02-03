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
package mah.app;

import mah.app.config.Config;
import mah.app.config.XmlConfigParser;
import mah.command.CommandManager;
import mah.common.util.IoUtils;
import mah.keybind.KeybindManager;
import mah.mode.ModeManager;
import mah.plugin.PluginManager;
import mah.ui.UiManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-08 11:35
 */
public class ApplicationManager {
    private static final ApplicationManager INSTANCE = new ApplicationManager();
    private static final List<ApplicationListener> APPLICATION_LISTENER = new ArrayList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationManager.class);

    static {
        APPLICATION_LISTENER.add(PluginManager.getInstance());
        APPLICATION_LISTENER.add(ModeManager.getInstance());
        APPLICATION_LISTENER.add(KeybindManager.getInstance());
        APPLICATION_LISTENER.add(CommandManager.getInstance());
        APPLICATION_LISTENER.add(UiManager.getInstance());
    }

    private ApplicationManager() {}

    public void start() {
        try {
            init();
            XmlConfigParser configParser = new XmlConfigParser(configPath);
            Config config = configParser.parse();
            ApplicationEvent applicationEvent = new ApplicationEvent(config);
            for (ApplicationListener applicationListener : APPLICATION_LISTENER) {
                applicationListener.start(applicationEvent);
            }
            for (ApplicationListener applicationListener : APPLICATION_LISTENER) {
                applicationListener.afterStart(applicationEvent);
            }
        } catch (Exception e) {
            LOGGER.error("application failed to startup", e);
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
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("conf.xml");
            IoUtils.writeToFile(configPath,  inputStream);
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
            LOGGER.error("fail to shutdown", e);
            System.exit(-1);
        }
    }
}
