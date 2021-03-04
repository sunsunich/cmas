package org.cmas.presentation.service.cards;

import org.cmas.Globals;
import org.cmas.entities.UserFile;
import org.cmas.entities.UserFileType;
import org.cmas.entities.cards.CardApprovalRequest;
import org.cmas.entities.cards.CardApprovalRequestStatus;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.diver.NotificationsCounter;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.cards.CardApprovalRequestDao;
import org.cmas.presentation.dao.cards.PersonalCardDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.dao.user.sport.NationalFederationDao;
import org.cmas.presentation.dao.user.sport.NotificationsCounterDao;
import org.cmas.presentation.entities.user.cards.RegFile;
import org.cmas.presentation.model.cards.CardApprovalRequestEditFormObject;
import org.cmas.presentation.model.cards.CardApprovalRequestFormObject;
import org.cmas.presentation.model.cards.CardApprovalRequestMobileFormObject;
import org.cmas.presentation.model.cards.CommonCardApprovalRequestFormObject;
import org.cmas.presentation.service.UserFileException;
import org.cmas.presentation.service.UserFileService;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.presentation.service.user.DiverService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.presentation.validator.UploadImageValidator;
import org.cmas.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created on Oct 21, 2019
 *
 * @author Alexander Petukhov
 */
public class CardApprovalRequestServiceImpl implements CardApprovalRequestService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private NationalFederationDao nationalFederationDao;

    @Autowired
    private PersonalCardDao personalCardDao;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private NotificationsCounterDao notificationsCounterDao;

    @Autowired
    private CardApprovalRequestDao cardApprovalRequestDao;

    @Autowired
    private HibernateSpringValidator validator;

    @Autowired
    private UserFileService userFileService;

    @Autowired
    private DiverService diverService;

    @Autowired
    private PersonalCardService personalCardService;

    @Autowired
    private MailService mailService;

    @Override
    public void processCardApprovalRequestFromMobile(Errors result,
                                                     CardApprovalRequestMobileFormObject cardApprovalRequestFormObject,
                                                     Diver diver) {
        validateCardApprovalRequest(result, cardApprovalRequestFormObject);
        if (result.hasErrors()) {
            return;
        }
        List<String> images = cardApprovalRequestFormObject.getImages();
        for (int i = 0; i < images.size(); i += 2) {
            CardApprovalRequest cardApprovalRequest = new CardApprovalRequest();
            try {
                setCommonFields(cardApprovalRequest, cardApprovalRequestFormObject, diver);
                String frontImage = images.get(i);
                String backImage = null;
                if (i + 1 < images.size()) {
                    backImage = images.get(i + 1);
                }
                cardApprovalRequest.setFrontImage(
                        processFile(result, diver, frontImage, "images")//, false);
                );
                if (backImage != null) {
                    cardApprovalRequest.setBackImage(
                            processFile(result, diver, backImage, "images")//, true);
                    );
                }
                cardApprovalRequestDao.save(cardApprovalRequest);
                notifyOnNewCardApprovalRequest(cardApprovalRequest);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                if (cardApprovalRequest.getFrontImage() != null) {
                    userFileService.processFileRollback(cardApprovalRequest.getFrontImage());
                }
                if (cardApprovalRequest.getBackImage() != null) {
                    userFileService.processFileRollback(cardApprovalRequest.getBackImage());
                }
                if (!result.hasErrors()) {
                    result.reject("validation.internal");
                }
            }
        }
    }

    @Override
    public void processCardApprovalRequest(Errors result,
                                           CardApprovalRequestFormObject cardApprovalRequestFormObject,
                                           Diver diver) {
        validateCardApprovalRequest(result, cardApprovalRequestFormObject);
        if (result.hasErrors()) {
            return;
        }
        CardApprovalRequest cardApprovalRequest = new CardApprovalRequest();
        try {
            transactionalProcessRequest(result, cardApprovalRequest, cardApprovalRequestFormObject, diver);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (cardApprovalRequest.getFrontImage() != null) {
                userFileService.processFileRollback(cardApprovalRequest.getFrontImage());
            }
            if (cardApprovalRequest.getBackImage() != null) {
                userFileService.processFileRollback(cardApprovalRequest.getBackImage());
            }
            if (!result.hasErrors()) {
                result.reject("validation.internal");
            }
        }
    }

    @Override
    public boolean addCardApprovalRequest(RegFile frontImage, @Nullable RegFile backImage, Diver diver) {
        try {
            CardApprovalRequest cardApprovalRequest = new CardApprovalRequest();
            cardApprovalRequest.setCreateDate(new Date());
            cardApprovalRequest.setDiver(diver);
            cardApprovalRequest.setIssuingFederation(diver.getFederation());
            UserFile userFileFront = userFileService.copyFileFromRegistration(diver, frontImage);
            cardApprovalRequest.setFrontImage(userFileFront);
            if (backImage != null) {
                UserFile userFileBack = userFileService.copyFileFromRegistration(diver, backImage);
                cardApprovalRequest.setBackImage(userFileBack);
            }
            cardApprovalRequestDao.save(cardApprovalRequest);
            notifyOnNewCardApprovalRequest(cardApprovalRequest);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    private void validateCardApprovalRequest(Errors result,
                                             CardApprovalRequestMobileFormObject cardApprovalRequestFormObject
    ) {
        validateCommonCardApprovalRequest(result, cardApprovalRequestFormObject);
        for (String image : cardApprovalRequestFormObject.getImages()) {
            validateImage(result, image, "images");
        }
    }

    private void validateCardApprovalRequest(Errors result,
                                             CardApprovalRequestFormObject cardApprovalRequestFormObject
    ) {
        validateCommonCardApprovalRequest(result, cardApprovalRequestFormObject);
        validateImage(result, cardApprovalRequestFormObject.getFrontImage(), "frontImage");
        validateImage(result, cardApprovalRequestFormObject.getBackImage(), "backImage");
    }

    private void validateCommonCardApprovalRequest(Errors result,
                                                   CommonCardApprovalRequestFormObject cardApprovalRequestFormObject) {
        validator.validate(cardApprovalRequestFormObject, result);
        String federationId = cardApprovalRequestFormObject.getFederationId();
        if (!result.hasFieldErrors("federationId") && !StringUtil.isTrimmedEmpty(federationId)) {
            NationalFederation nationalFederation = nationalFederationDao.getModel(Long.parseLong(federationId));
            if (nationalFederation == null) {
                result.rejectValue("federationId", "validation.incorrectField");
            } else {
                String cardType = cardApprovalRequestFormObject.getCardType();
                if (!result.hasFieldErrors("cardType") && !StringUtil.isTrimmedEmpty(cardType)) {
                    if (!personalCardService.canFederationEditCard(nationalFederation,
                                                                   PersonalCardType.valueOf(cardType))) {
                        //better error message
                        result.rejectValue("cardType", "validation.incorrectField");
                    }
                }
            }
        }
    }

    private static void validateImage(Errors result, MultipartFile multipartFile, String fieldName) {
        String errorCode = UploadImageValidator.validateImage(multipartFile);
        if (errorCode != null) {
            result.rejectValue(fieldName, errorCode);
        }
    }

    private static void validateImage(Errors result, String base64, String fieldName) {
        String errorCode = UploadImageValidator.validateBase64Image(base64);
        if (errorCode != null) {
            result.rejectValue(fieldName, errorCode);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void transactionalProcessRequest(Errors result,
                                            CardApprovalRequest cardApprovalRequest,
                                            CardApprovalRequestFormObject cardApprovalRequestFormObject,
                                            Diver diver) throws Exception {

        setCommonFields(cardApprovalRequest, cardApprovalRequestFormObject, diver);
        cardApprovalRequest.setFrontImage(
                processFile(result, diver, cardApprovalRequestFormObject.getFrontImage(), "frontImage")//, false);
        );
        cardApprovalRequest.setBackImage(
                processFile(result, diver, cardApprovalRequestFormObject.getBackImage(), "backImage")//, true);
        );

        cardApprovalRequestDao.save(cardApprovalRequest);
        notifyOnNewCardApprovalRequest(cardApprovalRequest);
    }

    private void setCommonFields(CardApprovalRequest cardApprovalRequest, CommonCardApprovalRequestFormObject cardApprovalRequestFormObject, Diver diver) {
        cardApprovalRequest.setCreateDate(new Date());
        cardApprovalRequest.setDiver(diver);

        cardApprovalRequest.setFederationName(cardApprovalRequestFormObject.getFederationCardNumber());

        String diverType = cardApprovalRequestFormObject.getDiverType();
        if (!StringUtil.isTrimmedEmpty(diverType)) {
            cardApprovalRequest.setDiverType(DiverType.valueOf(diverType));
        }
        String diverLevel = cardApprovalRequestFormObject.getDiverLevel();
        if (!StringUtil.isTrimmedEmpty(diverLevel)) {
            cardApprovalRequest.setDiverLevel(DiverLevel.valueOf(diverLevel));
        }
        String cardType = cardApprovalRequestFormObject.getCardType();
        if (!StringUtil.isTrimmedEmpty(cardType)) {
            cardApprovalRequest.setCardType(PersonalCardType.valueOf(cardType));
        }
        String validUntil = cardApprovalRequestFormObject.getValidUntil();
        if (!StringUtil.isTrimmedEmpty(validUntil)) {
            try {
                cardApprovalRequest.setValidUntil(Globals.getDTF().parse(validUntil));
            } catch (ParseException ignored) {
                // must be caught in validation before we get here
            }
        }
        String federationId = cardApprovalRequestFormObject.getFederationId();
        cardApprovalRequest.setIssuingFederation(nationalFederationDao.getModel(Long.parseLong(federationId)));
    }

    private void notifyOnNewCardApprovalRequest(CardApprovalRequest cardApprovalRequest) {
//        mailService.sendCardApprovalRequestToAquaLinkAdmin(cardApprovalRequest);
        int notificationsCnt = cardApprovalRequest.getFederationNotificationsCnt();
        if (notificationsCnt == 0) {
            Diver federationAdmin = null;
            NationalFederation federation = cardApprovalRequest.getIssuingFederation();
            if (federation != null) {
                federationAdmin = diverDao.getFederationAdmin(federation);
            }
            if (federationAdmin == null) {
                mailService.sendCardApprovalRequestToCmasHq(cardApprovalRequest);
            } else {
                NotificationsCounter notificationsCounter = notificationsCounterDao.getByDiver(federationAdmin);
                if (notificationsCounter.getFederationInitialCnt() > 0) {
                    mailService.sendCardApprovalRequestToFederation(
                            cardApprovalRequest,
                            federation,
                            federationAdmin
                    );
                }
            }
            cardApprovalRequest.setFederationNotificationsCnt(1);
            cardApprovalRequestDao.updateModel(cardApprovalRequest);
        }
    }

    private UserFile processFile(Errors result,
                                 Diver diver,
                                 MultipartFile multipartFile,
                                 String fieldName
//            , boolean fail
    ) throws Exception {
        try {
            return userFileService.processFile(diver, UserFileType.CARD_APPROVAL_REQUEST, multipartFile);
        } catch (UserFileException e) {
            result.rejectValue(fieldName, e.getErrorCode());
            // throw exception to rollback transaction
            throw e;
        }
    }

    private UserFile processFile(Errors result,
                                 Diver diver,
                                 String base64Data,
                                 String fieldName
//            , boolean fail
    ) throws Exception {
        try {
            return userFileService.processFile(diver, UserFileType.CARD_APPROVAL_REQUEST, base64Data);
        } catch (UserFileException e) {
            result.rejectValue(fieldName, e.getErrorCode());
            // throw exception to rollback transaction
            throw e;
        }
    }

    @Override
    public void declineCardApprovalRequest(CardApprovalRequest cardApprovalRequest) {
        cardApprovalRequest.setStatus(CardApprovalRequestStatus.DECLINED);
        cardApprovalRequestDao.updateModel(cardApprovalRequest);

        mailService.sendCardApprovalRequestDeclined(cardApprovalRequest);
    }

    @Override
    public void approveCardApprovalRequest(CardApprovalRequestEditFormObject formObject, Errors result) {
        validator.validate(formObject, result);
        if (result.hasErrors()) {
            return;
        }
        CardApprovalRequest request = cardApprovalRequestDao.getModel(Long.parseLong(formObject.getRequestId()));
        if (request == null) {
            result.rejectValue("requestId", "validation.incorrectField");
            return;
        }
        String diverType = formObject.getDiverType();
        if (StringUtil.isTrimmedEmpty(diverType)) {
            result.rejectValue("diverType", "validation.emptyField");
        } else {
            request.setDiverType(DiverType.valueOf(diverType));
        }
        String diverLevel = formObject.getDiverLevel();
        if (StringUtil.isTrimmedEmpty(diverLevel)) {
            result.rejectValue("diverLevel", "validation.emptyField");
        } else {
            request.setDiverLevel(DiverLevel.valueOf(diverLevel));
        }
        String cardType = formObject.getCardType();
        if (StringUtil.isTrimmedEmpty(cardType)) {
            result.rejectValue("cardType", "validation.emptyField");
        } else {
            request.setCardType(PersonalCardType.valueOf(cardType));
        }
        if (result.hasErrors()) {
            return;
        }

        String validUntil = formObject.getValidUntil();
        if (!StringUtil.isTrimmedEmpty(validUntil)) {
            try {
                request.setValidUntil(Globals.getDTF().parse(validUntil));
            } catch (ParseException ignored) {
            }
        }
        request.setFederationName(formObject.getFederationCardNumber());

        Diver diver = request.getDiver();
        DiverRegistrationStatus diverRegistrationStatus = diver.getDiverRegistrationStatus();
        boolean isNotCmas = diverRegistrationStatus != DiverRegistrationStatus.CMAS_BASIC
                            && diverRegistrationStatus != DiverRegistrationStatus.CMAS_FULL;
        if (isNotCmas) {
            diverService.addGuestDiverToFederation(request.getIssuingFederation(), diver);
        }
        PersonalCard personalCard = new PersonalCard();
        personalCard.setNumber(request.getFederationName());
        personalCard.setDiver(diver);
        personalCard.setDiverType(request.getDiverType());
        personalCard.setDiverLevel(request.getDiverLevel());
        personalCard.setCardType(request.getCardType());
        personalCard.setFederationName(request.getFederationName());
        personalCard.setValidUntil(request.getValidUntil());
        personalCard.setIssuingFederation(request.getIssuingFederation());
        Long cardId = (Long) personalCardDao.save(personalCard);
        PersonalCard dbCard = personalCardDao.getModel(cardId);
        personalCardService.generateAndSaveCardImage(dbCard.getId());

        diverService.updateDiverTypeAndLevelBasingOnCards(diver, isNotCmas);

        request.setStatus(CardApprovalRequestStatus.APPROVED);
        cardApprovalRequestDao.updateModel(request);
        mailService.sendCardApprovalRequestApproved(dbCard, diverService.getDiverStatusString(diver));
    }
}
