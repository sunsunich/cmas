package org.cmas.presentation.service.sports;

import org.cmas.entities.Country;
import org.cmas.entities.sport.SportsFederation;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
public interface SportsFederationService {

    SportsFederation getSportsmanFederationBySportsmanData(String firstName, String lastName, Country country);
}
