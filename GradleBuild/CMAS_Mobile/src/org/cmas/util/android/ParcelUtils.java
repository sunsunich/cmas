package org.cmas.util.android;

import android.os.Parcelable;
import org.cmas.util.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * User: ABadretdinov
 * Date: 03.04.14
 * Time: 17:30
 */
public class ParcelUtils {
    /**
     * Read the object from Base64 string.
     */
    public static Parcelable fromString(String s) throws IOException,
            ClassNotFoundException {
        byte[] data = Base64Coder.decode(s);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(data));
        try {
            return (Parcelable)ois.readObject();
        } finally {
            ois.close();
        }


    }

    /**
     * Write the object to a Base64 string.
     */
    public static String toString(Parcelable o) throws IOException {
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

