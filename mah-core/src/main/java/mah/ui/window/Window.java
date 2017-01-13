package mah.ui.window;

import mah.ui.layout.Layout;

/**
 * Created by zgq on 17-1-8 11:24.
 */
public interface Window {

    void init();

    void show();

    boolean isShowing();

    boolean isFocused();

    void hide();

    void moveToRight();

    void centerOnScreen();

    void moveToLeft();

    void useDefaultLayoutAsCurrentLayout();

    void setCurrentLayout(Layout layout);

    Layout getCurrentLayout();

    Layout getDefaultLayout();

    void moveToCusorScreen();
}
