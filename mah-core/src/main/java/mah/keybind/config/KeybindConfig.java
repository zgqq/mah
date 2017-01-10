package mah.keybind.config;

/**
 * Created by zgq on 2017-01-09 09:31
 */
public class KeybindConfig {
    private String mode;
    private String bind;
    private String action;

    public String getBind() {
        return bind;
    }

    public void setBind(String bind) {
        this.bind = bind;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public void setMode(String mode) {
        this.mode = mode;
    }
}
