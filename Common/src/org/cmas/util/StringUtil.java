package org.cmas.util;

public class StringUtil {

    private StringUtil() {

    }

    public static boolean isTrimmedEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static String addProtocolIfNecessary(String url, String protocol) {
        if (url.startsWith(protocol)) {
            return url;
        }
        return protocol + "://" + url;
    }

    public static String[] concatArrays(String[] array1, String[] array2) {
        String[] newArray = new String[array1.length + array2.length];
        System.arraycopy(array1, 0, newArray, 0, array1.length);
        System.arraycopy(array2, 0, newArray, array1.length, array2.length);
        return newArray;
    }
}
