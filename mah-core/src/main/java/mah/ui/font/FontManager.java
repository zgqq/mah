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
package mah.ui.font;

import mah.app.ApplicationEvent;
import mah.app.config.Config;
import mah.app.config.XmlConfig;
import mah.ui.UiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zgq on 2/4/17.
 */
public class FontManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(FontManager.class);
    private static final FontManager INSTANCE = new FontManager();
    private BuiltinFont currentFont;
    private BuiltinFont defaultFont;
    private final Map<String, BuiltinFont> availbleFonts = new HashMap<>();

    private FontManager() {
        loadFont();
    }

    public void start(ApplicationEvent event) {
        final Config config = event.getConfig();
        if (config instanceof XmlConfig) {
            final XmlFontConfigParser parser = new XmlFontConfigParser(((XmlConfig) config).getDocument());
            final String font = parser.parseFont();
            this.currentFont = availbleFonts.get(font);
            if (currentFont == null) {
                currentFont = defaultFont;
            }
        }
    }

    private void loadFont() {
        registerFonts();
        listFont();
    }

    private void registerFonts() {
        final BuiltinFont.Builder zenhei = new BuiltinFont.Builder("wqy-zenhei.ttc", "normal", "WenQuanYi Zen Hei");
        final BuiltinFont.Builder comic = new BuiltinFont.Builder("comic.ttf", "comic", "Comic Sans MS");
        final List<BuiltinFont.Builder> fonts = Arrays.asList(zenhei, comic);
        final GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            for (BuiltinFont.Builder fontBuilder : fonts) {
                InputStream is = FontManager.class.getClassLoader().getResourceAsStream(fontBuilder.getPath());
                Font font = Font.createFont(Font.TRUETYPE_FONT, is);
                ge.registerFont(font);
                fontBuilder.font(font);
                availbleFonts.put(fontBuilder.getName(), fontBuilder.build());
            }
            defaultFont = availbleFonts.get("normal");
        } catch (Exception e) {
            throw new UiException(e);
        }
    }

    private void listFont() {
        final String[] fonts =
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (int i = 0; i < fonts.length; i++) {
            LOGGER.info("Found font :" + fonts[i]);
        }
    }

    public String getCurrentFontName() {
        return currentFont.getRealName();
    }

    public static FontManager getInstance() {
        return INSTANCE;
    }
}
