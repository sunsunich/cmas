package org.cmas.presentation.controller.admin;

import org.cmas.entities.diver.Diver;
import org.cmas.presentation.entities.user.Registration;

import java.util.List;

public interface UserAnnouncesService {

    void sendMobileReadyAnnounce(List<Diver> divers);

    void resendRegistrations(List<Registration> registrations);

    void sendManualsToFederations(List<Diver> federationAdmins);
}
