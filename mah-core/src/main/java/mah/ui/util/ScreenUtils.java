/**
 * MIT License
 *
 * Copyright (c) 2017 zgqq
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package mah.ui.util;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zgq on 2017-01-12 14:54
 */
public final class ScreenUtils {
    private ScreenUtils() {}

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
        GraphicsDevice[] lstGDs = ge.getScreenDevices();
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
        GraphicsDevice[] lstGDs = ge.getScreenDevices();
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
