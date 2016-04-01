package org.cmas.presentation.service.user;

import org.cmas.Globals;
import org.cmas.entities.CardUser;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.Athlete;
import org.cmas.presentation.dao.user.PersonalCardDao;
import org.cmas.presentation.dao.user.PersonalCardTypeDao;
import org.cmas.util.dao.HibernateDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.SecureRandom;

/**
 * Created on Mar 21, 2016
 *
 * @author Alexander Petukhov
 */
public class PersonalCardServiceImpl implements PersonalCardService {

    @Autowired
    private PersonalCardDao personalCardDao;

    @Autowired
    private PersonalCardTypeDao personalCardTypeDao;

    @Override
    public <T extends CardUser> PersonalCard generatePrimaryCard(T cardUser, HibernateDao<T> entityDao) {
        PersonalCard personalCard = new PersonalCard();
        switch (cardUser.getRole()) {
            case ROLE_ATHLETE:
                personalCard.setAthlete((Athlete)cardUser);
                break;
            case ROLE_DIVER:
                //fall through
            case ROLE_DIVER_INSTRUCTOR:
                personalCard.setDiver((Diver)cardUser);
                break;
            case ROLE_AMATEUR:
                //fall through
            case ROLE_ADMIN:
                throw new IllegalStateException();
        }

        personalCard.setPersonalCardType(personalCardTypeDao.getPrimaryCardType());
        saveAndSetCardNumber(personalCard);
        cardUser.setPrimaryPersonalCard(personalCard);
        entityDao.updateModel(cardUser);
        return personalCard;
    }

    private static final SecureRandom RND = new SecureRandom();

    @SuppressWarnings("StringConcatenationMissingWhitespace")
    private void saveAndSetCardNumber(PersonalCard personalCard) {
        long id = (Long) personalCardDao.save(personalCard);

        String strId = String.valueOf(id);
        String idStrRight;
        int charsToGenerate = Globals.SPORTS_CARD_NUMBER_MAX_LENGTH - strId.length() - 1;
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
        StringBuilder dec = new StringBuilder(charsToGenerate);
        for (int i = 0; i < charsToGenerate; i++) {
            dec.append(random.nextInt(9) + 1);
        }
        return dec.toString();
    }
}
