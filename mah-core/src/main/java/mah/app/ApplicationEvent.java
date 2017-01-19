package mah.app;

import mah.app.config.Config;

/**
 * Created by zgq on 2017-01-08 11:36
 */
public class ApplicationEvent {

    private final Config config;

    public ApplicationEvent(Config config) {
        this.config = config;
    }

    public Config getConfig() {
        return config;
    }

}
