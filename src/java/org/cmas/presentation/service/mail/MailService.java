package org.cmas.presentation.service.mail;

import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.user.Registration;
import org.cmas.presentation.entities.user.UserClient;

public interface MailService {

     void confirmRegistrator(Registration reg);

    /**
     * Отправляет пользователю подтверждение о смене email
     * @param user
     */
    void confirmChangeEmail(UserClient user);

    void sendLostPasswd(UserClient user);

    /**
     * Отправляем пользователю сообщение, об успешной активации его в системе
     * @param user
     */
    void regCompleteNotify(UserClient user);

    void confirmPayment(Invoice invoice);

    void paymentFailed(Invoice invoice);
}
