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
package mah.ui.support.swing.util;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by zgq on 2/9/17.
 */
public final class ImageUtils {
    private ImageUtils() {}

    public static ImageIcon scale(InputStream inputStream, int width, int height) throws IOException {
        BufferedImage newIcon = ImageIO.read(inputStream);
        ImageIcon imageIcon = new ImageIcon(newIcon); // load the image to a imageIcon
        Image image = imageIcon.getImage(); // transform it
        // scale it the smooth way
        Image newimg = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        imageIcon = new ImageIcon(newimg);  // transform it back
        return imageIcon;
    }
}

