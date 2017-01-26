package org.cmas.entities.logbook;

/**
 * Created on Jan 07, 2017
 *
 * @author Alexander Petukhov
 */
public enum SurfaceType {
    NO_WAVES, SMALL_WAVES, MEDIUM_WAVES, BIG_WAVES;

    public String getName() {
        return name();
    }
}
