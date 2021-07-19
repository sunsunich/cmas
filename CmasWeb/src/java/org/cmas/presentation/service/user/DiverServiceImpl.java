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
import org.cmas.entities.diver.AreaOfInterest;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.diver.NotificationsCounter;
import org.cmas.entities.loyalty.PaidFeature;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.cards.PersonalCardDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.dao.user.sport.NationalFederationDao;
import org.cmas.presentation.dao.user.sport.NotificationsCounterDao;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.entities.user.cards.RegFile;
import org.cmas.presentation.model.user.DiverFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.cards.CardApprovalRequestService;
import org.cmas.presentation.service.cards.PersonalCardService;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.util.LocaleMapping;
import org.cmas.util.StringUtil;
import org.cmas.util.dao.RunInHibernate;
import org.cmas.util.schedule.Scheduler;
import org.hibernate.SessionFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.providers.encoding.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.text.ParseException;
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
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MailService mailService;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private NationalFederationDao nationalFederationDao;

    @Autowired
    DiverDao diverDao;

    @Autowired
    private NotificationsCounterDao notificationsCounterDao;

    @Autowired
    PersonalCardDao personalCardDao;

    @Autowired
    PersonalCardService personalCardService;

    @Autowired
    private CardApprovalRequestService cardApprovalRequestService;

    @Autowired
    private DiverMobileService diverMobileService;

    @Override
    public Diver add(Registration registration, String ip) {
        Diver diver = super.add(registration, ip);
        diver.setDiverType(DiverType.DIVER);
        diver.setDateLicencePaymentIsDue(
                new Date(System.currentTimeMillis() + (long) demoTimeDays * Globals.ONE_DAY_IN_MS)
        );
        diver.setDiverRegistrationStatus(DiverRegistrationStatus.DEMO);
        diver.setAreaOfInterest(registration.getAreaOfInterest());
        //todo why set federation to demo???
        diver.setFederation(registration.getFederation());
        diverDao.updateModel(diver);
        NotificationsCounter notificationsCounter = new NotificationsCounter();
        notificationsCounter.setDiver(diver);
        notificationsCounterDao.save(notificationsCounter);

        boolean imagesTransferSuccess = true;
        List<RegFile> images = registration.getImages();
        for (int i = 0; i < images.size(); i += 2) {
            RegFile frontImage = images.get(i);
            RegFile backImage = null;
            if (i + 1 < images.size()) {
                backImage = images.get(i + 1);
            }
            boolean imageTransferSuccess = cardApprovalRequestService.addCardApprovalRequest(
                    frontImage, backImage, diver);
            imagesTransferSuccess = imagesTransferSuccess && imageTransferSuccess;
        }
        if (imagesTransferSuccess) {
            registrationDao.deleteModel(registration);
        }
        return diver;
    }

    @Override
    public void afterPropertiesSet() {
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
//            String contentType = file.getContentType();
//            if (!"application/vnd.ms-excel".equals(contentType)
//                && !"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
//                return "validation.xlsFileFormat";
//            }
            UploadDiversTask newTask = new UploadDiversTask(this, fedAdmin.getFederation(), file.getInputStream());
            newTask.future = scheduler.schedule(newTask, 0L, TimeUnit.MILLISECONDS);
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

    void uploadDiverFromXls(NationalFederation federation, Diver diver) {
        Diver dbDiver = saveOrUpdateDiver(federation, diver);
        saveOrUpdateCards(federation, dbDiver, diver.getCards());
    }

    void finalizeDiverFromXls(NationalFederation federation, DiverModificationData diverModificationData) {
        updateDiverTypeAndLevelBasingOnCards(diverModificationData.dbDiver, false);
        setDiverInstructor(federation, diverModificationData.dbDiver, diverModificationData.instructor);
    }

    boolean uploadEgyptianDiver(Diver diver) {
        Country egypt = countryDao.getByCode(Country.EGYPT_COUNTRY_CODE);
        Country country = countryDao.getByName(diver.getCountry().getName());
        String firstName = diver.getFirstName();
        String lastName = diver.getLastName();
        List<PersonalCard> cards = diver.getCards();
        PersonalCard personalCard = cards.get(0);
        String primaryCardNumber = personalCard.getNumber();
        DiverType diverType = personalCard.getDiverType();
        DiverLevel diverLevel = personalCard.getDiverLevel();
        if (country == null) {
            //noinspection StringConcatenationArgumentToLogCall,StringConcatenation,MagicCharacter
            LOGGER.error("country not found for diver:"
                         + " firstName = " + firstName
                         + ", lastName = " + lastName
                         + ", cardNumber = " + primaryCardNumber
                         + ' ' + diverType
                         + ' ' + diverLevel
                         + " from " + diver.getCountry().getName()
            );
            return false;
        }
        NationalFederation federation = nationalFederationDao.getByCountry(egypt).get(0);
        String email = diver.getEmail();
        Diver dbDiver = diverDao.getByEmail(email);
        if (dbDiver == null) {
            //registering new user
            diver.setDateReg(new Date());
            diver.setAreaOfInterest(AreaOfInterest.SCUBA_DIVING.name());
            dbDiver = saveOrUpdateDiver(federation, diver);
        }
        Date dob = diver.getDob();
        String dbFullNameNoSpaces = getNameNoSpaces(dbDiver.getFirstName()) + getNameNoSpaces(dbDiver.getLastName());
        String fullNameNoSpaces = getNameNoSpaces(firstName) + getNameNoSpaces(lastName);
        if (!dbFullNameNoSpaces.equals(fullNameNoSpaces)) {
            LOGGER.error("cannot upload existing diver for Egypt federation, diver data mismatch:"
                         + " email = " + email
                         + " dbFullNameNoSpaces = " + dbFullNameNoSpaces
                         + ", fullNameNoSpaces = " + fullNameNoSpaces
                         + ", dob = " + dob
                         + ", cardNumber = " + primaryCardNumber
                         + ' ' + diverType
                         + ' ' + diverLevel
                         + " from " + country.getName()
            );
            return false;
        }
        String formattedDob = Globals.getDTF().format(dob);
        String formattedDobDb = Globals.getDTF().format(dbDiver.getDob());
        if (!formattedDobDb.equals(formattedDob)) {
            LOGGER.error("cannot upload existing diver for Egypt federation, diver data mismatch:"
                         + " email = " + email
                         + ", fullNameNoSpaces = " + fullNameNoSpaces
                         + " dbDob = " + formattedDobDb
                         + ", dob = " + formattedDob
                         + ", cardNumber = " + primaryCardNumber
                         + ' ' + diverType
                         + ' ' + diverLevel
                         + " from " + country.getName()
            );
            return false;
        }

        dbDiver.setCountry(country);
        dbDiver.setFirstName(StringUtil.correctSpaceCharAndTrim(firstName));
        dbDiver.setLastName(StringUtil.correctSpaceCharAndTrim(lastName));

        String generatedPassword = diver.getGeneratedPassword();
        if (StringUtil.isTrimmedEmpty(dbDiver.getPassword())
            && !StringUtil.isTrimmedEmpty(generatedPassword)) {
            dbDiver.setGeneratedPassword(generatedPassword);
            dbDiver.setPassword(
                    passwordEncoder.encodePassword(generatedPassword, UserDetails.SALT)
            );
        }
        diverDao.updateModel(dbDiver);

        saveOrUpdateCards(federation, dbDiver, cards);
        return true;
    }

    @NotNull
    private static String getNameNoSpaces(String firstName) {
        return StringUtil.correctSpaceCharAndTrim(firstName).replaceAll(" ", "");
    }

    void finalizeExistingEgyptianDiver(NationalFederation federation, DiverModificationData diverModificationData) {
        personalCardService.generatePrimaryCard(diverModificationData.dbDiver, diverDao, false);
        updateDiverTypeAndLevelBasingOnCards(diverModificationData.dbDiver, true);
        Diver dbDiver = diverModificationData.dbDiver;
        // egyptians have paid
        dbDiver.setPreviousRegistrationStatus(DiverRegistrationStatus.CMAS_BASIC);
        dbDiver.setDiverRegistrationStatus(DiverRegistrationStatus.CMAS_FULL);
        // dao update in the method below
        addGuestDiverToFederation(federation, dbDiver);
    }

    @Override
    public void uploadSingleDiver(NationalFederation federation, Diver diver) {
        Diver dbDiver = saveOrUpdateDiver(federation, diver);
        List<PersonalCard> cards = diver.getCards();
        setDiverInstructor(federation, dbDiver, diver.getInstructor());
        personalCardDao.deleteDiverCards(dbDiver);
        Map<CardEqualityKey, PersonalCard> cardsToAdd = new HashMap<>(cards.size());
        for (PersonalCard card : cards) {
            if (!personalCardService.canFederationEditCard(federation, card.getCardType())) {
                continue;
            }
            PersonalCardType cardType = card.getCardType();
            if (cardType == PersonalCardType.PRIMARY) {
                continue;
            }
            DiverLevel diverLevel = card.getDiverLevel();
            if (diverLevel == null) {
                card.setDiverLevel(DiverLevel.ONE_STAR);
            }
            @SuppressWarnings("ObjectAllocationInLoop")
            CardEqualityKey key = new CardEqualityKey(
                    card.getDiverType(), card.getDiverLevel(), cardType
            );
            cardsToAdd.put(key, card);
        }
        for (PersonalCard card : cardsToAdd.values()) {
            card.setDiver(dbDiver);
            card.setIssuingFederation(federation);
            personalCardDao.save(card);
        }
        updateDiverTypeAndLevelBasingOnCards(dbDiver, false);
        if (dbDiver.getDiverRegistrationStatus() != DiverRegistrationStatus.NEVER_REGISTERED) {
            personalCardService.generateNonPrimaryCardsImages(dbDiver);
        }
    }


    @NotNull
    private Diver saveOrUpdateDiver(NationalFederation federation, Diver diver) {
        Country country = federation.getCountry();
        Locale locale;
        @SuppressWarnings("unchecked")
        List<Locale> languages = LocaleUtils.languagesByCountry(LocaleMapping.getIso2CountryCode(country.getCode()));
        if (languages.isEmpty()) {
            locale = Locale.ENGLISH;
        } else {
            locale = languages.get(0);
        }
        String email = StringUtil.lowerCaseEmail(diver.getEmail());
        Diver dbDiver = diverDao.getByEmailForAdmin(email);
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
        dbDiver.setEnabled(true);
        dbDiver.setDateEdited(new Date());
        if (isNew) {
            Long diverId = (Long) diverDao.save(dbDiver);
            Diver loadedDiver = diverDao.getById(diverId);
            NotificationsCounter notificationsCounter = new NotificationsCounter();
            notificationsCounter.setDiver(loadedDiver);
            notificationsCounterDao.save(notificationsCounter);
        } else {
            diverDao.updateModel(dbDiver);
        }
        return dbDiver;
    }

    private void saveOrUpdateCards(NationalFederation federation, Diver dbDiver, Iterable<PersonalCard> cards) {
        for (PersonalCard card : cards) {
            if (!personalCardService.canFederationEditCard(federation, card.getCardType())) {
                continue;
            }
            String cardNumber = card.getNumber();
            PersonalCard dbCard = null;
            if (!StringUtil.isTrimmedEmpty(cardNumber)) {
                dbCard = personalCardDao.getByNumber(federation, cardNumber);
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

    private void setDiverInstructor(NationalFederation federation, Diver dbDiver, Diver instructor) {
        Diver dbInstructor = null;
        if (instructor != null) {
            dbInstructor = diverDao.getDiverByCardNumber(federation, instructor.getCards().get(0).getNumber());
        }
        if (dbDiver.getInstructor() == null && dbInstructor != null) {
            dbDiver.setInstructor(dbInstructor);
        }
        diverDao.updateModel(dbDiver);
    }

    @Override
    public void updateDiverTypeAndLevelBasingOnCards(@Nonnull Diver dbDiver, boolean forceUpdate) {
        DiverType dbDiverType = dbDiver.getDiverType();
        DiverLevel dbDiverDiverLevel = dbDiver.getDiverLevel();
        PersonalCard maxNationalCard = personalCardService.getMaxNationalCard(dbDiver);
        if (maxNationalCard == null) {
            maxNationalCard = personalCardService.getMaxCard(dbDiver);
            if (maxNationalCard == null) {
                return;
            }
        }
        DiverType newDiverType = maxNationalCard.getDiverType();
        boolean typeOrLevelUpdated = false;
        if (newDiverType != null && dbDiverType != newDiverType) {
            dbDiver.setDiverType(newDiverType);
            typeOrLevelUpdated = true;
        }
        DiverLevel newDiverLevel = maxNationalCard.getDiverLevel();
        if (newDiverLevel != null && newDiverType == dbDiver.getDiverType() && dbDiverDiverLevel != newDiverLevel) {
            dbDiver.setDiverLevel(newDiverLevel);
            typeOrLevelUpdated = true;
        }
        if (typeOrLevelUpdated || forceUpdate) {
            diverDao.updateModel(dbDiver);
            updatePrimaryCard(dbDiver);
        }
    }

    private void updatePrimaryCard(Diver dbDiver) {
        DiverType dbDiverType = dbDiver.getDiverType();
        DiverLevel dbDiverDiverLevel = dbDiver.getDiverLevel();
        PersonalCard primaryCard = dbDiver.getPrimaryPersonalCard();
        if (primaryCard == null) {
            //noinspection StringConcatenationArgumentToLogCall,StringConcatenation,MagicCharacter
            LOGGER.error("primaryCard not found for diver:"
                         + " firstName = " + dbDiver.getFirstName()
                         + ", lastName = " + dbDiver.getLastName()
                         + ' ' + dbDiverType
                         + ' ' + dbDiverDiverLevel
                         + " from " + dbDiver.getCountry().getName()
            );
            return;
        }
        primaryCard.setDiverType(dbDiverType);
        primaryCard.setDiverLevel(dbDiverDiverLevel);
        personalCardDao.updateModel(primaryCard);
        personalCardService.generateAndSaveCardImage(primaryCard.getId());
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
    public void addGuestDiverToFederation(NationalFederation federation, Diver dbDiver) {
        dbDiver.setFederation(federation);
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
        diverMobileService.setMobileAuthCode(dbDiver);
        diverDao.updateModel(dbDiver);
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
            diver.setPayedAtLeastOnce(true);
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

    @Override
    public void editDiver(DiverFormObject formObject, Diver diver) {
        diver.setAreaOfInterest(formObject.getAreaOfInterest());
        Country country = countryDao.getByCode(formObject.getCountryCode());
        diver.setCountry(country);
        DiverRegistrationStatus diverRegistrationStatus = diver.getDiverRegistrationStatus();
        if (diverRegistrationStatus != DiverRegistrationStatus.CMAS_BASIC &&
            diverRegistrationStatus != DiverRegistrationStatus.CMAS_FULL
        ) {
            diver.setFirstName(formObject.getFirstName());
            diver.setLastName(formObject.getLastName());
            try {
                diver.setDob(Globals.getDTF().parse(formObject.getDob()));
            } catch (ParseException ignored) {
            }
        }
        diverDao.updateModel(diver);
    }

    @Override
    public String getDiverStatusString(Diver diver) {
        // //3 star Free dive  Instructor  CMAS Basic
        String level = "";
        switch (diver.getDiverLevel()) {
            case ONE_STAR:
                level = "1 star";
                break;
            case TWO_STAR:
                level = "2 star";
                break;
            case THREE_STAR:
                level = "3 star";
                break;
            case FOUR_STAR:
                level = "4 star";
                break;
        }
        boolean isApnea = false;
        for (PersonalCard card : diver.getCards()) {
            if (card.getCardType() == PersonalCardType.APNOEA) {
                isApnea = true;
                break;
            }
        }
        String type = "";
        switch (diver.getDiverType()) {
            case DIVER:
                type = "Diver";
                break;
            case INSTRUCTOR:
                type = "Instructor";
                break;
        }
        String status = "";
        switch (diver.getDiverRegistrationStatus()) {
            case NEVER_REGISTERED:
            case INACTIVE:
                status = "Inactive";
                break;
            case DEMO:
                status = "Demo";
                break;
            case GUEST:
                status = "AquaLink";
                break;
            case CMAS_BASIC:
                status = "CMAS Basic";
                break;
            case CMAS_FULL:
                status = "CMAS Full";
                break;
        }
        return level + ' '
               + (isApnea ? "Free dive" : "")
               + type + " "
               + status
                ;
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
