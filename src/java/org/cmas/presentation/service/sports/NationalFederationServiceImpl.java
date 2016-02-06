package org.cmas.presentation.service.sports;

import org.cmas.entities.Country;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.SportsFederation;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.dao.user.sport.SportsFederationDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
public class NationalFederationServiceImpl implements NationalFederationService {

    @Autowired
    private SportsFederationDao sportsFederationDao;

    @Autowired
    private DiverDao diverDao;

    @Override
    public SportsFederation getSportsmanFederationBySportsmanData(String firstName, String lastName, Country country) {
        //todo better search
        SportsFederation sportsFederation = sportsFederationDao.getByCountry(country);
        if (sportsFederation == null) {
            //todo remote call
            if("Alexander".equals(firstName) && country.getCode().equalsIgnoreCase("rus")){
                sportsFederation =  new SportsFederation();
                sportsFederation.setCountry(country);
                sportsFederation.setName("Test Russia");
                sportsFederationDao.save(sportsFederation);
            }
        }
        return sportsFederation;
    }

    @Override
    public Diver getDiver(String firstName, String lastName, Date dob, Country country) {
        SportsFederation federation = sportsFederationDao.getByCountry(country);
        //todo remote call
        return diverDao.searchDiver(federation, firstName, lastName, dob);
    }
}
