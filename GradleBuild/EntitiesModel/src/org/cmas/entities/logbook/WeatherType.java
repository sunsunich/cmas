package org.cmas.entities.logbook;

/**
 * Created on Jan 07, 2017
 *
 * @author Alexander Petukhov
 */
public enum WeatherType {

    CLEAR, CLOUDY, MISTY, RAIN, SNOW, STORMY;

    public String getName() {
        return name();
    }
}
