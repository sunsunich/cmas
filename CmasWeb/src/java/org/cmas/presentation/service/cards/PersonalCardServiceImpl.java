package org.cmas.presentation.service.cards;

import com.google.firebase.database.utilities.Pair;
import org.cmas.Globals;
import org.cmas.backend.DrawCardService;
import org.cmas.backend.ImageStorageManager;
import org.cmas.entities.Role;
import org.cmas.entities.cards.CardUser;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.sport.Athlete;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.controller.cards.CardDisplayManager;
import org.cmas.presentation.dao.cards.PersonalCardDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.util.dao.HibernateDao;
import org.cmas.util.dao.RunInHibernate;
import org.cmas.util.schedule.Scheduler;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
    private DiverDao diverDao;

    @Autowired
    private DrawCardService drawCardService;

    @Autowired
    private ImageStorageManager imageStorageManager;

    @Autowired
    private CardDisplayManager cardDisplayManager;

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private Scheduler scheduler;

    @Override
    public void setupDisplayCardsForDivers(List<Diver> divers) {
        for (Diver diver : divers) {
            List<PersonalCard> cardsToShow = getCardsToShow(diver);
            diver.setCards(cardsToShow);
        }
    }

    @Override
    public List<PersonalCard> getCardsToShow(Diver diver) {
        List<PersonalCard> cards = diver.getCards();
        if (cards == null || cards.isEmpty()) {
            return new ArrayList<>();
        }
        Map<PersonalCardType, PersonalCard> result
                = new EnumMap<>(PersonalCardType.class);
        for (PersonalCard card : cards) {
            PersonalCardType cardType = card.getCardType();
            if (cardType == PersonalCardType.PRIMARY) {
                continue;
            }
            PersonalCard existingCard = result.get(cardType);
            if (existingCard == null) {
                result.put(cardType, card);
            } else {
                if (card.getDiverType() == existingCard.getDiverType()) {
                    if (card.getDiverLevel() == null) {
                        continue;
                    }
                    if (existingCard.getDiverLevel() == null
                        || existingCard.getDiverLevel().ordinal() < card.getDiverLevel().ordinal()) {
                        result.put(cardType, card);
                    }
                } else if (card.getDiverType() == DiverType.INSTRUCTOR) {
                    result.put(cardType, card);
                }
            }
        }
        return new ArrayList<>(result.values());
    }

    @Nullable
    @Override
    public PersonalCard getMaxNationalCard(Diver diver) {
        List<PersonalCard> diverCards = personalCardDao.getNationalCardsOrdered(diver);
        return getMaxCard(diverCards);
    }

    @Nullable
    @Override
    public PersonalCard getMaxCard(Diver diver) {
        return getMaxCard(personalCardDao.getCardsByDiver(diver));
    }

    private static PersonalCard getMaxCard(Collection<PersonalCard> diverCards) {
        if (diverCards == null) {
            return null;
        }
        PersonalCard foundCard = null;
        for (PersonalCard card : diverCards) {
            if (foundCard == null) {
                foundCard = card;
            } else {
                DiverType cardDiverType = card.getDiverType();
                if (cardDiverType == null) {
                    continue;
                }
                DiverType foundCardDiverType = foundCard.getDiverType();
                if (foundCardDiverType != cardDiverType) {
                    if (foundCardDiverType != DiverType.INSTRUCTOR) {
                        foundCard = card;
                    }
                    // if types are not equal no point comparing levels
                    continue;
                }
                DiverLevel cardDiverLevel = card.getDiverLevel();
                if (cardDiverLevel == null) {
                    continue;
                }
                DiverLevel foundCardDiverLevel = foundCard.getDiverLevel();
                if (foundCardDiverLevel == null || foundCardDiverLevel.ordinal() < cardDiverLevel.ordinal()) {
                    foundCard = card;
                }
            }
        }
        return foundCard;
    }

    @Override
    public void generateNonPrimaryCardsImages(CardUser cardUser) {
        if (cardUser.getRole() == Role.ROLE_DIVER) {
            final long diverId = cardUser.getId();
            scheduler.schedule(
                    new RunInHibernate(sessionFactory) {
                        @Override
                        protected void runTaskInHibernate() {
                            List<PersonalCard> cardsToShow = getCardsToShow(diverDao.getById(diverId));
                            for (PersonalCard card : cardsToShow) {
                                generateAndSaveCardImage(card.getId());
                            }
                        }
                    }, 0L, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public <T extends CardUser> PersonalCard generatePrimaryCard(T cardUser, HibernateDao<T> entityDao, boolean generateImage) {
        final PersonalCard personalCard = new PersonalCard();
        personalCard.setCardType(PersonalCardType.PRIMARY);
        saveAndSetCardNumber(personalCard);
        switch (cardUser.getRole()) {
            case ROLE_ATHLETE:
                personalCard.setAthlete((Athlete) cardUser);
                break;
            case ROLE_DIVER:
                Diver diver = (Diver) cardUser;
                personalCard.setDiver(diver);
                DiverLevel diverLevel = diver.getDiverLevel();
                if (diverLevel == null) {
                    diverLevel = DiverLevel.ONE_STAR;
                    diver.setDiverLevel(diverLevel);
                    diverDao.updateModel(diver);
                }
                personalCard.setDiverLevel(diverLevel);
                personalCard.setDiverType(diver.getDiverType());
                break;
            case ROLE_AMATEUR:
            case ROLE_FEDERATION_ADMIN:
            case ROLE_ADMIN:
                throw new IllegalStateException();
        }
        personalCardDao.updateModel(personalCard);
        cardUser.setPrimaryPersonalCard(personalCard);
        entityDao.updateModel(cardUser);
        if (generateImage && cardUser.getRole() == Role.ROLE_DIVER) {
            scheduler.schedule(
                    new RunInHibernate(sessionFactory) {
                        @Override
                        protected void runTaskInHibernate() {
                            generateAndSaveCardImage(personalCard.getId());
                        }
                    }, 0L, TimeUnit.MILLISECONDS);
        }
        return personalCard;
    }

    @Override
    public PersonalCard generateAndSaveCardImage(long personalCardId) {
        PersonalCard personalCard = personalCardDao.getById(personalCardId);
        // fetching everything manually, hibernate fetch fails sometimes
        Diver diver = diverDao.getById(personalCard.getDiver().getId());
        if (diver == null) {
            log.error("Cannot generateAndSaveCardImage, personalCardId=" + personalCardId
                      + ". No diver for this card");
            return personalCard;
        }
        List<PersonalCard> diversCards = personalCardDao.getCardsByDiver(diver);
        PersonalCard primaryCard = null;
        List<PersonalCard> otherCards = new ArrayList<>(diversCards.size());
        for (PersonalCard card : diversCards) {
            if (card.getCardType() == PersonalCardType.PRIMARY) {
                primaryCard = card;
            } else {
                otherCards.add(card);
            }
        }
        if (primaryCard == null) {
            log.error("Cannot generateAndSaveCardImage, personalCardId=" + personalCardId
                      + ". No primary card for diver id=" + diver.getId());
            return personalCard;
        }

        try {
            Pair<BufferedImage, BufferedImage> images = drawCardService.drawDiverCard(personalCard,
                                                                                      primaryCard,
                                                                                      otherCards);
            imageStorageManager.storeCardImage(personalCard, images.getFirst(), images.getSecond());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
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

    @Override
    public boolean canFederationEditCard(NationalFederation nationalFederation, PersonalCardType cardType) {
        Map<String, PersonalCardType[]> personalCardTypes = nationalFederation.getIsHeli()
                ? cardDisplayManager.getHeliCardGroups()
                : cardDisplayManager.getPersonalCardGroups();
        for (PersonalCardType[] heliCards : personalCardTypes.values()) {
            if (Arrays.asList(heliCards).contains(cardType)) {
                return true;
            }
        }
        return false;
    }
}
