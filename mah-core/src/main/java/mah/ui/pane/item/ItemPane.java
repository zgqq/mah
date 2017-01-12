package mah.ui.pane.item;

/**
 * Created by zgq on 2017-01-08 14:00
 */
public interface ItemPane<T extends Item>{

    T getItem();

    void pending();

    void reset(T item,int num);

    @Deprecated
    void reset(T item);

    void unpending();
}
