package mah.ui.pane.post;

import mah.ui.pane.Text;

import java.util.List;

/**
 * Created by zgq on 2017-01-17 14:39
 */
public interface Post {

    Text getTitle();

    Text getContent();

    List<Text> getAdditions();

}
