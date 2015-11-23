package org.cmas.presentation.service.mail;

import org.cmas.entities.User;
import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.entities.user.Registration;

public interface MailService {

     void confirmRegistrator(Registration reg);

    /**
     * Отправляет пользователю подтверждение о смене email
     * @param user
     */
    void confirmChangeEmail(User user);

    void sendLostPasswd(User user);

    /**
     * Отправляем пользователю сообщение, об успешной активации его в системе
     * @param user
     */
    void regCompleteNotify(BackendUser user);

    void confirmPayment(Invoice invoice);

    void paymentFailed(Invoice invoice);
}
