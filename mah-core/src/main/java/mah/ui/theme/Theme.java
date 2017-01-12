package mah.ui.theme;

import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * Created by zgq on 2017-01-08 17:26
 */
public final class Theme {

    private final String name;
    private final Map<String, LayoutTheme> nameToLayout;

    public Theme(String name, Map<String, LayoutTheme> nameToLayout) {
        this.name = name;
        this.nameToLayout = nameToLayout;
    }

    @Nullable
    public LayoutTheme findLayoutTheme(String name) {
        return nameToLayout.get(name);
    }

    public String getName() {
        return name;
    }

}
