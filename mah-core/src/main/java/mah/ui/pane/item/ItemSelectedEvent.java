package mah.ui.pane.item;

/**
 * Created by zgq on 2017-01-10 15:07
 */
public class ItemSelectedEvent {

    private Item item;

    public ItemSelectedEvent(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }
}
