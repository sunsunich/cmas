package org.cmas.presentation.service.user;

import org.apache.commons.lang.LocaleUtils;
import org.cmas.backend.xls.DiverXlsParser;
import org.cmas.entities.Country;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.PersonalCardType;
import org.cmas.entities.Role;
import org.cmas.entities.UserBalance;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.user.PersonalCardDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.util.LocaleMapping;
import org.cmas.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * Created on Apr 29, 2016
 *
 * @author Alexander Petukhov
 */
public class DiverServiceImpl extends UserServiceImpl<Diver> implements DiverService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PersonalCardService personalCardService;

    @Autowired
    @Qualifier("rusDiverXlsParser")
    private DiverXlsParser rusDiverXlsParser;

    @Autowired
    @Qualifier("singleTableDiverXlsParser")
    private DiverXlsParser singleTableDiverXlsParser;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private PersonalCardDao personalCardDao;

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
                if (existingCard.getDiverLevel().ordinal() < card.getDiverLevel().ordinal()) {
                    result.put(cardType, card);
                }
            }
        }
        return new ArrayList<>(result.values());
    }


    @SuppressWarnings("CallToStringEqualsIgnoreCase")
    @Override
    public void uploadDivers(NationalFederation federation, InputStream file) throws Exception {
        Collection<Diver> divers;
        Country country = federation.getCountry();
        String countryCode = country.getCode();
        if ("RUS".equalsIgnoreCase(countryCode)) {
            divers = rusDiverXlsParser.getDivers(file);
        } else {
            divers = singleTableDiverXlsParser.getDivers(file);
        }
        if (divers == null) {
            throw new UnsupportedOperationException("Federation not supported");
        }
        for (Diver diver : divers) {
            uploadDiver(federation, diver, false);
            //hidden functionality creating or editing primary card number
            Diver dbDiver = diverDao.getByEmail(diver.getEmail());
            PersonalCard primaryPersonalCard = diver.getPrimaryPersonalCard();
            if (primaryPersonalCard != null) {
                String newNumber = primaryPersonalCard.getNumber();
                if (!StringUtil.isTrimmedEmpty(newNumber)) {
                    PersonalCard checkingCard = personalCardDao.getByNumber(newNumber);
                    if (checkingCard == null) {
                        // no such number found in DB
                        PersonalCard dbCard = dbDiver.getPrimaryPersonalCard();
                        boolean isNewCard = dbCard == null;
                        if (isNewCard || !dbCard.getNumber().equals(newNumber)) {
                            //number change or new number detected
                            try {
                                if (isNewCard) {
                                    dbCard = primaryPersonalCard;
                                    dbCard.setCardType(PersonalCardType.PRIMARY);
                                    dbCard.setDiverLevel(dbDiver.getDiverLevel());
                                    dbCard.setDiverType(dbDiver.getDiverType());
                                    primaryPersonalCard.setDiver(dbDiver);
                                    personalCardDao.save(dbCard);
                                    dbDiver.setPrimaryPersonalCard(primaryPersonalCard);
                                    diverDao.updateModel(dbDiver);
                                } else {
                                    dbCard.setNumber(newNumber);
                                    personalCardDao.updateModel(dbCard);
                                }

                                personalCardService.generateAndSaveCardImage(dbCard.getId());
                                if (dbDiver.getDateReg() == null) {
                                    dbDiver.setDateReg(new Date());
                                    diverDao.updateModel(dbDiver);
                                }
                            } catch (Exception e) {
                                //number change or creation failed
                                log.error(e.getMessage(), e);
                            }
                        }
                    }
                }
            }
        }
        for (Diver diver : divers) {
            Diver dbDiver = diverDao.getByEmail(diver.getEmail());
            setDiverInstructor(dbDiver, diver.getInstructor());
        }
    }

    private void setDiverInstructor(Diver dbDiver, Diver instructor) {
        Diver dbInstructor = null;
        if (instructor != null) {
            dbInstructor = diverDao.getDiverByCardNumber(
                    instructor.getCards().get(0).getNumber()
            );
        }
        if (dbDiver.getInstructor() == null && dbInstructor != null) {
            dbDiver.setInstructor(dbInstructor);
        }
        diverDao.updateModel(dbDiver);
    }

    @SuppressWarnings({"CallToStringEqualsIgnoreCase", "CallToStringEquals", "ObjectAllocationInLoop"})
    @Override
    public void uploadDiver(NationalFederation federation, Diver diver, boolean overrideCards) {
        Country country = federation.getCountry();
        String countryCode = country.getCode();
        Locale locale = null;
        @SuppressWarnings("unchecked")
        List<Locale> languages = LocaleUtils.languagesByCountry(LocaleMapping.getIso2CountryCode(countryCode));
        if (!languages.isEmpty()) {
            locale = languages.get(0);
        }
        String email = diver.getEmail();
        Diver dbDiver = diverDao.getByEmail(email);
        boolean isNew = dbDiver == null;
        if (isNew) {
            dbDiver = diverDao.createNew(Diver.class);
            dbDiver.setLocale(locale);
            dbDiver.setFederation(federation);
            dbDiver.setCountry(country);
            dbDiver.setRole(Role.ROLE_DIVER);
            dbDiver.setEmail(email);

            UserBalance userBalance = new UserBalance();
            userBalanceDao.save(userBalance);
            dbDiver.setUserBalance(userBalance);
        }
        String firstName = diver.getFirstName();
        if (!StringUtil.isTrimmedEmpty(firstName)) {
            dbDiver.setFirstName(firstName);
        }
        String lastName = diver.getLastName();
        if (!StringUtil.isTrimmedEmpty(lastName)) {
            dbDiver.setLastName(lastName);
        }
        Date dob = diver.getDob();
        if (dob != null) {
            dbDiver.setDob(dob);
        }
        DiverType diverType = diver.getDiverType();
        if (diverType != null && dbDiver.getDiverType() != DiverType.INSTRUCTOR) {
            dbDiver.setDiverType(diverType);
        }
        DiverLevel diverLevel = diver.getDiverLevel();
        DiverLevel dbDiverDiverLevel = dbDiver.getDiverLevel();
        if (diverLevel != null
            && (dbDiverDiverLevel == null
                || dbDiverDiverLevel.ordinal() < diverLevel.ordinal())
                ) {
            dbDiver.setDiverLevel(diverLevel);
        }
        if (isNew) {
            diverDao.save(dbDiver);
        } else {
            diverDao.updateModel(dbDiver);
        }
        setDiverInstructor(dbDiver, diver.getInstructor());
        List<PersonalCard> cards = diver.getCards();
        if (overrideCards) {
            personalCardDao.deleteDiverCards(dbDiver);
            Map<CardEqualityKey, PersonalCard> cardsToAdd = new HashMap<>(cards.size());
            for (PersonalCard card : cards) {
                CardEqualityKey key = new CardEqualityKey(
                        card.getDiverType(), card.getDiverLevel(), card.getCardType()
                );
                cardsToAdd.put(key, card);
            }
            for (PersonalCard card : cardsToAdd.values()) {
                card.setDiver(dbDiver);
                personalCardDao.save(card);
            }
        } else {
            for (PersonalCard card : cards) {
                String cardNumber = card.getNumber();
                PersonalCard dbCard = null;
                if (!StringUtil.isTrimmedEmpty(cardNumber)) {
                    dbCard = personalCardDao.getByNumber(cardNumber);
                }
                boolean isNewCard = dbCard == null;
                if (isNewCard) {
                    dbCard = card;
                } else {
                    dbCard.setFederationName(card.getFederationName());
                    dbCard.setCardType(card.getCardType());
                    dbCard.setDiverLevel(card.getDiverLevel());
                    dbCard.setDiverType(card.getDiverType());
                }
                dbCard.setDiver(dbDiver);
                if (isNewCard) {
                    personalCardDao.save(dbCard);
                } else {
                    personalCardDao.updateModel(dbCard);
                }
            }
        }
    }

    private static final class CardEqualityKey {
        private final DiverType diverType;
        private final DiverLevel diverLevel;
        private final PersonalCardType cardType;

        private CardEqualityKey(DiverType diverType, DiverLevel diverLevel, PersonalCardType cardType) {
            this.diverType = diverType;
            this.diverLevel = diverLevel;
            this.cardType = cardType;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof CardEqualityKey)) {
                return false;
            }
            CardEqualityKey key = (CardEqualityKey) obj;
            return diverType == key.diverType &&
                   diverLevel == key.diverLevel &&
                   cardType == key.cardType;
        }

        @Override
        public int hashCode() {
            return Objects.hash(diverType, diverLevel, cardType);
        }
    }
}
