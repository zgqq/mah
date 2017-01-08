package mah.ui.support.swing.theme;

import mah.ui.theme.LayoutTheme;
import mah.ui.theme.Theme;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zgq on 2017-01-08 19:17
 */
public enum Themes {

    DARK("#333333","#ffffff");

    private Theme theme;

    Themes(String color,String fontColor) {
        Map<String, LayoutTheme> layoutThemes = new HashMap<>();
        Map<String, String> properties = new HashMap<>();


        properties.put("input-field-font-color",fontColor);
        properties.put("input-pane-background-color",color);
        properties.put("input-field-background-color",color);

        properties.put("item-list-background-color",color);

        properties.put("item-pane-background-color",color);
        properties.put("content-background-color",color);
        properties.put("content-font-color",fontColor);
        properties.put("description-background-color",color);
        properties.put("description-font-color",fontColor);
        properties.put("num-font-color", fontColor);

        properties.put("layout-background-color", color);

        LayoutTheme layoutTheme = new LayoutThemeImpl("classic_item_list_layout",properties);
        layoutThemes.put(layoutTheme.getName(),layoutTheme);
        theme = new Theme("dark",layoutThemes);
    }

    public Theme getTheme() {
        return theme;
    }
}
