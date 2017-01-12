package mah.ui.support.swing.theme;

import mah.ui.theme.LayoutTheme;
import mah.ui.theme.Theme;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zgq on 2017-01-08 19:17
 */
public enum Themes {

    DARK("#333333","#616161","#222222","#ffffff","#000000","#DAA520");

    private Theme theme;

    Themes(String backgroundColor,String inputBackgroundColor,String pendingBackgroundColor,String fontColor,String highlightForegroundColor,String highlightColor) {

        Map<String, String> inputProperties = new HashMap<>();
        inputProperties.put("input-field-font-color",fontColor);
//        inputProperties.put("input-pane-background-color",backgroundColor);
        inputProperties.put("input-field-background-color",inputBackgroundColor);
        inputProperties.put("background-color",backgroundColor);
//        inputProperties.put("layout-background-color", backgroundColor);

        Map<String, LayoutTheme> layoutThemes = new HashMap<>();
        Map<String, String> properties = new HashMap<>();

        properties.putAll(inputProperties);

//        properties.put("item-list-background-color",backgroundColor);

        properties.put("item-pane-background-color",backgroundColor);
        properties.put("middle-container-background-color",backgroundColor);
        properties.put("text-background-color",backgroundColor);
        properties.put("text-foreground-color",fontColor);
        properties.put("num-font-color", fontColor);

        properties.put("text-highlight-foreground-color",highlightForegroundColor);
        properties.put("text-highlight-background-color",highlightColor);

        properties.put("pending-background-color",pendingBackgroundColor);

        LayoutTheme layoutTheme = new LayoutThemeImpl("classic_item_list_layout",properties);
        layoutThemes.put(layoutTheme.getName(),layoutTheme);

        LayoutTheme defaultLayoutTheme = new LayoutThemeImpl("default_layout",inputProperties);
        layoutThemes.put(defaultLayoutTheme.getName(),defaultLayoutTheme);

        theme = new Theme("dark",layoutThemes);
    }

    public Theme getTheme() {
        return theme;
    }
}
