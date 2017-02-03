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
package mah.ui.theme;

import mah.app.ApplicationEvent;
import mah.app.config.Config;
import mah.app.config.XmlConfig;
import mah.ui.layout.Layout;
import mah.ui.support.swing.theme.Themes;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zgq on 2017-01-08 17:28
 */
public class ThemeManager {
    private final Map<String, Theme> themes = new HashMap<>();
    private static final ThemeManager INSTANCE = new ThemeManager();
    private static final String DEFAULT_THEME = "default";
    private String currentTheme;

    private ThemeManager() {
        addThemes();
    }

    private void addThemes() {
        addTheme(Themes.DARK.getTheme());
    }

    public final void start(ApplicationEvent event) {
        Config config = event.getConfig();
        if (config instanceof XmlConfig) {
            XmlThemeConfigParser parser = new XmlThemeConfigParser(((XmlConfig) config).getDocument());
            this.currentTheme = parser.parseTheme();
        }
    }

    public void addTheme(Theme theme) {
        themes.put(theme.getName(), theme);
    }

    @Nullable
    public LayoutTheme getLayoutTheme(String layoutName) {
        Theme theme = themes.get(currentTheme);
        if (theme == null) {
            Theme defaultTheme = themes.get(DEFAULT_THEME);
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
