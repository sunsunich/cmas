package org.cmas.presentation.service.sports;

import org.cmas.entities.Country;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.SportsFederation;

import java.util.Date;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
public interface NationalFederationService {

    SportsFederation getSportsmanFederationBySportsmanData(String firstName, String lastName, Country country);

    Diver getDiver(String firstName, String lastName, Date dob, Country country);
}
