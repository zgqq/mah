package mah.keybind.config;

/**
 * Created by zgq on 2017-01-09 09:31
 */
public final class KeybindConfig {
    private final String mode;
    private final String bind;
    private final String action;

    public KeybindConfig(String mode, String bind, String action) {
        this.mode = mode;
        this.bind = bind;
        this.action = action;
    }

    public String getBind() {
        return bind;
    }

    public String getAction() {
        return action;
    }


    public String getMode() {
        return mode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeybindConfig keybind = (KeybindConfig) o;

        if (!bind.equals(keybind.bind)) return false;
        return action.equals(keybind.action);

    }

    @Override
    public int hashCode() {
        int result = bind.hashCode();
        result = 31 * result + action.hashCode();
        return result;
    }

}
