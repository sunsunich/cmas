package org.cmas.presentation.service.sports;

import org.cmas.entities.Country;
import org.cmas.entities.Role;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.logbook.LogbookVisibility;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.dao.user.sport.NationalFederationDao;
import org.cmas.presentation.model.admin.FederationFormObject;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private MailService mailService;

    @Override
    public Diver createNewFederation(FederationFormObject formObject) {
        Country country = countryDao.getByCode(formObject.getCountryCode());
        NationalFederation federation = new NationalFederation();
        federation.setCountry(country);
        federation.setName(formObject.getName());
        federation.setVersion(nationalFederationDao.getMaxVersion() + 1L);
        Serializable fedId = nationalFederationDao.save(federation);
        NationalFederation dbFederation = nationalFederationDao.getModel(fedId);

        Diver federationAdmin = diverDao.createNew(Diver.class);
        federationAdmin.setLocale(Locale.ENGLISH);
        federationAdmin.setFederation(dbFederation);
        federationAdmin.setCountry(country);
        federationAdmin.setRole(Role.ROLE_FEDERATION_ADMIN);

        federationAdmin.setDateReg(new Date());
        federationAdmin.setEmail(StringUtil.lowerCaseEmail(formObject.getEmail()));
        federationAdmin.setPassword("36b1f45f0a95d1624734220892f0e7a9"); // cmasdata
        federationAdmin.setFirstName(formObject.getName());
        federationAdmin.setLastName("");
        federationAdmin.setDefaultVisibility(LogbookVisibility.PRIVATE);

        Serializable adminId = diverDao.save(federationAdmin);
        Diver dbFederationAdmin = diverDao.getModel(adminId);

        // todo implement
        // mailService.sendInsuranceRequestFailed();

        return dbFederationAdmin;
    }

    @Override
    public void informAllFederations() {
        List<NationalFederation> federations = nationalFederationDao.getAll();
        for (NationalFederation federation : federations) {
            // todo implement
        //    mailService.sendInsuranceRequestFailed();
        }
    }

    @Override
    public List<Diver> searchDivers(String firstName, String lastName, Date dob, Country country, boolean isForRegistration) {
        List<NationalFederation> federations = nationalFederationDao.getByCountry(country);
        //todo remote call
        return diverDao.searchDivers(federations, firstName, lastName, dob,
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
