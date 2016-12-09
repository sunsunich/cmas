package org.cmas.util.log;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.xml.DOMConfigurator;

import java.io.File;

@SuppressWarnings({"ClassNamingConvention"})
public class Log4jSetup {
    private Log4jSetup() {
    }


    /**
     * Выполняет {@link #basicSetup(Level)} с уровнем WARN или DEBUG в зависимости от
     * параметра quiet
     * @param quiet WARN or DEBUG?
     */
    public static void basicSetup(boolean quiet) {
        Level level = quiet ? Level.WARN : Level.DEBUG;
        basicSetup(level);
    }

    /**
     * Выполняет {@link #basicSetup(Level)} с уровнем INFO
     */
    public static void basicSetup() {
        basicSetup(Level.INFO);
    }

    /**
     * Настраивает Log4J настройками из указанного файла
     * @param xmlFile File to init from
     */
    public static void xmlFileSetup(File xmlFile) {
        DOMConfigurator.configureAndWatch(xmlFile.getPath());
    }

    /**
     * Выполняет простую настройку log4j - все сыплется в консоль
     * @param level Logging level
     */
    public static void basicSetup(Level level) {
        Logger.getRootLogger().setLevel(Level.INFO);
        Logger.getLogger("org.cmas").setLevel(level);
        // Set normal pattern instead of simple
        Logger.getRootLogger().removeAllAppenders();
//        Logger.getRootLogger().addAppender( new ConsoleAppender(new PatternLayout(PatternLayout.TTCC_CONVERSION_PATTERN)) );
        Logger.getRootLogger().addAppender( new ConsoleAppender(new PatternLayout("%d{yyyy.MM.dd HH:mm:ss,S} [%t] %-5p %x %c - %m%n")) );
    }
}
