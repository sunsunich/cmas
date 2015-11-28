package org.cmas.presentation.service.user;

import org.cmas.Globals;
import org.cmas.entities.sport.SportsFederation;
import org.cmas.entities.sport.Sportsman;
import org.cmas.entities.sport.SportsmanCard;
import org.cmas.presentation.dao.user.sport.SportsmanCardDao;
import org.cmas.presentation.dao.user.sport.SportsmanCardTypeDao;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.service.sports.SportsFederationService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;

/**
 * Created on Nov 22, 2015
 *
 * @author Alexander Petukhov
 */
public class SportsmanServiceImpl extends UserServiceImpl<Sportsman> implements SportsmanService {

    @Autowired
    private SportsFederationService sportsFederationService;

    @Autowired
    private SportsmanCardDao sportsmanCardDao;

    @Autowired
    private SportsmanCardTypeDao sportsmanCardTypeDao;

    @Nullable
    @Override
    public Sportsman add(Registration registration, String ip) {
        Sportsman sportsman = super.add(registration, ip);
        String firstName = registration.getFirstName();
        sportsman.setFirstName(firstName);
        String lastName = registration.getLastName();
        sportsman.setLastName(lastName);
        SportsFederation sportsmanFederation
                = sportsFederationService.getSportsmanFederationBySportsmanData(
                firstName, lastName, sportsman.getCountry()
        );
        sportsman.setFederation(sportsmanFederation);

        if(sportsmanFederation != null){
            SportsmanCard sportsmanCard =  new SportsmanCard();
            sportsmanCard.setSportsman(sportsman);
            sportsmanCard.setSportsmanCardType(sportsmanCardTypeDao.getPrimaryCardType());
            saveAndSetCardNumber(sportsmanCard);
        }

        return sportsman;
    }

    private static final SecureRandom RND = new SecureRandom();

    private void saveAndSetCardNumber(SportsmanCard sportsmanCard) {
        long id = (Long) sportsmanCardDao.save(sportsmanCard);

        String strId = String.valueOf(id);
        String idStrRight;
        int charsToGenerate = Globals.SPORTS_CARD_NUMBER_MAX_LENGTH - strId.length();
        if (charsToGenerate > 0) {
            idStrRight = generateNonZero(charsToGenerate, RND);
        } else {
            idStrRight = "";
        }
        sportsmanCard.setNumber(
                strId + 0 + idStrRight
        );

        sportsmanCardDao.updateModel(sportsmanCard);
    }

    private static String generateNonZero(int charsToGenerate, SecureRandom random) {
        StringBuilder dec = new StringBuilder();
        for (int i = 0; i < charsToGenerate; i++) {
            dec.append(random.nextInt(9) + 1);
        }
        return dec.toString();
    }
}
