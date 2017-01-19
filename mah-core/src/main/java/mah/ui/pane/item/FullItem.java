package mah.ui.pane.item;

import mah.ui.pane.Text;

import java.io.InputStream;

/**
 * Created by zgq on 2017-01-11 10:57
 */
public interface FullItem extends Item {

    InputStream getIconInputStream();

    Text getContent();

    Text getDescription();

}
