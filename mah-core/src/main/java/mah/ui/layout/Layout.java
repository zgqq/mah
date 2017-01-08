package mah.ui.layout;

/**
 * Created by zgq on 2017-01-08 11:54
 */
public interface Layout {

    default String getName(){
        throw new UnsupportedOperationException("Unable to get layout name");
    }

}
