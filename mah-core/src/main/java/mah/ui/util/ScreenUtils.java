package mah.ui.util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-12 14:54
 */
public final class ScreenUtils {

    private ScreenUtils() {

    }


    public static Rectangle getScreenBoundsAt(Point pos) {
        GraphicsDevice gd = getGraphicsDeviceAt(pos);
        Rectangle bounds = null;
        if (gd != null) {
            bounds = gd.getDefaultConfiguration().getBounds();
        }
        return bounds;
    }


    public List<Rectangle> getScreenBounds() {
        List<Rectangle> bounds = new ArrayList<>(25);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice lstGDs[] = ge.getScreenDevices();
        for (GraphicsDevice gd : lstGDs) {
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            Rectangle screenBounds = gc.getBounds();
            bounds.add(screenBounds);
        }

        return bounds;
    }

    public static Rectangle getDeviceBounds(GraphicsDevice device) {
        GraphicsConfiguration gc = device.getDefaultConfiguration();
        Rectangle bounds = gc.getBounds();
        return bounds;
    }

    public static GraphicsDevice getGraphicsDeviceAt(Point pos) {
        GraphicsDevice device = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice lstGDs[] = ge.getScreenDevices();

        ArrayList<GraphicsDevice> lstDevices = new ArrayList<>(lstGDs.length);
        for (GraphicsDevice gd : lstGDs) {
            Rectangle screenBounds = getDeviceBounds(gd);
            if (screenBounds.contains(pos)) {
                lstDevices.add(gd);
            }
        }
        if (!lstDevices.isEmpty()) {
            device = lstDevices.get(0);
        }
        return device;

    }


}
