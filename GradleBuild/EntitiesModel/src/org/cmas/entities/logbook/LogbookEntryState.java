package org.cmas.entities.logbook;

/**
 * Created on Jan 11, 2017
 *
 * @author Alexander Petukhov
 */
public enum LogbookEntryState {
    NEW, SAVED, PUBLISHED;

    public String getName() {
        return name();
    }
}
