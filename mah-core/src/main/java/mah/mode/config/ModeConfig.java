package mah.mode.config;

import mah.keybind.config.KeybindConfig;

import java.util.HashSet;
import java.util.List;

/**
 * Created by zgq on 2017-01-09 13:44
 */
public class ModeConfig {

    private String parent;
    private String name;
    private ModeConfig parentMode;
    private List<KeybindConfig> keybinds;
    private HashSet<KeybindConfig> caches;

    public HashSet<KeybindConfig> getKeybinds() {
        if (caches != null) {
            return caches;
        }
        HashSet<KeybindConfig> allKeybinds = new HashSet<>();
        if (parentMode != null) {
            HashSet<KeybindConfig> keybinds = parentMode.getKeybinds();
            allKeybinds.addAll(keybinds);
        }
        allKeybinds.addAll(keybinds);
        caches = allKeybinds;
        return allKeybinds;
    }

    public void setKeybinds(List<KeybindConfig> keybinds) {
        this.keybinds = keybinds;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ModeConfig getParentMode() {
        return parentMode;
    }

    public void setParentMode(ModeConfig parentMode) {
        this.parentMode = parentMode;
    }
}
