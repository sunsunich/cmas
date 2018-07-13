package org.cmas.entities.diver;

/**
 * Created on May 21, 2018
 *
 * @author Alexander Petukhov
 */
public enum DiverRegistrationStatus {
    NEVER_REGISTERED, INACTIVE, DEMO, GUEST, CMAS_BASIC, CMAS_FULL;

    public String getName() {
        return name();
    }
}
