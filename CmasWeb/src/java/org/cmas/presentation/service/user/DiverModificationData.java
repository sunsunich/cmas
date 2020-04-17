package org.cmas.presentation.service.user;

import org.cmas.entities.diver.Diver;

/**
 * Created on Apr 15, 2020
 *
 * @author Alexander Petukhov
 */
class DiverModificationData {

    final Diver dbDiver;
    Diver instructor;

    DiverModificationData(Diver dbDiver, Diver instructor) {
        this.dbDiver = dbDiver;
        this.instructor = instructor;
    }
}
