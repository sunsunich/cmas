package org.cmas.util.spring;

import org.springframework.beans.factory.config.PropertyOverrideConfigurer;
import org.springframework.core.io.FileSystemResource;

/**
 * Конфигураторп свойств для Spring-конткста. Берет свойства из файла, путь к которому задан
 * в системном свойстве "properties.file". Если такой файл не найден, возбуждается Exception
 * и Spring не стартует
 */
public class PropertyConfigurer extends PropertyOverrideConfigurer {
    public static final String RESOURCE_KEY = "properties.file";

    public PropertyConfigurer() {
        super();
        String file = System.getProperty(RESOURCE_KEY);
        if (file == null) {
            throw new IllegalArgumentException("Can't fetch resource file path from system properties by key "
                    + RESOURCE_KEY + " - key is not defined");
        }
        FileSystemResource r = new FileSystemResource(file);
        setLocation(r);
    }
}
