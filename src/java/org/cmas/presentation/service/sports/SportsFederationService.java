package org.cmas.presentation.service.sports;

import org.cmas.entities.sport.SportsFederation;
import org.cmas.entities.sport.Sportsman;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
public interface SportsFederationService {

    SportsFederation getSportsmanFederation(Sportsman sportsman);
}
