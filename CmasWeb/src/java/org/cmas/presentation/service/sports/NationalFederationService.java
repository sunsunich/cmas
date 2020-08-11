package org.cmas.presentation.service.sports;

import org.cmas.entities.Country;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.model.admin.FederationFormObject;

import java.util.Date;
import java.util.List;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
public interface NationalFederationService {

    /**
     *
     * @param formObject data to create federation from
     * @return federation admin
     */
    Diver createNewFederation(FederationFormObject formObject);

    void informAllFederations();

    List<Diver> searchDivers(String firstName, String lastName, Date dob, Country country, boolean isForRegistration);

    List<Diver> searchDivers(String certificateNumber, boolean isForRegistration);
}
