package org.cmas.presentation.service.cards;

import org.cmas.Globals;
import org.cmas.entities.UserFile;
import org.cmas.entities.UserFileType;
import org.cmas.entities.cards.CardApprovalRequest;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.cards.CardApprovalRequestDao;
import org.cmas.presentation.dao.user.sport.NationalFederationDao;
import org.cmas.presentation.entities.user.cards.RegFile;
import org.cmas.presentation.model.cards.CardApprovalRequestFormObject;
import org.cmas.presentation.service.UserFileException;
import org.cmas.presentation.service.UserFileService;
import org.cmas.presentation.service.mail.MailService;
import org.cmas.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;
import java.util.Date;

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
    private CountryDao countryDao;

    @Autowired
    private CardApprovalRequestDao cardApprovalRequestDao;

    @Autowired
    private UserFileService userFileService;

    @Autowired
    private MailService mailService;

    @Override
    public void processCardApprovalRequest(Errors result,
                                           CardApprovalRequestFormObject cardApprovalRequestFormObject,
                                           Diver diver) {
        CardApprovalRequest cardApprovalRequest = new CardApprovalRequest();
        try {
            transactionalProcessFeedback(result, cardApprovalRequest, cardApprovalRequestFormObject, diver);
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

    @Transactional(rollbackFor = Exception.class)
    public void transactionalProcessFeedback(Errors result,
                                             CardApprovalRequest cardApprovalRequest,
                                             CardApprovalRequestFormObject cardApprovalRequestFormObject,
                                             Diver diver) throws Exception {

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
            cardApprovalRequest.setValidUntil(Globals.getDTF().parse(validUntil));
        }
        String countryCode = cardApprovalRequestFormObject.getCountryCode();
        if (!StringUtil.isTrimmedEmpty(countryCode)) {
            cardApprovalRequest.setIssuingCountry(countryDao.getByCode(countryCode));
        }
        String federationId = cardApprovalRequestFormObject.getFederationId();
        if (!StringUtil.isTrimmedEmpty(federationId)) {
            cardApprovalRequest.setIssuingFederation(nationalFederationDao.getModel(Long.parseLong(federationId)));
        }

        cardApprovalRequest.setFrontImage(
                processFile(result, diver, cardApprovalRequestFormObject.getFrontImage(), "frontImage")//, false);
        );
        cardApprovalRequest.setBackImage(
                processFile(result, diver, cardApprovalRequestFormObject.getBackImage(), "backImage")//, true);
        );

        cardApprovalRequestDao.save(cardApprovalRequest);

        notifyOnNewCardApprovalRequest(cardApprovalRequest);
    }

    private void notifyOnNewCardApprovalRequest(CardApprovalRequest cardApprovalRequest) {
        mailService.sendCardApprovalRequestToAquaLinkAdmin(cardApprovalRequest);

        if (cardApprovalRequest.getIssuingFederation() == null) {
            // todo send to CMAS HQ
        } else {
            // todo send to federation
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
}
