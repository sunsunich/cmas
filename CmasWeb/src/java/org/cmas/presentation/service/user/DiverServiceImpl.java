package org.cmas.presentation.service.user;

import org.apache.commons.lang.LocaleUtils;
import org.cmas.Globals;
import org.cmas.backend.xls.DiverXlsParser;
import org.cmas.entities.Country;
import org.cmas.entities.Role;
import org.cmas.entities.UserBalance;
import org.cmas.entities.billing.Invoice;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.loyalty.PaidFeature;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.cards.PersonalCardDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.dao.user.sport.NationalFederationDao;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.service.cards.PersonalCardService;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.util.LocaleMapping;
import org.cmas.util.StringUtil;
import org.cmas.util.dao.RunInHibernate;
import org.cmas.util.schedule.Scheduler;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created on Apr 29, 2016
 *
 * @author Alexander Petukhov
 */
public class DiverServiceImpl extends UserServiceImpl<Diver> implements DiverService, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiverServiceImpl.class);

    private int demoTimeDays;

    @Autowired
    private Scheduler scheduler;

    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    @Qualifier("rusDiverXlsParser")
    DiverXlsParser rusDiverXlsParser;

    @Autowired
    @Qualifier("egyptDiverXlsParser")
    DiverXlsParser egyptDiverXlsParser;

    @Autowired
    @Qualifier("iranDiverXlsParser")
    DiverXlsParser iranDiverXlsParser;

    @Autowired
    @Qualifier("singleTableDiverXlsParser")
    DiverXlsParser singleTableDiverXlsParser;

    @Autowired
    private MailService mailService;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private NationalFederationDao nationalFederationDao;

    @Autowired
    DiverDao diverDao;

    @Autowired
    PersonalCardDao personalCardDao;

    @Autowired
    private PersonalCardService personalCardService;

    @Override
    public Diver add(Registration registration, String ip) {
        Diver diver = super.add(registration, ip);
        diver.setDiverType(DiverType.DIVER);
        diver.setDateLicencePaymentIsDue(
                new Date(System.currentTimeMillis() + (long) demoTimeDays * Globals.ONE_DAY_IN_MS)
        );
        diver.setDiverRegistrationStatus(DiverRegistrationStatus.DEMO);
        diver.setAreaOfInterest(registration.getAreaOfInterest());
        diverDao.updateModel(diver);
        return diver;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        scheduleDiverRegistrationStatusUpdate();
    }

    private void scheduleDiverRegistrationStatusUpdate() {
        scheduler.scheduleDaily(new RunInHibernate(sessionFactory) {
            @Override
            public void runTaskInHibernate() {
                diverDao.updateDiverRegistrationStatusOnPaymentDueDate();
            }
        }, 2, 0, TimeZone.getDefault());
    }

    private final Map<Long, UploadDiversTask> fedAdminIdToUploadTask = new ConcurrentHashMap<>();
    private final Map<Long, Lock> fedAdminIdToLock = new ConcurrentHashMap<>();

    @SuppressWarnings({"CallToStringEqualsIgnoreCase", "HardcodedFileSeparator", "CallToStringEquals"})
    @Override
    public String scheduleUploadDivers(Diver fedAdmin, MultipartFile file) throws IOException {
        long adminId = fedAdmin.getId();
        Lock lock = fedAdminIdToLock.get(adminId);
        if (lock == null) {
            synchronized (fedAdminIdToLock) {
                Lock value = new ReentrantLock();
                fedAdminIdToLock.put(adminId, value);
                lock = value;
            }
        }
        lock.lock();
        try {
            UploadDiversTask oldTask = fedAdminIdToUploadTask.get(adminId);
            if (oldTask != null) {
                scheduler.remove(oldTask);
                if (oldTask.future != null && !oldTask.future.isDone()) {
                    oldTask.future.cancel(true);
                    scheduler.purge();
                }
                fedAdminIdToUploadTask.remove(adminId);
            }

            if (file == null) {
                return "validation.emptyField";
            }
            String contentType = file.getContentType();
            if (!"application/vnd.ms-excel".equals(contentType)
                && !"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
                return "validation.xlsFileFormat";
            }

            UploadDiversTask newTask = new UploadDiversTask(this, fedAdmin.getFederation(), file.getInputStream());
            newTask.future = scheduler.schedule(newTask, 0, TimeUnit.MILLISECONDS);
            fedAdminIdToUploadTask.put(adminId, newTask);
        } finally {
            lock.unlock();
        }
        return "";
    }

    @Override
    public UploadDiversTask getUploadDiversTask(long fedAdminId) {
        return fedAdminIdToUploadTask.get(fedAdminId);
    }

    void setDiverInstructor(NationalFederation federation, Diver dbDiver, Diver instructor) {
        Diver dbInstructor = null;
        if (instructor != null) {
            dbInstructor = diverDao.getDiverByCardNumber(federation, instructor.getCards().get(0).getNumber());
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
        Locale locale;
        @SuppressWarnings("unchecked")
        List<Locale> languages = LocaleUtils.languagesByCountry(LocaleMapping.getIso2CountryCode(countryCode));
        if (languages.isEmpty()) {
            locale = Locale.ENGLISH;
        } else {
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
            dbDiver.setDiverRegistrationStatus(DiverRegistrationStatus.NEVER_REGISTERED);
            dbDiver.setPreviousRegistrationStatus(DiverRegistrationStatus.NEVER_REGISTERED);

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
        setDiverInstructor(federation, dbDiver, diver.getInstructor());
        List<PersonalCard> cards = diver.getCards();
        if (overrideCards) {
            personalCardDao.deleteDiverCards(dbDiver);
            Map<CardEqualityKey, PersonalCard> cardsToAdd = new HashMap<>(cards.size());
            for (PersonalCard card : cards) {
                PersonalCardType cardType = card.getCardType();
                if (cardType == PersonalCardType.PRIMARY) {
                    continue;
                }
                CardEqualityKey key = new CardEqualityKey(
                        card.getDiverType(), card.getDiverLevel(), cardType
                );
                cardsToAdd.put(key, card);
            }
            for (PersonalCard card : cardsToAdd.values()) {
                card.setDiver(dbDiver);
                personalCardDao.save(card);
            }
            if (dbDiver.getDiverRegistrationStatus() != DiverRegistrationStatus.NEVER_REGISTERED) {
                personalCardService.generateNonPrimaryCardsImages(dbDiver);
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

    @Override
    public void uploadExistingEgyptianDiver(Diver diver) {
        Country egypt = countryDao.getByCode("EGY");
        Diver dbDiver = diverDao.getByFirstNameLastNameCountry(diver.getFirstName(),
                                                               diver.getLastName(),
                                                               egypt.getCode());
        if (dbDiver == null) {
            LOGGER.error("cannot upload existing diver for Egypt federation, no such diver:"
                         + " firstName = " + diver.getFirstName()
                         + ", lastName = " + diver.getLastName()
                         + ", cardNumber = " + diver.getCards().get(0).getNumber()
                         + ' ' + diver.getDiverType()
                         + ' ' + diver.getDiverLevel()
                         + " from " + diver.getCountry().getName()
            );
            return;
        }
        Country country = countryDao.getByName(diver.getCountry().getName());
        if (country == null) {
            LOGGER.error("country not found for diver:"
                         + " firstName = " + diver.getFirstName()
                         + ", lastName = " + diver.getLastName()
                         + ", cardNumber = " + diver.getCards().get(0).getNumber()
                         + ' ' + diver.getDiverType()
                         + ' ' + diver.getDiverLevel()
                         + " from " + diver.getCountry().getName()
            );
        } else {
            dbDiver.setCountry(country);
        }

        updateDiverCmasData(nationalFederationDao.getByCountry(egypt),
                            dbDiver,
                            diver);
    }

    @Override
    public void updateDiverCmasData(NationalFederation nationalFederation, Diver dbDiver, Diver diverData) {
        PersonalCard primaryCard = dbDiver.getPrimaryPersonalCard();
        if (primaryCard == null) {
            LOGGER.error("primaryCard not found for diver:"
                         + " firstName = " + dbDiver.getFirstName()
                         + ", lastName = " + dbDiver.getLastName()
                         + ' ' + dbDiver.getDiverType()
                         + ' ' + dbDiver.getDiverLevel()
                         + " from " + dbDiver.getCountry().getName()
            );
            return;
        }

        dbDiver.setFederation(nationalFederation);
        boolean typeOrLevelUpdated = false;
        if (dbDiver.getDiverType() != DiverType.INSTRUCTOR) {
            dbDiver.setDiverType(diverData.getDiverType());
            typeOrLevelUpdated = true;
        }
        if (dbDiver.getDiverLevel().ordinal() < diverData.getDiverLevel().ordinal()) {
            dbDiver.setDiverLevel(diverData.getDiverLevel());
            typeOrLevelUpdated = true;
        }

        if (typeOrLevelUpdated) {
            primaryCard.setDiverType(diverData.getDiverType());
            primaryCard.setDiverLevel(diverData.getDiverLevel());
            personalCardDao.updateModel(primaryCard);
            personalCardService.generateAndSaveCardImage(primaryCard.getId());
        }

        for (PersonalCard card : diverData.getCards()) {
            card.setDiver(dbDiver);
            personalCardService.generateAndSaveCardImage(
                    (Long) personalCardDao.save(card)
            );
        }
        switch (dbDiver.getDiverRegistrationStatus()) {
            case NEVER_REGISTERED:
                break;
            case INACTIVE:
                // fall through
            case DEMO:
                dbDiver.setPreviousRegistrationStatus(DiverRegistrationStatus.NEVER_REGISTERED);
                dbDiver.setDiverRegistrationStatus(DiverRegistrationStatus.CMAS_BASIC);
                break;
            case GUEST:
                dbDiver.setPreviousRegistrationStatus(DiverRegistrationStatus.CMAS_BASIC);
                dbDiver.setDiverRegistrationStatus(DiverRegistrationStatus.CMAS_FULL);
                break;
            case CMAS_BASIC:
                break;
            case CMAS_FULL:
                break;
        }
        diverDao.updateModel(dbDiver);
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

    @Override
    public void diverPaidForFeature(Diver diver, Invoice invoice, boolean isConfirmEmail) {
        boolean hasCmasLicenceFeature = false;
        boolean hasGoldFeature = false;
        for (PaidFeature paidFeature : invoice.getRequestedPaidFeatures()) {
            if (paidFeature.getId() == Globals.CMAS_LICENCE_PAID_FEATURE_DB_ID) {
                hasCmasLicenceFeature = true;
            } else if (paidFeature.getId() == Globals.GOLD_MEMBERSHIP_PAID_FEATURE_DB_ID) {
                hasGoldFeature = true;
            }
        }
        boolean isSendEmail = hasGoldFeature;
        if (hasCmasLicenceFeature) {
            Date dateLicencePaymentIsDue = diver.getDateLicencePaymentIsDue();
            long startTime;
            if (dateLicencePaymentIsDue == null || dateLicencePaymentIsDue.getTime() < System.currentTimeMillis()) {
                startTime = System.currentTimeMillis();
            } else {
                startTime = dateLicencePaymentIsDue.getTime();
            }
            diver.setDateLicencePaymentIsDue(new Date(startTime + Globals.getMsInYear()));
            switch (diver.getDiverRegistrationStatus()) {
                case NEVER_REGISTERED:
                    // fall through
                case GUEST:
                    break;
                case INACTIVE:
                    diver.setDiverRegistrationStatus(DiverRegistrationStatus.GUEST);
                    diver.setPreviousRegistrationStatus(DiverRegistrationStatus.INACTIVE);
                    isSendEmail = true;
                    break;
                case DEMO:
                    diver.setDiverRegistrationStatus(DiverRegistrationStatus.GUEST);
                    diver.setPreviousRegistrationStatus(DiverRegistrationStatus.DEMO);
                    isSendEmail = true;
                    break;
                case CMAS_BASIC:
                    diver.setDiverRegistrationStatus(DiverRegistrationStatus.CMAS_FULL);
                    diver.setPreviousRegistrationStatus(DiverRegistrationStatus.CMAS_BASIC);
                    isSendEmail = true;
                    break;
                case CMAS_FULL:
                    isSendEmail = true;
                    break;
            }
        }
        if (hasGoldFeature) {
            diver.setDateGoldStatusPaymentIsDue(new Date(System.currentTimeMillis() + Globals.getMsInYear()));
        }
        if (hasCmasLicenceFeature || hasGoldFeature) {
            diverDao.updateModel(diver);
        }
        if (hasGoldFeature) {
            personalCardService.generateAndSaveCardImage(diver.getPrimaryPersonalCard().getId());
        }
        if (isConfirmEmail && isSendEmail) {
            mailService.confirmPayment(invoice);
        }
    }

    @Required
    public void setDemoTimeDays(int demoTimeDays) {
        this.demoTimeDays = demoTimeDays;
    }

    @Override
    public int getDemoTimeDays() {
        return demoTimeDays;
    }
}
