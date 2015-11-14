package org.cmas.util.bytecode;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.cmas.util.text.StringUtil.numbered;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class InMemoryCompiler {
    // Logger instance for class
    private static final Logger log = LoggerFactory.getLogger(InMemoryCompiler.class);

    private final ByteArrayClassLoader cl =
            new ByteArrayClassLoader(new HashMap<String, byte[]>(),getClass().getClassLoader());

    @Nullable
    public Class compile(String name, String source, Iterable<String> options)  {
        log.debug("source = \n" + numbered(source));
        boolean result = compileIt(name, source, options);
        log.debug("result of compilation "+result);
        if (!result) {
            log.error("compilation failed "+result+" source=\n"+source+" options="+options);
            return null;
        }
        try {
            return Class.forName(name, true, cl);
        } catch (ClassNotFoundException e) {
            log.error("error load class with name"+name, e);
            return null;
        }
    }

    private boolean compileIt(String className, String sourceCode, Iterable<String> options) {

        final List<JavaClassByteArray> classes = new ArrayList<JavaClassByteArray>();
        JavaCompiler compiler = getCompiler();

        StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(null, null, null);
        JavaFileManager fileManager = new ForwardingJavaFileManager(stdFileManager) {
            @Override
            public JavaFileObject getJavaFileForOutput(Location location, String cn, JavaFileObject.Kind kind, FileObject sibling) {
                JavaClassByteArray javaClassByteArray = new JavaClassByteArray(cn, kind);
                classes.add(javaClassByteArray);
                return javaClassByteArray;
            }
        };

        Iterable<? extends JavaFileObject> units = Arrays.asList(new JavaSourceFromString(className, sourceCode));
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
        Boolean aBoolean = compiler.getTask(null, fileManager, diagnostics, options, null, units).call();
        try {
            fileManager.close();
        } catch (IOException e) {
            log.warn("error close file manager" ,e);
        }
        diagnostics.getDiagnostics();
        for (Diagnostic<? extends JavaFileObject> message : diagnostics.getDiagnostics()) {
            log.warn("d = " + message);
        }
        for (JavaClassByteArray fileObject : classes) {
            cl.addClass(fileObject.getName(), fileObject.getData());
        }
        return aBoolean;
    }


    private JavaCompiler getCompiler() {
        try {
            String[] defaultToolsLocation = { "lib", "tools.jar" };
            File file = new File(System.getProperty("java.home"));
            if ("jre".equalsIgnoreCase(file.getName())) {
                file = file.getParentFile();
            }
            for (String name : defaultToolsLocation) {
                file = new File(file, name);
            }
            URL[] urls = {file.toURI().toURL()};
            ClassLoader urlClassLoader = URLClassLoader.newInstance(urls);
            return (JavaCompiler)Class.forName("com.sun.tools.javac.api.JavacTool", false, urlClassLoader).newInstance(); //getClass().getClassLoader())
        } catch (Exception e) {
            log.error("error getting compiler", e);
        }
        /*if (Lazy.compilerClass == null)
            return null;
        try {
            return Lazy.compilerClass.newInstance();
        } catch (Throwable e) {
            return null;
        }*/
        throw new IllegalStateException("cant get compiler");
    }

    /*static class Lazy  {
        private static final String defaultJavaCompilerName
            = "com.sun.tools.javac.api.JavacTool";
        private static final String[] defaultToolsLocation
            = { "lib", "tools.jar" };
        static final Class<? extends JavaCompiler> compilerClass;
        static {
            Class<? extends JavaCompiler> c = null;
            try {
                c = findClass().asSubclass(JavaCompiler.class);
            } catch (Throwable t) {
                // ignored
            }
            compilerClass = c;
        }

        private static Class<?> findClass()
            throws MalformedURLException, ClassNotFoundException
        {
            try {
                return enableAsserts(Class.forName(defaultJavaCompilerName, false, null));
            } catch (ClassNotFoundException e) {
                // ignored, try looking else where
            }
            File file = new File(System.getProperty("java.home"));
            if (file.getName().equalsIgnoreCase("jre"))
                file = file.getParentFile();
            for (String name : defaultToolsLocation)
                file = new File(file, name);
            URL[] urls = {file.toURI().toURL()};
            ClassLoader cl = URLClassLoader.newInstance(urls);
            cl.setPackageAssertionStatus("com.sun.tools.javac", true);
            return Class.forName(defaultJavaCompilerName, false, cl);
        }

        private static Class<?> enableAsserts(Class<?> cls) {
            try {
                ClassLoader loader = cls.getClassLoader();
                if (loader != null)
                    loader.setPackageAssertionStatus("com.sun.tools.javac", true);
            } catch (SecurityException ex) {
                // ignored
            }
            return cls;
        }
    }*/


}
