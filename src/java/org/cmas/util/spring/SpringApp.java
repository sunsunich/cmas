package org.cmas.util.spring;

import org.cmas.util.log.Log4jSetup;
import org.apache.log4j.Level;
import org.jetbrains.annotations.NonNls;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.ExampleMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public abstract class SpringApp<T extends SpringApp.AppOptions> {
    protected static final Logger LOG = LoggerFactory.getLogger(SpringApp.class);

    protected ConfigurableApplicationContext context;
    @NonNls
    protected static final String MAIN_APP_THREAD_NAME = "MainApp";

    public abstract T createOptionsInstance();
    public abstract void start(T opts) throws Exception;
    public abstract void stop() throws Exception;

    public void setupApplication(String[] args) throws Exception {  // main
        Thread.currentThread().setName(MAIN_APP_THREAD_NAME);
        T opts = parseOptions(args);
        setupLogger(opts);
        setupConfigFile(opts);
        addShutdownHook();
        start(opts);
        createPidFile(opts);
    }

    private T parseOptions(String[] args) {
        T opts = createOptionsInstance();
        CmdLineParser parser = new CmdLineParser(opts);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            String className = getClass().getName();
            System.err.println(e.getMessage());
            System.err.println("Usage: java " + className +  " arguments...");
            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();
            // print option sample. This is useful some time
            System.err.println("Example: java " + className + parser.printExample(ExampleMode.ALL));
            //noinspection CallToSystemExit
            System.exit(1);
        }
        return opts;
    }

    private void setupConfigFile(T opts) {
        // Путь к файлу настроек передается через системное свойство properties.file в PropertyConigurer,
        // который потом настраивает c помощью этого файла Spring context
        System.setProperty(PropertyConfigurer.RESOURCE_KEY, opts.getPropsFile().toString());
    }

    private void setupLogger(T opts) {
        // log4j configuration
        if (opts.getLog4jFile() == null) {
            Log4jSetup.basicSetup(Level.INFO);
        } else {
            Log4jSetup.xmlFileSetup(opts.getLog4jFile());
        }
    }

    private void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    stop();
                } catch (Exception e) {
                    LOG.error("Error stopping app", e);
                }
            }
        }));
    }

    private void createPidFile(T opts) throws IOException {
        File selfData = new File("/proc/self");
        if (selfData.exists()) {
            int pid = Integer.parseInt(selfData.getCanonicalFile().getName());
            // Записываем PID файл
            OutputStreamWriter wri = new OutputStreamWriter(new FileOutputStream(opts.getPidFile()));
            try {
                wri.write(Integer.toString(pid));
            } finally {
                wri.close();
            }
        }
    }

    public interface AppOptions {
        File getLog4jFile();
        File getPropsFile();
        File getPidFile();
    }
}

