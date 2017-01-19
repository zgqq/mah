package mah.ui.support.swing.layout;

import mah.mode.Mode;
import mah.ui.layout.ModeEvent;
import mah.ui.layout.ModeListener;

import java.util.List;

/**
 * Created by zgq on 2017-01-18 09:23
 */
public final class LayoutUtils {
    private LayoutUtils() {}

    public static void triggerMode(Mode mode, List<ModeListener> modeListeners) {
        if (mode != null) {
            mode.trigger();
            ModeEvent modeEvent = new ModeEvent();
            modeEvent.setMode(mode);
            for (ModeListener modeListener : modeListeners) {
                modeListener.modeTriggered(modeEvent);
            }
        }
    }
}
