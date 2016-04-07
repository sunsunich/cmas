package org.cmas.util;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64Test {

    public static void main(String[] args) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            IOUtils.copy(Base64Test.class.getResourceAsStream("account.png"), baos);
            //   baos.flush();
            byte[] imageBytes = baos.toByteArray();
            System.out.println(Base64Coder.encodeString(imageBytes));
        }
    }
}
