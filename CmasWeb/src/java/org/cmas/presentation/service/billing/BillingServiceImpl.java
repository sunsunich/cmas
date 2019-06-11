package org.cmas.presentation.service.billing;

import org.cmas.entities.User;
import org.cmas.entities.billing.Invoice;
import org.cmas.entities.billing.InvoiceStatus;
import org.cmas.entities.billing.InvoiceType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.loyalty.PaidFeature;
import org.cmas.presentation.dao.billing.InvoiceDao;
import org.cmas.presentation.model.billing.PaymentAddData;
import org.cmas.util.random.Randomazer;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BillingServiceImpl implements BillingService {

    private static final Logger LOG = LoggerFactory.getLogger(BillingServiceImpl.class);
    private static final int INVOICE_NUMBER_RAND_PART_LENGTH = 7;

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private TransactionalBillingServiceImpl transactionalBillingService;

    @Autowired
    private Randomazer randomazer;

    private static final int MAX_ATTEMPTS_CNT = 2;

    @Transactional
    @Override
    public boolean paymentAdd(PaymentAddData data, String ip) {
        BigDecimal amount = new BigDecimal(data.getAmount());

        checkAmount(amount);

        long invoiceId = data.getInvoiceId();
        int attemptCnt = 0;
        while (attemptCnt < MAX_ATTEMPTS_CNT) {
            try {
                if (!transactionalBillingService.canPaymentAdd(ip, amount, invoiceId)) {
                    return false;
                }
                break;
            } catch (StaleObjectStateException ignored) {
                attemptCnt++;
            }
        }
        if (attemptCnt >= MAX_ATTEMPTS_CNT) {
//            Invoice invoice = invoiceDao.getById(invoiceId);

//            try {
            //todo fix hibernate session here
//                mailService.paymentFailed(invoice);
//            } catch (Exception ex) {
//                LOG.error("error while sending email to user", ex);
//            }
            return false;
        }
        return true;
    }


    @Override
    public void orderCreationErrorPaymentReturn(User user, BigDecimal amount, String errorCause) {

        checkAmount(amount);

        int attemptCnt = 0;
        int returnAttemptCnt = MAX_ATTEMPTS_CNT * 3;
        while (attemptCnt < returnAttemptCnt) {
            try {
                transactionalBillingService.orderErrorPaymentReturn(user, amount, errorCause);
                return;
            } catch (StaleObjectStateException ignored) {
                attemptCnt++;
            }
        }

        LOG.error("error while returning to user account user:{}, amount:{}", user.getEmail(), amount);

    }


    @Override
    public boolean paymentReturn(long orderId, String ip) {
        int attemptCnt = 0;
        while (attemptCnt < MAX_ATTEMPTS_CNT) {
            try {
                return transactionalBillingService.canPaymentReturn(orderId, ip);
            } catch (StaleObjectStateException ignored) {
                attemptCnt++;
            }
        }
        return false;
    }

    @Override
    public boolean paymentReturn(User user, BigDecimal amount, long orderId, String ip) {
        checkAmount(amount);

        int attemptCnt = 0;
        while (attemptCnt < MAX_ATTEMPTS_CNT) {
            try {
                transactionalBillingService.paymentReturn(user, amount, orderId, ip);
                return true;
            } catch (StaleObjectStateException ignored) {
                attemptCnt++;
            }
        }
        return false;
    }

    @Override
    public void paymentError(Invoice invoice) throws Exception {
        invoice.setInvoiceStatus(InvoiceStatus.ERROR);
        invoiceDao.saveModel(invoice);
    }

    @Override
    public Invoice createInvoice(List<PaidFeature> features, User user, InvoiceType type) {
        return createInvoice(features, user, type, null);
    }

    @Override
    public Invoice createInvoice(List<PaidFeature> features,
                                 User user,
                                 InvoiceType type,
                                 Set<Diver> paidForDivers) {
        Invoice invoice = new Invoice();
        BigDecimal amount = BigDecimal.ZERO;
        for (PaidFeature paidFeature : features) {
            amount = amount.add(paidFeature.getPrice());
        }
        if (paidForDivers != null) {
            amount = amount.multiply(new BigDecimal(paidForDivers.size()));
            invoice.setPaidForDivers(new HashSet<>(paidForDivers));
        }

        invoice.setAmount(amount);
        invoice.setCreateDate(new Date());
        invoice.setInvoiceStatus(InvoiceStatus.NOT_PAID);
        invoice.setInvoiceType(type);
        invoice.setUser(user);
        invoice.setRequestedPaidFeatures(features);
        saveInvoiceAndSetExtId(invoice);
        return invoice;
    }

    private void saveInvoiceAndSetExtId(Invoice invoice) {
        long id = (Long) invoiceDao.save(invoice);
        invoice.setExternalInvoiceNumber(
                randomazer.generateRandomStringByUniqueId(id, INVOICE_NUMBER_RAND_PART_LENGTH));
        invoiceDao.updateModel(invoice);
    }

    @Override
    public PaymentWithdrawResult paymentWithdraw(User user, BigDecimal amount, String ip) {

        checkAmount(amount);

        int attemptCnt = 0;
        while (attemptCnt < MAX_ATTEMPTS_CNT) {

            try {
                if (!transactionalBillingService.canPaymentWithdraw(user, amount, ip)) {
                    return PaymentWithdrawResult.NOT_ENOUGH;
                }
                break;
            } catch (StaleObjectStateException ignored) {
                attemptCnt++;
            }
        }
        if (attemptCnt >= MAX_ATTEMPTS_CNT) {
            return PaymentWithdrawResult.ERROR;
        }
        return PaymentWithdrawResult.OK;
    }

    private static void checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("amount is less or eq to 0");
        }
    }


}
