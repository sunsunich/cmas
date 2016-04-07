package org.cmas.util.font;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created on Apr 06, 2016
 *
 * @author Alexander Petukhov
 */
public final class FontUtils {

    private static final Logger LOG = LoggerFactory.getLogger(FontUtils.class);

    private static final Map<String, Font> FONTS = new HashMap<>();
    private static final Font SERIF_FONT = new Font("serif", Font.PLAIN, 24);

    private FontUtils() {
    }

    public static synchronized Font getFont(String name, InputStream is) {
        if (name == null) {
            return SERIF_FONT;
        }
        try {
            // load from a cache map, if exists
            if (FONTS.containsKey(name)) {
                return FONTS.get(name);
            }
            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment ge = GraphicsEnvironment
                    .getLocalGraphicsEnvironment();
            ge.registerFont(font);

            FONTS.put(name, font);
            return font;
        } catch (Exception ex) {
            LOG.error(name + "not loaded.  Using serif font.", ex);
            return SERIF_FONT;
        }
    }
}
