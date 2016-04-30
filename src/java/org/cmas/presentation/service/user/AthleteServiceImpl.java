package org.cmas.presentation.service.user;

import org.cmas.entities.sport.Athlete;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.service.sports.NationalFederationService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public class AthleteServiceImpl extends UserServiceImpl<Athlete> implements AthleteService {

    @Autowired
    private NationalFederationService nationalFederationService;

    @Autowired
    private PersonalCardService personalCardService;

    @Nullable
    @Override
    public Athlete add(Registration registration, String ip) {
        Athlete athlete = super.add(registration, ip);
        String firstName = registration.getFirstName();
        athlete.setFirstName(firstName);
        String lastName = registration.getLastName();
        athlete.setLastName(lastName);
        NationalFederation sportsmanFederation
                = nationalFederationService.getSportsmanFederationBySportsmanData(
                firstName, lastName, athlete.getCountry()
        );
        athlete.setFederation(sportsmanFederation);

        if(sportsmanFederation != null){
           personalCardService.generatePrimaryCard(athlete, entityDao);
        }

        return athlete;
    }


}
