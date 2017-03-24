package mah.ui.layout;

import mah.event.EventHandler;
import mah.ui.input.InputConfirmedEvent;
import mah.ui.pane.input.InputPaneProvider;

/**
 * Created by zgq on 17-3-24.
 */
public interface InputLayout extends Layout,InputPaneProvider {
    void fireInputConfirmedEvent(InputConfirmedEvent event);

    void setOnInputConfirmed(EventHandler<? extends InputConfirmedEvent> eventHandler);
}
