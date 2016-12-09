package org.cmas.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created on Jun 24, 2016
 *
 * @author Alexander Petukhov
 */
public class ShaEncoder {

    public static final String SEPARATOR = "+";

    public static String encode(String src) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] bytes = src.getBytes("UTF-8");
        md.update(bytes, 0, bytes.length);
        byte[] sha1hash = md.digest();
        return convertToHex(sha1hash);
    }

    private static String convertToHex(byte[] sha1hash) {
        StringBuilder builder = new StringBuilder();
        for (byte b : sha1hash) {
            addHex(builder, b >> 4 & 0xf);
            addHex(builder, b & 0xf);
        }
        return builder.toString();
    }

    private static void addHex(StringBuilder builder, int c) {
        if (c < 10) {
            builder.append((char) (c + '0'));
        } else {
            builder.append((char) (c + 'a' - 10));
        }
    }
}

