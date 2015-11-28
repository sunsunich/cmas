package org.cmas.presentation.service.sports;

import org.cmas.entities.Country;
import org.cmas.entities.sport.SportsFederation;
import org.cmas.presentation.dao.user.sport.SportsFederationDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
public class SportsFederationServiceImpl implements SportsFederationService {

    @Autowired
    private SportsFederationDao sportsFederationDao;

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
}
