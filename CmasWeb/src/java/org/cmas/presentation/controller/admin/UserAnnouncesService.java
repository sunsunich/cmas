package org.cmas.presentation.controller.admin;

import org.cmas.entities.diver.Diver;

import java.util.List;

public interface UserAnnouncesService {

    void sendMobileReadyAnnounce(List<Diver> divers);

    void sendManualsToFederations(List<Diver> federationAdmins);
}
