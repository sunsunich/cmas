package org.cmas.presentation.service.user;

import org.cmas.Globals;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.sport.Athlete;
import org.cmas.entities.sport.SportsFederation;
import org.cmas.presentation.dao.user.PersonalCardDao;
import org.cmas.presentation.dao.user.PersonalCardTypeDao;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.service.sports.NationalFederationService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public class AthleteServiceImpl extends UserServiceImpl<Athlete> implements AthleteService {

    @Autowired
    private NationalFederationService nationalFederationService;

    @Autowired
    private PersonalCardDao personalCardDao;

    @Autowired
    private PersonalCardTypeDao personalCardTypeDao;

    @Nullable
    @Override
    public Athlete add(Registration registration, String ip) {
        Athlete athlete = super.add(registration, ip);
        String firstName = registration.getFirstName();
        athlete.setFirstName(firstName);
        String lastName = registration.getLastName();
        athlete.setLastName(lastName);
        SportsFederation sportsmanFederation
                = nationalFederationService.getSportsmanFederationBySportsmanData(
                firstName, lastName, athlete.getCountry()
        );
        athlete.setFederation(sportsmanFederation);

        if(sportsmanFederation != null){
            PersonalCard personalCard =  new PersonalCard();
            personalCard.setAthlete(athlete);
            personalCard.setPersonalCardType(personalCardTypeDao.getPrimaryCardType());
            saveAndSetCardNumber(personalCard);
            athlete.setPrimaryPersonalCard(personalCard);
            entityDao.updateModel(athlete);
        }

        return athlete;
    }

    private static final SecureRandom RND = new SecureRandom();

    private void saveAndSetCardNumber(PersonalCard personalCard) {
        long id = (Long) personalCardDao.save(personalCard);

        String strId = String.valueOf(id);
        String idStrRight;
        int charsToGenerate = Globals.SPORTS_CARD_NUMBER_MAX_LENGTH - strId.length();
        if (charsToGenerate > 0) {
            idStrRight = generateNonZero(charsToGenerate, RND);
        } else {
            idStrRight = "";
        }
        personalCard.setNumber(
                strId + 0 + idStrRight
        );

        personalCardDao.updateModel(personalCard);
    }

    private static String generateNonZero(int charsToGenerate, SecureRandom random) {
        StringBuilder dec = new StringBuilder();
        for (int i = 0; i < charsToGenerate; i++) {
            dec.append(random.nextInt(9) + 1);
        }
        return dec.toString();
    }
}
