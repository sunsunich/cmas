package org.cmas.util.bytecode;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

public class JavaClassByteArray extends SimpleJavaFileObject {

    ByteArrayOutputStream data = new ByteArrayOutputStream();
    /**
     * Construct a SimpleJavaFileObject of the given kind and with the
     * given URI.
     *
     * @param kind the kind of this file object
     */
    protected JavaClassByteArray(String name, Kind kind) {
        super(URI.create(name.replace('.','/') ), kind);
    }

    @Override
    public OutputStream openOutputStream() throws IOException {
        return data;
    }

    public byte[] getData() {
        return data.toByteArray();
    }
}
