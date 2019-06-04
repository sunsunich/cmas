package org.cmas.presentation.service.mail;

import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.DiverFriendRequest;
import org.cmas.entities.logbook.LogbookBuddieRequest;
import org.cmas.entities.loyalty.CameraOrder;
import org.cmas.presentation.entities.billing.Invoice;
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
}
