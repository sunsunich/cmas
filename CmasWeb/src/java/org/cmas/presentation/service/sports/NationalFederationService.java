package org.cmas.presentation.service.sports;

import org.cmas.entities.Country;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.NationalFederation;

import java.util.Date;
import java.util.List;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
public interface NationalFederationService {

    NationalFederation getSportsmanFederationBySportsmanData(String firstName, String lastName, Country country);

    List<Diver> searchDivers(String firstName, String lastName, Date dob, Country country, boolean isForRegistration);

    List<Diver> searchDivers(String certificateNumber, boolean isForRegistration);
}
