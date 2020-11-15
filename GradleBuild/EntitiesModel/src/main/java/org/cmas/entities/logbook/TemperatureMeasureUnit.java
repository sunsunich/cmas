package org.cmas.entities.logbook;

/**
 * Created on Jan 09, 2017
 *
 * @author Alexander Petukhov
 */
public enum TemperatureMeasureUnit {

    CELSIUS, FAHRENHEIT;

    public String getName() {
        return name();
    }
}
