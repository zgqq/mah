package mah.ui.support.swing.theme;

import mah.ui.UIException;
import mah.ui.theme.LayoutTheme;

import java.util.Map;

/**
 * Created by zgq on 2017-01-08 17:31
 */
public class LayoutThemeImpl implements LayoutTheme{

    private final String name;
    private final Map<String,String> properties;

    public LayoutThemeImpl(String name, Map<String, String> properties) {
        this.name = name;
        this.properties = properties;
    }

    public String findProperty(String key) {
        String property = properties.get(key);
        if (property == null) {
            throw new UIException("Not found property " + key);
        }
        return property;
    }

    @Override
    public String getName() {
        return name;
    }
}
