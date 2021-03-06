package org.cmas.util.text;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Map;

public final class StringUtil {
    // Paranoid instantiation prohibition
    private StringUtil() {}

    // Префикс URL для сайтов
    public static final String WWW = "www.";
    public static final int MAXL = 255;

    private static final String [][] trans = {
             {"Й", "J"}, {"Ц", "TS"}, {"У", "U"},  {"К", "K"},   {"Е", "E"},
             {"Н", "N"}, {"Г", "G"},  {"Ш", "SH"}, {"Щ", "SCH"}, {"З", "Z"},
             {"Х", "H"}, {"Ъ", "'"},  {"Ф", "F"},  {"Ы", "Y"},   {"В", "V"},
             {"А", "A"}, {"П", "P"},  {"Р", "R"},  {"О", "O"},   {"Л", "L"},
             {"Д", "D"}, {"Ж", "ZH"}, {"Э", "E"},  {"Я", "YA"},  {"Ч", "CH"},
             {"С", "S"}, {"М", "M"},  {"И", "I"},  {"Т", "T"},   {"Ь", "'"},
             {"Б", "B"}, {"Ю", "YU"},

             {"й", "j"}, {"ц", "ts"}, {"у", "u"},  {"к", "k"},   {"е", "e"},
             {"н", "n"}, {"г", "g"},  {"ш", "sh"}, {"щ", "sch"}, {"з", "z"},
             {"х", "h"}, {"ъ", "'"},  {"ф", "f"},  {"ы", "y"},   {"в", "v"},
             {"а", "a"}, {"п", "p"},  {"р", "r"},  {"о", "o"},   {"л", "l"},
             {"д", "d"}, {"ж", "zh"}, {"э", "e"},  {"я", "ya"},  {"ч", "ch"},
             {"с", "s"}, {"м", "m"},  {"и", "i"},  {"т", "t"},   {"ь", "'"},
             {"б", "b"}, {"ю", "yu"}};

    public static boolean isEmpty(@Nullable String str) {
        return str == null || "".equals(str);
    }

    /**
     * Превышен ли лимит на длину строки?
     *
     * @param str String (may be null)
     * @return Whether the given string is of the valid length
     */
    public static boolean isValidLength(@Nullable String str) {
        return isValidLength(str, MAXL);
    }

    public static boolean isValidLength(String str, int size) {
        if (isEmpty(str)) {
            return true;
        }
        return str.length() <= size;
    }

    /**
     * Обрезает строку, если она длинее 255 символов
     * @param str String (may be null)
     * @return Truncated string
     */
    @Nullable
    public static String truncate(@Nullable String str) {
        if (isValidLength(str)) {
            return str;
        } else {
            return str.substring(0, MAXL);
        }
    }

    public static String rus2Translit(String in) {
        StringBuffer out = new StringBuffer();
        if (in != null && in.length() > 0) {
            for (int i=0; i < in.length(); i++) {
                String c = in.substring(i, i + 1);
                for(int j=0; j < trans.length; j++){
                    if(c.equals(trans[j][0])){
                        c = trans[j][1];
                        break;
                    }
                }
                out.append(c);
            }
        }
        return out.toString();
    }

    /**
     * Сравнивает строки.
     * Возможно нулевые
     * @param str source
     * @param anStr source
     * @return
     */
    public static int compareNullStr(String str, String anStr) {
        if (str == null) {
            return 0;
        }
        if (anStr == null) {
            return 0;
        }
        return str.compareTo(anStr);
    }

    /**
    * Convert special characters to HTML entities
    * '&' (ampersand) becomes '&amp;'
    * '"' (double quote) becomes '&quot;'
    * ''' (single quote) becomes '&#039;'
    * '<' (less than) becomes '&lt;'
    * '>' (greater than) becomes '&gt;'
    * @param source
    * @return String with HTML entities
    */
    @Nullable
    public static String htmlSpecialChars(String source) {
        if (source == null) {
            return null;
        }
        if("".equals(source)) {
            return source;
        }
        source = source.replace("&", "&amp;");
        source = source.replace("\"", "&quot;");
        source = source.replace("'", "&apos;");
        source = source.replace("<", "&lt;");
        source = source.replace(">", "&gt;");
        return source;
    }

    public static String htmlSpecialCharsBack(String source) {
        if (source == null) {
            return null;
        }
        if("".equals(source)) {
            return source;
        }
        source = source.replace("&amp;", "&");
        source = source.replace("&quot;", "\"");
        source = source.replace("&apos;", "'");
        source = source.replace("&lt;", "<");
        source = source.replace("&gt;", ">");
        return source;
    }

    public static boolean checkStrLen(String s, int maxLen) {
        if (s == null) {
            return true;
        }
        if("".equals(s)) {
            return true;
        }
        for(String p: s.split("\\s")) {
            if (p.length() > maxLen) {
                return false;
            }
        }
        return true;
    }

