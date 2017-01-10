package mah.ui.layout;

import mah.ui.pane.Pane;
import mah.ui.pane.input.InputPaneProvider;

/**
 * Created by zgq on 2017-01-08 11:55
 */
public interface ClassicAbstractLayout extends Layout,InputPaneProvider{

    void updatePane(Pane pane);

}
