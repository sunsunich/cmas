package org.cmas.presentation.service.billing;

import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.user.UserClient;
import org.cmas.presentation.model.billing.PaymentAddData;
import org.cmas.presentation.model.billing.PaymentAddFormObject;

import java.math.BigDecimal;

public interface BillingService {

    Invoice createInvoice(PaymentAddFormObject fo, UserClient user);

    boolean paymentAdd(PaymentAddData data, String ip, boolean isConfirmEmail);

    boolean paymentReturn(long orderId, String ip);

    boolean paymentReturn(UserClient user, BigDecimal amount, long orderId, String ip);

    void paymentError(Invoice invoice) throws Exception;

    PaymentWithdrawResult paymentWithdraw(UserClient user, BigDecimal amount, String ip);

    void orderCreationErrorPaymentReturn(UserClient user, BigDecimal amount, String errorCause);
}
