package org.cmas.presentation.service.user;

import org.cmas.Globals;
import org.cmas.backend.DrawCardService;
import org.cmas.backend.ImageStorageManager;
import org.cmas.entities.Country;
import org.cmas.entities.Role;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.user.RegFileDao;
import org.cmas.presentation.dao.user.RegistrationDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.dao.user.sport.NationalFederationDao;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.entities.user.cards.RegFile;
import org.cmas.presentation.model.registration.DiverRegistrationDTO;
import org.cmas.presentation.model.registration.DiverRegistrationFormObject;
import org.cmas.presentation.model.registration.FullDiverRegistrationFormObject;
import org.cmas.presentation.model.registration.RegistrationConfirmFormObject;
import org.cmas.presentation.model.user.UserDetails;
import org.cmas.presentation.service.cards.PersonalCardService;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.presentation.service.sports.NationalFederationService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.presentation.validator.ValidatorUtils;
import org.cmas.remote.ErrorCodes;
import org.cmas.util.ImageUtils;
import org.cmas.util.StringUtil;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.providers.encoding.Md5PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RegistrationServiceImpl implements RegistrationService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MailService mailer;

    @Autowired
    private HibernateSpringValidator validator;

    @Autowired
    private RegistrationDao registrationDao;

    @Autowired
    private Md5PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordService passwordService;

    @Autowired
    private AllUsersService allUsersService;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private RegFileDao regFileDao;

    @Autowired
    private NationalFederationDao nationalFederationDao;

    @Autowired
    private NationalFederationService federationService;

    @Autowired
    private PersonalCardService personalCardService;

    @Autowired
    private ImageStorageManager imageStorageManager;

    @Autowired
    private DrawCardService drawCardService;

    @Autowired
    private DiverService diverService;

    @Autowired
    private DiverMobileService diverMobileService;

    @Override
    public void validate(DiverRegistrationFormObject formObject, Errors errors) {
        validator.validate(formObject, errors);
        String certificateNumber = formObject.getCertificate();
        List<Diver> divers;
        if (StringUtil.isTrimmedEmpty(certificateNumber)) {
            Country country = countryDao.getByCode(formObject.getCountry());
            if (country == null) {
                errors.rejectValue("country", "validation.incorrectField");
            }
            if (errors.hasErrors()) {
                return;
            }
            try {
                divers = federationService.searchDivers(
                        formObject.getFirstName(),
                        formObject.getLastName(),
                        Globals.getDTF().parse(formObject.getDob()),
                        country, false
                );
            } catch (ParseException ignored) {
                errors.rejectValue("dob", "validation.incorrectField");
                return;
            }
        } else {
            if (errors.hasErrors()) {
                return;
            }
            divers = federationService.searchDivers(certificateNumber, false);
        }
        if (divers.isEmpty()) {
            if (!StringUtil.isTrimmedEmpty(certificateNumber)) {
                errors.rejectValue("certificate", "validation.certificateUnknown");
            }
            return;
        }
        for (Diver diver : divers) {
            if (diver.getPreviousRegistrationStatus() == DiverRegistrationStatus.NEVER_REGISTERED) {
                return;
            }
        }
        errors.reject("validation.diverAlreadyRegistered");
    }

    @Override
    public void validateFromMobile(FullDiverRegistrationFormObject formObject, Errors errors) {
        validator.validate(formObject, errors);
        String federationId = formObject.getFederationId();
        if (StringUtil.isTrimmedEmpty(federationId)) {
            errors.rejectValue("federationId", "validation.emptyField");
        } else {
            ValidatorUtils.validateLong(errors, federationId, "federationId", "validation.incorrectField");
        }
        List<String> images = formObject.getImages();
        if (images.isEmpty()) {
            errors.rejectValue("images", "validation.emptyField");
        }
        if (errors.hasErrors()) {
            return;
        }
        Country country = countryDao.getByCode(formObject.getCountryCode());
        if (country == null) {
            errors.rejectValue("countryCode", "validation.incorrectField");
        }
        NationalFederation nationalFederation = nationalFederationDao.getModel(Long.parseLong(federationId));
        if (nationalFederation == null) {
            errors.rejectValue("federationId", "validation.incorrectField");
        }
        for (String image : formObject.getImages()) {
//            log.error("image=" + image);
            ImageUtils.ImageConversionResult imageConversionResult = ImageUtils.base64ToImage(image);
            if (imageConversionResult.image == null) {
                String errorCode = imageConversionResult.errorCode == null ?
                        "validation.incorrectField" :
                        imageConversionResult.errorCode;
                errors.rejectValue("images", errorCode);
            }
        }
        if (errors.hasErrors()) {
            return;
        }
        validateEmail(formObject, errors);
    }

    @Override
    public void validateEmail(FullDiverRegistrationFormObject formObject, Errors errors) {
        String emailFieldName = "email";
        if (!errors.hasFieldErrors(emailFieldName)) {
            String emailValue = formObject.getEmail();
            if (!allUsersService.isEmailUnique(Role.ROLE_DIVER, null, emailValue)) {
                errors.rejectValue(emailFieldName, ErrorCodes.EMAIL_ALREADY_EXISTS);
            }
        }
    }

    @Override
    public void validateConfirm(RegistrationConfirmFormObject formObject, Errors errors) {
        validator.validate(formObject, errors);
        if (errors.hasErrors()) {
            return;
        }
        Registration reg = registrationDao.getBySec(formObject.getSec());
        if (reg == null) {
            errors.rejectValue("sec", "validation.incorrectField");
        }
    }

    @Override
    @Nullable
    @Transactional
    public List<DiverRegistrationDTO> getDiversForRegistration(DiverRegistrationFormObject formObject) {
        String certificateNumber = formObject.getCertificate();
        List<Diver> divers;
        if (StringUtil.isTrimmedEmpty(certificateNumber)) {
            Country country = countryDao.getByCode(formObject.getCountry());
            try {
                Date dob = Globals.getDTF().parse(formObject.getDob());
                divers = federationService.searchDivers(
                        formObject.getFirstName(),
                        formObject.getLastName(),
                        dob,
                        country, true
                );
                //todo fix dob for iranian federation
                if (country.getCode().equals(Country.IRAN_COUNTRY_CODE)) {
                    for (Diver diver : divers) {
                        diver.setDob(dob);
                        diverDao.updateModel(diver);
                    }
                }
            } catch (ParseException e) {
                throw new IllegalStateException(e);
            }
        } else {
            divers = federationService.searchDivers(certificateNumber, true);
        }
        if (divers.isEmpty()) {
            return Collections.emptyList();
        }
        boolean isSendEmail = divers.size() == 1;
        if (isSendEmail) {
            setupCMASDiver(divers.get(0), formObject.getLocale());
        }
        List<DiverRegistrationDTO> dtos = new ArrayList<>(divers.size());
        for (Diver diver : divers) {
            dtos.add(evalDTO(diver, isSendEmail, certificateNumber));
        }
        return dtos;
    }

    private DiverRegistrationDTO evalDTO(Diver diver, boolean isEvalEmail, String certificateNumber) {
        DiverRegistrationDTO dto = new DiverRegistrationDTO();
        dto.setFirstName(diver.getFirstName());
        dto.setLastName(diver.getLastName());
        if (isEvalEmail) {
            String email = diver.getEmail();
            if (!StringUtil.isTrimmedEmpty(email)) {
                dto.setMaskedEmail(email.charAt(0) + "..." + email.substring(email.indexOf('@')));
            }
        } else {
            dto.setDiverId(diver.getId());
        }
        PersonalCard mostValuableCard = null;
        List<PersonalCard> cards = diver.getCards();
        if (cards != null) {
            for (PersonalCard card : cards) {
                if (StringUtil.isTrimmedEmpty(certificateNumber)) {
                    if (mostValuableCard == null) {
                        mostValuableCard = card;
                    } else {
                        if (card.getDiverType() == mostValuableCard.getDiverType()) {
                            if (card.getDiverLevel() == null) {
                                continue;
                            }
                            if (mostValuableCard.getDiverLevel() == null
                                || mostValuableCard.getDiverLevel().ordinal() < card.getDiverLevel().ordinal()) {
                                mostValuableCard = card;
                            }
                        } else if (card.getDiverType() == DiverType.INSTRUCTOR) {
                            mostValuableCard = card;
                        }
                    }
                } else {
                    if (certificateNumber.equals(card.getNumber())) {
                        mostValuableCard = card;
                        break;
                    }
                }
            }
        }
        if (mostValuableCard != null) {
            dto.setCardNumber(mostValuableCard.getPrintNumber());
            dto.setCardImageUrl("/i/" + drawCardService.getFileName(mostValuableCard));
        }
        return dto;
    }

    @Override
    public void setupCMASDiver(Diver diver, Locale locale) {
        String generatedPassword = passwordService.generatePassword();
        String newPasswd = passwordEncoder.encodePassword(generatedPassword, UserDetails.SALT);
        diver.setGeneratedPassword(generatedPassword);
        diver.setPassword(newPasswd);
        if (diver.getLocale() == null) {
            diver.setLocale(locale);
        }
        if (diver.getDiverRegistrationStatus() == DiverRegistrationStatus.NEVER_REGISTERED
            && diver.isPayedAtLeastOnce()
        ) {
            // exceptional case when federation pays for diver
            diver.setDiverRegistrationStatus(DiverRegistrationStatus.CMAS_FULL);
            diver.setPreviousRegistrationStatus(DiverRegistrationStatus.NEVER_REGISTERED);
        } else {
            diver.setDiverRegistrationStatus(DiverRegistrationStatus.CMAS_BASIC);
            diver.setDateLicencePaymentIsDue(
                    new Date(System.currentTimeMillis() + (long) diverService.getDemoTimeDays() * Globals.ONE_DAY_IN_MS)
            );
        }
        diverMobileService.setMobileAuthCode(diver);
        diverDao.updateModel(diver);
        mailer.sendDiverPassword(diver);
    }

    @Override
    public void generateAllCardsImages(Diver diver) {
        if (diver.getPrimaryPersonalCard() == null) {
            personalCardService.generatePrimaryCard(diver, diverDao);
            personalCardService.generateNonPrimaryCardsImages(diver);
        }
    }

    @Override
    @Transactional
    public Registration add(FullDiverRegistrationFormObject formObject) {
        Registration entity = new Registration(new Date());
        String email = StringUtil.lowerCaseEmail(formObject.getEmail());
        entity.setEmail(email);
        entity.setAreaOfInterest(formObject.getAreaOfInterest());
        String country = formObject.getCountryCode();
        if (StringUtil.isTrimmedEmpty(country)) {
            country = formObject.getCountry();
        }
        entity.setCountry(country);
        entity.setFirstName(formObject.getFirstName());
        entity.setLastName(formObject.getLastName());
        try {
            entity.setDob(Globals.getDTF().parse(formObject.getDob()));
        } catch (ParseException e) {
            throw new IllegalStateException(e);
        }
        entity.setLocale(formObject.getLocale());
        entity.setRole(Role.ROLE_DIVER.getName());

        Long id = (Long) registrationDao.save(entity);
        entity.setMd5(passwordEncoder.encodePassword(id + email, UserDetails.SALT));

        String federationId = formObject.getFederationId();
        if (!StringUtil.isTrimmedEmpty(federationId)) {
            NationalFederation federation = nationalFederationDao.getModel(Long.parseLong(federationId));
            if (federation != null) {
                entity.setFederation(federation);
            }
        }
        registrationDao.updateModel(entity);

        for (String image : formObject.getImages()) {
            ImageUtils.ImageConversionResult imageConversionResult = ImageUtils.base64ToImage(image);
            if (imageConversionResult.image != null) {
                RegFile regFile = new RegFile();
                regFile.setRegistration(entity);
                regFile.setFileUrl("");
                regFileDao.save(regFile);
                try {
                    imageStorageManager.storeRegCardImage(regFile, imageConversionResult.image);
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        mailer.sendRegistration(entity);
        return entity;
    }

    @Override
    @Transactional
    public List<Registration> getReadyToRegister() {
        return registrationDao.getReadyToRegister();
    }

    @Override
    @Transactional
    public void delete(long id) {
        Registration reg = registrationDao.getModel(id);
        if (reg != null) {
            registrationDao.deleteModel(reg);
        }
    }
}