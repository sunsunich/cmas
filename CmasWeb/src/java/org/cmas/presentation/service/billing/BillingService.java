package org.cmas.presentation.service.billing;

import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.loyalty.PaidFeature;
import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.billing.InvoiceType;
import org.cmas.presentation.model.billing.PaymentAddData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public interface BillingService {

    Invoice createInvoice(List<PaidFeature> features, User user, InvoiceType type);

    boolean paymentAdd(PaymentAddData data, String ip);

    boolean paymentReturn(long orderId, String ip);

    boolean paymentReturn(User user, BigDecimal amount, long orderId, String ip);

    void paymentError(Invoice invoice) throws Exception;

    Invoice createInvoice(List<PaidFeature> features,
                          User user,
                          InvoiceType type,
                          Set<Diver> paidForDivers);

    PaymentWithdrawResult paymentWithdraw(User user, BigDecimal amount, String ip);

    void orderCreationErrorPaymentReturn(User user, BigDecimal amount, String errorCause);
}
