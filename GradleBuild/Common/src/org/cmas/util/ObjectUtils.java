package org.cmas.util;

import org.cmas.Globals;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;


/**
 */
public class ObjectUtils {


    private static final AtomicLong LONG_SEQUENCE = new AtomicLong();

    /**
     * Skip first 1000 short values to look smarter, xoxo.
     * Some positive constant must be passed to constructor here.
     * By passing large value we skip system values used for Android ids (e.g. requestIds result codes etc.)
     */
    private static final AtomicLong SHORT_SEQUENCE = new AtomicLong(1001);

    public static final Runnable EMPTY_RUNNABLE = new Runnable() {
        @Override
        public void run() {
        }
    };

    private static final Map<String, Pattern> REGEXP_2_PATTERN = new ConcurrentHashMap<>();

    private ObjectUtils() {
    }

    public static long nextLong() {
        return LONG_SEQUENCE.getAndIncrement();
    }

    /**
     * The pool of available short values is very limited. So this must be called only from a static context like
     * public static final int SOME_ID = ObjectUtils.nextShort();
     * For example, this method must not be called on every Activity#onCreate() invocation.
     *
     * @return unique positive short
     */
    public static short nextShort() {
        long value = SHORT_SEQUENCE.getAndIncrement();
        if (value > Short.MAX_VALUE) {
            throw new IllegalStateException();
        }
        return (short) value;
    }

    public static void run(Runnable runnable) {
        if (runnable != null) {
            runnable.run();
        }
    }

    public static void run(Iterable<? extends Runnable> runnables) {
        if (runnables == null) {
            return;
        }

        for (Runnable runnable : runnables) {
            run(runnable);
        }
    }

    public static void await(CountDownLatch latch) {
        if (latch != null) {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /**
     * There is an org.apache.commons.lang3.ObjectUtils#firstNonNull(T...) method, but it always uses varargs.
     */
    public static <T> T firstNonNull(T first, T second) {
        return first == null ? second : first;
    }

    public static <T> T firstNonNull(T first, T second, T third) {
        return first != null ? first :
                second != null ? second : third;
    }

    public static <T> T get(Reference<T> reference) {
        return reference == null ? null : reference.get();
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * The "toStringOrNull" name is used to enable static import since the "toString" name cannot be imported.
     */
    public static String toStringOrNull(Object obj) {
        return obj == null ? null : obj.toString();
    }

    public static boolean equal(Object a, Object b) {
        return a == b || (a != null && a.equals(b));
    }

    public static boolean toStringEquals(Object o1, Object o2) {
        return equal(toStringOrNull(o1), toStringOrNull(o2));
    }

    public static void deleteFileOrThrow(File file) {
        if (file != null && file.exists()) {
            if (!file.delete()) {
                throw new IllegalStateException("Cannot delete file " + file.getAbsolutePath());
            }
        }
    }

    public static String urlEncode(String s) {
        try {
            return s == null ? null : URLEncoder.encode(s, Globals.UTF_8_ENC);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        }
    }

    public static Pattern getPattern(String regexp) {
        Pattern pattern = REGEXP_2_PATTERN.get(regexp);
        if (pattern == null) {
            pattern = Pattern.compile(regexp);
            REGEXP_2_PATTERN.put(regexp, pattern);
        }
        return pattern;
    }

    public static Set<Field> getFieldsAnnotatedWith(Class<?> clazz, Class<? extends Annotation> annClass) {
        Set<Field> fields = new HashSet<>();
        while (clazz != null) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(annClass)) {
                    fields.add(field);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return fields;
    }

    public static Set<Field> getAllFields(Class<?> clazz) {
        Set<Field> fields = new HashSet<>();
        while (clazz != null) {
            Collections.addAll(fields, clazz.getDeclaredFields());
            clazz = clazz.getSuperclass();
        }
        return fields;
    }


    public static void deleteRegularFiles(File dir) {
        if (dir == null || !dir.isDirectory()) {
            return;
        }
        for (File f : dir.listFiles()) {
            if (f.isFile()) {
                //noinspection ResultOfMethodCallIgnored
                f.delete();
            }
        }
    }
}
