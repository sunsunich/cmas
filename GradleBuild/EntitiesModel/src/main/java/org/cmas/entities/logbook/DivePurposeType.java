package org.cmas.entities.logbook;

/**
 * Created on Jan 07, 2017
 *
 * @author Alexander Petukhov
 */
public enum DivePurposeType {
    /*
    purpose of the dive: only my training (education), recreational(fun), instructor teaching (instruction)
     */

    EDUCATIONAL, RECREATIONAL, INSTRUCTING;

    public String getName() {
        return name();
    }
}
