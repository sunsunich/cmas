package org.cmas.presentation.service.billing;

import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.dao.billing.InvoiceDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.billing.InvoiceStatus;
import org.cmas.presentation.entities.billing.InvoiceType;
import org.cmas.presentation.model.billing.PaymentAddData;
import org.cmas.presentation.model.billing.PaymentAddFormObject;
import org.cmas.presentation.service.mail.MailService;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.Date;

public class BillingServiceImpl implements BillingService {

    private static final Logger LOG = LoggerFactory.getLogger(BillingServiceImpl.class);
    private static final int INVOICE_NUMBER_RAND_PART_LENGTH = 7;
    private static final SecureRandom RND = new SecureRandom();

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private MailService mailService;

    @Autowired
    private TransactionalBillingServiceImpl transactionalBillingService;

    private static final int MAX_ATTEMPTS_CNT = 2;


    @Override
    public boolean paymentAdd(PaymentAddData data, String ip, boolean isConfirmEmail) {
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
        Invoice invoice = invoiceDao.getById(invoiceId);
        Diver diver = invoice.getDiver();
        diver.setHasPayed(true);
        diverDao.updateModel(diver);
        if (isConfirmEmail) {
            try {
                mailService.confirmPayment(invoice);
            } catch (Exception e) {
                LOG.error("error while sending email to user", e);
            }
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

        LOG.error(
                "error while returning to user account user:" + user.getEmail()
                + ", amount:" + amount
        );

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
    public Invoice createInvoice(PaymentAddFormObject fo, User user) {
        BigDecimal amount = new BigDecimal(fo.getAmount());
        InvoiceType type = InvoiceType.valueOf(fo.getPaymentType());
        Invoice invoice = new Invoice();

        invoice.setAmount(amount);
        invoice.setCreateDate(new Date());
        invoice.setInvoiceStatus(InvoiceStatus.NOT_PAID);
        invoice.setInvoiceType(type);
        invoice.setUser(user);
        saveInvoiceAndSetExtId(invoice);
        return invoice;
    }

    private void saveInvoiceAndSetExtId(Invoice invoice) {
        long id = (Long) invoiceDao.save(invoice);
        String externalInvoiceNumberBeg = genRandom(INVOICE_NUMBER_RAND_PART_LENGTH);
        String externalInvoiceNumberEnd = genRandom(INVOICE_NUMBER_RAND_PART_LENGTH);
        invoice.setExternalInvoiceNumber(
                externalInvoiceNumberBeg.substring(0, INVOICE_NUMBER_RAND_PART_LENGTH)
                + id
                + externalInvoiceNumberEnd.substring(0, INVOICE_NUMBER_RAND_PART_LENGTH)
        );
        invoiceDao.updateModel(invoice);
    }

    private String genRandom(int minLength) {
        long minNumber = 10L * (long) (minLength - 1);
        long number = Math.abs(RND.nextLong());
        if (number > Long.MAX_VALUE - minNumber) {
            return String.valueOf(number);
        } else {
            return String.valueOf(minNumber + number);
        }
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

    private void checkAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("amount is less or eq to 0");
        }
    }


}
