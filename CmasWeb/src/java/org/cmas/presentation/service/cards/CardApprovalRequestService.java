package org.cmas.presentation.service.cards;

import org.cmas.entities.diver.Diver;
import org.cmas.presentation.model.cards.CardApprovalRequestFormObject;
import org.springframework.validation.Errors;

/**
 * Created on Oct 19, 2019
 *
 * @author Alexander Petukhov
 */
public interface CardApprovalRequestService {
    void processCardApprovalRequest(Errors result,
                                    CardApprovalRequestFormObject cardApprovalRequestFormObject,
                                    Diver diver);
}
