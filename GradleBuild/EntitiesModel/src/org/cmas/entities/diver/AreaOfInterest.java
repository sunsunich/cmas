package org.cmas.entities.diver;

/**
 * Created on Jul 10, 2018
 *
 * @author Alexander Petukhov
 */
public enum AreaOfInterest {
    SCUBA_DIVING, FREE_DIVING, BLUE_HELMETS, OTHER;

    public String getName() {
        return name();
    }
}
