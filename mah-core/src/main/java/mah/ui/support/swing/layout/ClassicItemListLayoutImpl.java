package mah.ui.support.swing.layout;

import mah.ui.UIException;
import mah.ui.event.EventHandler;
import mah.ui.layout.ClassicItemListLayout;
import mah.ui.pane.item.Item;
import mah.ui.pane.item.ItemListPane;
import mah.ui.pane.item.ItemSelectedEvent;
import mah.ui.support.swing.pane.item.ItemListPaneFactoryImpl;
import mah.ui.theme.LayoutTheme;
import mah.ui.theme.ThemeManager;
import mah.ui.theme.Themeable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by zgq on 2017-01-08 13:51
 */
public class ClassicItemListLayoutImpl extends SwingAbstractClassicLayoutWrapper implements ClassicItemListLayout, SwingLayout, Themeable {
    private static final String NAME = "classic_item_list_layout";
    private List<EventHandler<? extends ItemSelectedEvent>> itemSelectedEventHandlers = new ArrayList<>();
    private final ItemListPaneFactoryImpl factory;
    private ItemListPane itemListPane;

    public ClassicItemListLayoutImpl() {
        super(ClassicAbstractLayoutImpl.instance());
        this.factory = new ItemListPaneFactoryImpl();
    }

    private void applyTheme() {
        LayoutTheme layoutTheme = ThemeManager.getInstance().getLayoutTheme(NAME);
        apply(layoutTheme);
    }

    @Override
    public void init() {
        super.init();
        applyTheme();
    }

    @Override
    public void updateItems(List<? extends Item> items) {
        applyTheme();
        this.itemListPane = factory.createItemListPane(items,itemSelectedEventHandlers);
        getLayout().updatePane(this.itemListPane);
    }

    @Override
    public void updateItems(Item... items) {
        if (items == null) {
            throw new UIException("The items should not be null in layout " + getName());
        }
        applyTheme();
        updateItems(Arrays.asList(items));
    }

    @Override
    public void updateItem(Item item, int num) {
        if (this.itemListPane == null) {
            throw new UIException("Unable to update item");
        }
        this.itemListPane.updateItem(item, num);
    }

    @Override
    public String getName() {
        return NAME;
    }


    @Override
    public void setOnItemSelected(EventHandler<? extends ItemSelectedEvent> event) {

    }
}
