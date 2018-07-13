package org.cmas.presentation.service.billing;

import org.cmas.entities.User;
import org.cmas.entities.fin.PaidFeature;
import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.billing.InvoiceType;
import org.cmas.presentation.model.billing.PaymentAddData;

import java.math.BigDecimal;
import java.util.List;

public interface BillingService {

    Invoice createInvoice(List<PaidFeature> features, User user, InvoiceType type);

    boolean paymentAdd(PaymentAddData data, String ip, boolean isConfirmEmail);

    boolean paymentReturn(long orderId, String ip);

    boolean paymentReturn(User user, BigDecimal amount, long orderId, String ip);

    void paymentError(Invoice invoice) throws Exception;

    PaymentWithdrawResult paymentWithdraw(User user, BigDecimal amount, String ip);

    void orderCreationErrorPaymentReturn(User user, BigDecimal amount, String errorCause);
}
