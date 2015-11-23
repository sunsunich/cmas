package org.cmas.presentation.service.billing;

import org.cmas.entities.sport.Sportsman;
import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.model.billing.PaymentAddData;
import org.cmas.presentation.model.billing.PaymentAddFormObject;

import java.math.BigDecimal;

public interface BillingService {

    Invoice createInvoice(PaymentAddFormObject fo, Sportsman user);

    boolean paymentAdd(PaymentAddData data, String ip, boolean isConfirmEmail);

    boolean paymentReturn(long orderId, String ip);

    boolean paymentReturn(Sportsman user, BigDecimal amount, long orderId, String ip);

    void paymentError(Invoice invoice) throws Exception;

    PaymentWithdrawResult paymentWithdraw(Sportsman user, BigDecimal amount, String ip);

    void orderCreationErrorPaymentReturn(Sportsman user, BigDecimal amount, String errorCause);
}
