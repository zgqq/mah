package mah.openapi.ui.layout;

import mah.ui.layout.ClassicPostLayout;

/**
 * Created by zgq on 2017-01-09 17:20
 */
public interface LayoutFactory {

    mah.ui.layout.ClassicItemListLayout createClassicItemListLayout();

    ClassicPostLayout createClassicPostLayout();
}

