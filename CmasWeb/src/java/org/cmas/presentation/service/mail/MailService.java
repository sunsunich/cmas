package org.cmas.presentation.service.mail;

import org.cmas.entities.FeedbackItem;
import org.cmas.entities.User;
import org.cmas.entities.billing.Invoice;
import org.cmas.entities.cards.CardApprovalRequest;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.DiverFriendRequest;
import org.cmas.entities.logbook.LogbookBuddieRequest;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.entities.loyalty.CameraOrder;
import org.cmas.entities.loyalty.InsuranceRequest;
import org.cmas.presentation.entities.user.Registration;

public interface MailService {

    void sendDiverPassword(Diver diver);

    void sendRegistration(Registration reg);

    void regCompleteNotify(User user);

    void sendFriendRequest(DiverFriendRequest friendRequest);

    void sendToInstructorToApprove(LogbookBuddieRequest instructorRequest);

    void sendInstructorApproved(LogbookBuddieRequest instructorRequest);

    void confirmChangeEmail(User user);

    void sendLostPasswd(User user);

    void confirmPayment(Invoice invoice);

    void paymentFailed(Invoice invoice);

    void sendCameraOrderMailToDiver(CameraOrder cameraOrder);

    void sendCameraOrderMailToSubal(CameraOrder cameraOrder);

    void noInsuranceRequestForInvoice(Invoice invoice);

    void sendInsuranceRequestFailed(InsuranceRequest insuranceRequest, String message);

    void sendLogbookEntryChanged(LogbookEntry logbookEntry);

    void sendFeedbackItem(FeedbackItem feedbackItem);

    void sendFeedbackItemToUser(FeedbackItem feedbackItem);

    void sendCardApprovalRequestToAquaLinkAdmin(CardApprovalRequest cardApprovalRequest);

    void sendCardApprovalRequestDeclined(CardApprovalRequest cardApprovalRequest);

    void sendCardApprovalRequestApproved(PersonalCard newCard, String statusStr);

    void cmasMobileAnnounce(Diver diver);
}
