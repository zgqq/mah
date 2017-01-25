package mah.keybind.config;

/**
 * Created by zgq on 2017-01-25 21:48
 */
public final class GlobalKeybindConfig {

    private final String listen;
    private final String consume;
    private final String action;

    public GlobalKeybindConfig(String listen, String consume, String action) {
        if (listen == null || consume == null || action == null) {
            throw new NullPointerException();
        }
        this.listen = listen;
        this.consume = consume;
        this.action = action;
    }

    public String getListen() {
        return listen;
    }

    public String getConsume() {
        return consume;
    }

    public String getAction() {
        return action;
    }
}
