package org.cmas.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializationUtil {

    private SerializationUtil() {
    }

    /**
     * Read the object from Base64 string.
     */
    public static Serializable fromString(String s) throws IOException,
                                                      ClassNotFoundException {
        byte[] data = Base64Coder.decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        try {
            return (Serializable)ois.readObject();
        } finally {
            ois.close();
        }


    }

    /**
     * Write the object to a Base64 string.
     */
    public static String toString(Serializable o) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        try {
            oos.writeObject(o);
        } finally {
            oos.close();
        }

        return new String(Base64Coder.encode(baos.toByteArray()));
    }
}
