/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cmas.util.barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

/**
 * This class does the work of decoding the user's request and extracting all the data
 * to be encoded in a barcode.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class BarcodeEncoder {

    private static final int WHITE = 0xFFFFFFFF;
    private static final int BLACK = 0xFF000000;

    private BarcodeEncoder() {
    }

    public static Pixels createQRCode(String number, int imgWidth, int imgHeight) throws WriterException {
        return encodeAsBitmap(number, BarcodeFormat.QR_CODE, imgWidth, imgHeight);
    }

    public static Pixels encodeAsBitmap(String contents, BarcodeFormat format, int desiredWidth, int desiredHeight)
            throws WriterException {
        Hashtable<EncodeHintType, Object> hints = null;
        String encoding = guessAppropriateEncoding(contents);
        if (encoding != null) {
            hints = new Hashtable<>(2);
            hints.put(EncodeHintType.CHARACTER_SET, encoding);
        }
        Writer writer = new MultiFormatWriter();
        BitMatrix result = writer.encode(contents, format, desiredWidth, desiredHeight, hints);
        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        return new Pixels(width, height, pixels);
    }

    private static String guessAppropriateEncoding(CharSequence contents) {
        // Very crude at the moment
        for (int i = 0; i < contents.length(); i++) {
            if ((int) contents.charAt(i) > 0xFF) {
                return "UTF-8";
            }
        }
        return null;
    }

}
