package org.cmas.presentation.service.user;

import org.cmas.Globals;
import org.cmas.backend.DrawCardService;
import org.cmas.entities.CardUser;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.sport.Athlete;
import org.cmas.presentation.dao.user.PersonalCardDao;
import org.cmas.presentation.dao.user.PersonalCardTypeDao;
import org.cmas.util.dao.HibernateDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.image.BufferedImage;
import java.security.SecureRandom;

/**
 * Created on Mar 21, 2016
 *
 * @author Alexander Petukhov
 */
public class PersonalCardServiceImpl implements PersonalCardService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PersonalCardDao personalCardDao;

    @Autowired
    private PersonalCardTypeDao personalCardTypeDao;

    @Autowired
    private DrawCardService drawCardService;

    @Override
    public <T extends CardUser> PersonalCard generatePrimaryCard(T cardUser, HibernateDao<T> entityDao) {
        PersonalCard personalCard = new PersonalCard();
        personalCard.setPersonalCardType(personalCardTypeDao.getPrimaryCardType());
        saveAndSetCardNumber(personalCard);
        switch (cardUser.getRole()) {
            case ROLE_ATHLETE:
                personalCard.setAthlete((Athlete) cardUser);
                break;
            case ROLE_DIVER:
                //fall through
            case ROLE_DIVER_INSTRUCTOR:
                Diver diver = (Diver) cardUser;
                personalCard.setDiver(diver);
                personalCard.setDiverLevel(diver.getDiverLevel());
                try {
                    BufferedImage image = drawCardService.drawDiverCard(personalCard);
//                    image.getData().getDataBuffer().
                    //todo set and save image
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                break;
            case ROLE_AMATEUR:
                //fall through
            case ROLE_ADMIN:
                throw new IllegalStateException();
        }
        personalCardDao.updateModel(personalCard);
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
