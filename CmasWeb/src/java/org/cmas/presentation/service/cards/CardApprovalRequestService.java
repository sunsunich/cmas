package org.cmas.presentation.service.cards;

import org.cmas.entities.cards.CardApprovalRequest;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.entities.user.cards.RegFile;
import org.cmas.presentation.model.cards.CardApprovalRequestEditFormObject;
import org.cmas.presentation.model.cards.CardApprovalRequestFormObject;
import org.cmas.presentation.model.cards.CardApprovalRequestMobileFormObject;
import org.springframework.validation.Errors;

import javax.annotation.Nullable;

/**
 * Created on Oct 19, 2019
 *
 * @author Alexander Petukhov
 */
public interface CardApprovalRequestService {

    void processCardApprovalRequestFromMobile(Errors result,
                                              CardApprovalRequestMobileFormObject cardApprovalRequestFormObject,
                                              Diver diver);

    void processCardApprovalRequest(Errors result,
                                    CardApprovalRequestFormObject cardApprovalRequestFormObject,
                                    Diver diver);

    boolean addCardApprovalRequest(RegFile frontImage, @Nullable RegFile backImage, Diver diver);

    void declineCardApprovalRequest(CardApprovalRequest cardApprovalRequest);

    void approveCardApprovalRequest(CardApprovalRequestEditFormObject formObject, Errors result);
}
