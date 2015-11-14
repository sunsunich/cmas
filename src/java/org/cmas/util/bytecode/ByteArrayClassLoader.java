package org.cmas.util.bytecode;

import java.util.Arrays;
import java.util.Map;

/**
 * копипаста такая копипаста
 */
class ByteArrayClassLoader extends ClassLoader {

    private final Map<String,byte[]> classes;
    /**
     * The given {@link Map} of classes must not be modified afterwards.
     *
     * @param classes String className => byte[] data
     */
    public ByteArrayClassLoader(Map<String,byte[]> classes) {
        super();
        this.classes = classes;
    }

    /**
     * @see #ByteArrayClassLoader(Map)
     */
    public ByteArrayClassLoader(Map<String,byte[]> classes, ClassLoader parent) {
        super(parent);
        this.classes = classes;
    }

    void addClass(String name, byte[] bytecode) {
        classes.put(name.replaceAll("/", "."), bytecode);
    }
    /**
     * Implements {@link ClassLoader#findClass(String)}.
     * <p>
     * Notice that, although nowhere documented, no more than one thread at a time calls this
     * method, because {@link ClassLoader#loadClass(String)} is
     * <code>synchronized</code>.
     */
    @Override
    protected Class findClass(String name) throws ClassNotFoundException {
        byte[] data = classes.get(name);
        if (data == null) {
            throw new ClassNotFoundException(name);
        }

        return defineClass(
            name,                // name
            data, 0, data.length // b, off, len
        );
    }

    /**
     * An object is regarded equal to <code>this</code> iff
     * <ul>
     *   <li>It is an instance of {@link ByteArrayClassLoader}
     *   <li>{@link #equals(ByteArrayClassLoader)} returns <code>true</code>
     * </ul>
     * @see #equals(ByteArrayClassLoader)
     */
    @Override
    public boolean equals(Object that) {
        return that instanceof ByteArrayClassLoader && equals((ByteArrayClassLoader) that);
    }

    /**
     * Two {@link ByteArrayClassLoader}s are regarded equal iff
     * <ul>
     *   <li>Both have the same parent {@link ClassLoader}
     *   <li>Exactly the same classes (name, bytecode) were added to both
     * </ul>
     * Roughly speaking, equal {@link ByteArrayClassLoader}s will return functionally identical
     * {@link Class}es on {@link ClassLoader#loadClass(String)}.
     */
    public boolean equals(ByteArrayClassLoader that) {
        if (this == that) {
            return true;
        }

        if (!getParent().equals(that.getParent())) {
            return false;
        }

        if (classes.size() != that.classes.size()) {
            return false;
        }
        for (Map.Entry<String, byte[]> o : classes.entrySet()) {
            byte[] ba = that.classes.get(((Map.Entry) o).getKey());
            if (ba == null) {
                return false;
            }
            if (!Arrays.equals((byte[]) ((Map.Entry) o).getValue(), ba)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hc = getParent().hashCode();

        for (Map.Entry<String, byte[]> me : classes.entrySet()) {
            hc ^= me.getKey().hashCode();
            byte[] ba = me.getValue();
            for (byte aBa : ba) {
                hc = (31 * hc) ^ aBa;
            }
        }
        return hc;
    }

}
