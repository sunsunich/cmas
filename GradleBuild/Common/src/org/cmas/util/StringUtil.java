package org.cmas.util;

public final class StringUtil {

    private StringUtil() {

    }

    public static boolean isTrimmedEmpty(String s) {
        return s == null || correctSpaceCharAndTrim(s).isEmpty();
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

    public static String correctSpaceCharAndTrim(String str) {
        if (str == null) {
            return null;
        }
        return str.replace((char) 160, ' ').trim();
    }

    public static boolean equalsFull(String str, String anStr) {
        if (str == null && anStr == null) {
            return true;
        }
        if (str == null && anStr != null) {
            return false;
        }
        if (str != null && anStr == null) {
            return false;
        }
        return correctSpaceCharAndTrim(str).equals(correctSpaceCharAndTrim(anStr));
    }

    public static String addLeadingZerosDecimal(int maxDigits, int i) {
        long maxNumber = (long) StrictMath.pow(10.0, (double) maxDigits);
        if (maxNumber > (long) Integer.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        if ((long) i >= maxNumber) {
            throw new IllegalArgumentException();
        }
        return String.valueOf(maxNumber + (long) i).substring(1);
    }
}
