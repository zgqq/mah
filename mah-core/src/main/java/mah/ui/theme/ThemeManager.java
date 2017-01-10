package mah.ui.theme;

import mah.ui.layout.Layout;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zgq on 2017-01-08 17:28
 */
public class ThemeManager {

    private final Map<String, Theme> NAME_THEMES = new HashMap<>();
    private static final ThemeManager INSTANCE = new ThemeManager();
    private static final String DEFAULT_THEME = "default";
    private String currentTheme;

    private ThemeManager() {

    }

    public void addTheme(Theme theme) {
        NAME_THEMES.put(theme.getName(), theme);
    }

    @Nullable
    public LayoutTheme getLayoutTheme(String layoutName) {
        Theme theme = NAME_THEMES.get(currentTheme);
        if (theme == null) {
            Theme defaultTheme = NAME_THEMES.get(DEFAULT_THEME);
            if (defaultTheme == null) {
                return null;
            }
            return defaultTheme.findLayoutTheme(layoutName);
        }
        return theme.findLayoutTheme(layoutName);
    }

    public void applyTheme(Layout layout) {
        LayoutTheme layoutTheme = getLayoutTheme(layout.getName());
        if (layout instanceof Themeable) {
            Themeable themeable = (Themeable) layout;
            themeable.apply(layoutTheme);
        }
    }

    public void setCurrentTheme(String currentTheme) {
        this.currentTheme = currentTheme;
    }

    public static ThemeManager getInstance() {
        return INSTANCE;
    }


}
