package org.cmas.presentation.service.sports;

import org.cmas.entities.Country;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.dao.user.sport.NationalFederationDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

/**
 * Created on Nov 20, 2015
 *
 * @author Alexander Petukhov
 */
public class NationalFederationServiceImpl implements NationalFederationService {

    @Autowired
    private NationalFederationDao nationalFederationDao;

    @Autowired
    private DiverDao diverDao;

    @Override
    public NationalFederation getSportsmanFederationBySportsmanData(String firstName, String lastName, Country country) {
        //todo better search
        NationalFederation nationalFederation = nationalFederationDao.getByCountry(country);
        if (nationalFederation == null) {
            //todo remote call
            if("Alexander".equals(firstName) && country.getCode().equalsIgnoreCase("rus")){
                nationalFederation =  new NationalFederation();
                nationalFederation.setCountry(country);
                nationalFederation.setName("Test Russia");
                nationalFederationDao.save(nationalFederation);
            }
        }
        return nationalFederation;
    }

    @Override
    public List<Diver> searchDivers(String firstName, String lastName, Date dob, Country country, boolean isForRegistration) {
        NationalFederation federation = nationalFederationDao.getByCountry(country);
        //todo remote call
        return diverDao.searchDivers(federation, firstName, lastName, dob,
                                     isForRegistration ? DiverRegistrationStatus.NEVER_REGISTERED : null
        );
    }

    @Override
    public List<Diver> searchDivers(String certificateNumber, boolean isForRegistration) {
        //todo remote call
        return diverDao.getDiversByCardNumber(certificateNumber,
                                     isForRegistration ? DiverRegistrationStatus.NEVER_REGISTERED : null
        );
    }
}
