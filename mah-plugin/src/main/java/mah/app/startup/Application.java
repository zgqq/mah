package mah.app.startup;

import mah.app.ApplicationManager;
import mah.ui.support.swing.theme.Themes;
import mah.ui.theme.ThemeManager;

/**
 * Created by zgq on 2017-01-08 11:33
 */
public class Application {

    public static void main(String[] args) {

        ThemeManager.getInstance().addTheme(Themes.DARK.getTheme());
        ThemeManager.getInstance().setCurrentTheme("dark");
        ApplicationManager.getInstance().start();
//        ClassicItemListLayout classicItemListLayout = new ClassicItemListLayout();
//        List<FullItemImpl> itemList = new ArrayList<>();
//        itemList.add(new FullItemImpl.Builder("ok").description("ss").iconInputStream(Application.class.getResourceAsStream("/translation/translate.png")).build());
//        itemList.add(new FullItemImpl.Builder("ok").description("ss").iconInputStream(Application.class.getResourceAsStream("/translation/translate.png")).build());
//        itemList.add(new FullItemImpl.Builder("ok").description("ss").iconInputStream(Application.class.getResourceAsStream("/translation/translate.png")).build());
//        itemList.add(new FullItemImpl.Builder("ok2").iconInputStream(Application.class.getResourceAsStream("/translation/translate.png")).build());
//        classicItemListLayout.updateItems(itemList);
    }

}