    /**
     * return the longest common prefix of s and t
     * @param s
     * @param t
     * @return return the longest common prefix
     */
    public static String lcp(String s, String t) {
        int n = Math.min(s.length(), t.length());
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) != t.charAt(i))
                return s.substring(0, i);
        }
        return s.substring(0, n);
    }

    /**
     * Стандартный split оставляет пустые поля там, где не было контента между разделителей.
     * Эта функция преобразует их в null
     * @param data Резултат работы {@link String#split(String)}
     */
    public static void nullifySplitResult(@Nullable String[] data) {
        if (data != null) {
            for (int i = 0; i < data.length; i++) {
                String s = data[i];
                if (s.length() == 0) {
                    data[i] = null;
                }
            }
        }
    }

    /**
     * Returns map string as key-value pair
     * @param map Any map
     *
     * @return Map string as key-value pair
     */
    @NotNull
    @SuppressWarnings("StaticMethodNamingConvention")
    public static String str(@Nullable Map<Object, Object> map) {
        if (map == null) { return "NULL"; }
        if (map.isEmpty()) { return "EMPTY"; }
        StringBuilder buf = new StringBuilder("{");
        for (Map.Entry<Object, Object> entry : map.entrySet()) {
            Object key = entry.getKey(); Object value = entry.getValue();
            buf.append(key).append(" = '").append(value).append("', ");
        }

        int bufLength = buf.length();
        if (bufLength > 2) { buf.delete(bufLength - 2, bufLength); }

        return buf.append('}').toString();
    }

    /**
     * Returns string containing collection comma-separated values
     * @param collection Any collection
     * @return String containing collection comma-separated values
     */
    @NotNull
    @SuppressWarnings("StaticMethodNamingConvention")
    public static String str(@Nullable Collection<?> collection) {
        if (collection == null) { return "NULL"; }
        if (collection.isEmpty())  { return "EMPTY"; }
        StringBuilder buf = new StringBuilder("{");
        for (Object o : collection) { buf.append('\'').append(o).append("', "); }

        return buf.append('}').toString();
    }

    /**
     * Shortcut for String.format()
     * @param template String template
     * @param args List of values
     * @return Applied template
     * @see String#format(String, Object[])
     */
    @SuppressWarnings("StaticMethodNamingConvention")
    public static String f(@NotNull String template, Object... args) {
        return String.format(template, args);
    }

    /**
     * repeat method with the ability to set a separator (http://issues.apache.org/jira/browse/LANG-348)
     */
    public static String repeat(String s, char c, int num) {
        if (num == 0) {
            return "";
        }
        if (num == 1) {
            return s;
        }
        StringBuilder sb = new StringBuilder((s.length() + 1) * num);
        sb.append(s);
        while (--num > 0) {
            sb.append(c).append(s);
        }
        return sb.toString();
    }

    public static String numbered(String source) {
        String[] lines = source.split("\n");
        NumberFormat nf = new DecimalFormat(repeat("", '0', 1+String.valueOf(lines.length+1).length())); // кто угадает что делает эта строка?
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i];
            res.append(nf.format(i+1));
            res.append(" ");
            res.append(line);
            res.append("\n");
        }
        return res.toString();
    }

    @NotNull
    public static String makeIconName(@NotNull String src) {
        String result = src.toLowerCase();
        result = result.replaceAll("\\W+", "_");
        return result;
    }

    @NotNull
    public static String formatDurationSec(int dur) {
        if (dur == 0) {
            return "";
        } else if (dur < 60) {
            return dur + " сек.";
        } else if (dur < 60 * 60) {
            int min = dur / 60;
            int sec = dur % 60;
            String minLabel = sec < 10 ? " мин.  " : " мин. ";
            return min + minLabel + sec + " сек.";
        } else {
            int hours = dur / (60 * 60);
            int tsec = dur % (60 * 60);
            int min = tsec / 60;
            int sec = tsec % 60;
            String minLabel = sec < 10 ? " мин.  " : " мин. ";
            String hourLabel = min < 10 ? " ч.  " : " ч. ";
            return hours + hourLabel +  min + minLabel + sec + " сек.";
        }
    }

    public static void appendEscaped(StringBuilder buf, String x) {
		int stringLength = x.length();
        buf.append('\'');

        //
        // Note: buf.append(char) is _faster_ than
        // appending in blocks, because the block
        // append requires a System.arraycopy()....
        // go figure...
        //

        for (int i = 0; i < stringLength; ++i) {
            char c = x.charAt(i);

            switch (c) {
            case 0: /* Must be escaped for 'mysql' */
                buf.append('\\');
                buf.append('0');

                break;

            case '\n': /* Must be escaped for logs */
                buf.append('\\');
                buf.append('n');

                break;

            case '\r':
                buf.append('\\');
                buf.append('r');

                break;

            case '\\':
                buf.append('\\');
                buf.append('\\');

                break;

            case '\'':
                buf.append('\\');
                buf.append('\'');

                break;

            /*case '"': // Better safe than sorry
                if (this.usingAnsiMode) {
                    buf.append('\\');
                }

                buf.append('"');

                break;*/

            case '\032': /* This gives problems on Win32 */
                buf.append('\\');
                buf.append('Z');

                break;

            default:
                buf.append(c);
            }
        }

        buf.append('\'');

    }

    public static boolean isEscapeNeededForString(String x, int stringLength) {
        boolean needsHexEscape = false;

        for (int i = 0; i < stringLength; ++i) {
            char c = x.charAt(i);

            switch (c) {
            case 0: /* Must be escaped for 'mysql' */

                needsHexEscape = true;
                break;

            case '\n': /* Must be escaped for logs */
                needsHexEscape = true;

                break;

            case '\r':
                needsHexEscape = true;
                break;

            case '\\':
                needsHexEscape = true;

                break;

            case '\'':
                needsHexEscape = true;

                break;

            /*case '"': // Better safe than sorry
                needsHexEscape = true;

                break;
            */
            case '\032': /* This gives problems on Win32 */
                needsHexEscape = true;
                break;
            }

            if (needsHexEscape) {
                break; // no need to scan more
            }
        }
        return needsHexEscape;
    }


}
