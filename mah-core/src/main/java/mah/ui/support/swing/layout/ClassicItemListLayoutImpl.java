package mah.ui.support.swing.layout;

import mah.ui.layout.ClassicItemListLayout;
import mah.ui.pane.item.Item;
import mah.ui.pane.item.ItemListPane;
import mah.ui.support.swing.pane.item.ItemListPaneFactoryImpl;
import mah.ui.theme.LayoutTheme;
import mah.ui.theme.ThemeManager;
import mah.ui.theme.Themeable;

import javax.swing.*;
import java.util.List;

/**
 * Created by zgq on 2017-01-08 13:51
 */
public class ClassicItemListLayoutImpl implements ClassicItemListLayout, SwingLayout, Themeable {

    private static final String NAME = "classic_item_list_layout";
    private final AbstractClassicLayoutImpl layout;
    private final ItemListPaneFactoryImpl factory;

    public ClassicItemListLayoutImpl() {
        this.layout = new AbstractClassicLayoutImpl();
        this.factory = new ItemListPaneFactoryImpl();
        init();
    }

    private void init() {
        LayoutTheme layoutTheme = ThemeManager.getInstance().getLayoutTheme(NAME);
        apply(layoutTheme);
    }

    @Override
    public void apply(LayoutTheme theme) {
        Themeable themeable = this.layout;
        themeable.apply(theme);
    }

    @Override
    public JPanel getPanel() {
        return layout.getPanel();
    }

    @Override
    public void updateItems(List<? extends Item> items) {
        ItemListPane itemListPane = factory.createItemListPane(items);
        layout.updatePane(itemListPane);
    }


    @Override
    public String getName() {
        return NAME;
    }
}
