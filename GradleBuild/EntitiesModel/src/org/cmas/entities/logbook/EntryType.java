package org.cmas.entities.logbook;

/**
 * Created on Jan 07, 2017
 *
 * @author Alexander Petukhov
 */
public enum EntryType {

    FROM_SHORE, FROM_BOAT;

    public String getName() {
        return name();
    }
}
