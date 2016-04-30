package org.cmas.presentation.service.sports;

import org.cmas.entities.Country;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.NationalFederation;
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
    public NationalFederation getSportsmanFederationBySportsmanData(String firstName, String lastName, Country country) {
        //todo better search
        NationalFederation nationalFederation = sportsFederationDao.getByCountry(country);
        if (nationalFederation == null) {
            //todo remote call
            if("Alexander".equals(firstName) && country.getCode().equalsIgnoreCase("rus")){
                nationalFederation =  new NationalFederation();
                nationalFederation.setCountry(country);
                nationalFederation.setName("Test Russia");
                sportsFederationDao.save(nationalFederation);
            }
        }
        return nationalFederation;
    }

    @Override
    public Diver getDiver(String firstName, String lastName, Date dob, Country country) {
        NationalFederation federation = sportsFederationDao.getByCountry(country);
        //todo remote call
        return diverDao.searchDiver(federation, firstName, lastName, dob);
    }
}
