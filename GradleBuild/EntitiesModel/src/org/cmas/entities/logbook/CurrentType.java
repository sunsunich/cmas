package org.cmas.entities.logbook;

/**
 * Created on Jan 07, 2017
 *
 * @author Alexander Petukhov
 */
public enum CurrentType {
    NO_CURRENT, LIGHT_CURRENT, MEDIUM_CURRENT, STRONG_CURRENT;

    public String getName() {
        return name();
    }
}
