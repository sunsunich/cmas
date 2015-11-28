package org.cmas.presentation.service.billing;

import org.cmas.entities.User;
import org.cmas.entities.UserBalance;
import org.cmas.entities.sport.Sportsman;
import org.cmas.presentation.dao.billing.FinLogDao;
import org.cmas.presentation.dao.billing.InvoiceDao;
import org.cmas.presentation.dao.user.UserBalanceDao;
import org.cmas.presentation.dao.user.UserEventDao;
import org.cmas.presentation.entities.billing.*;
import org.cmas.presentation.entities.user.UserEvent;
import org.cmas.presentation.entities.user.UserEventType;
import org.hibernate.StaleObjectStateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

public class TransactionalBillingServiceImpl {

    @Autowired
    private UserBalanceDao userBalanceDao;

    @Autowired
    private UserEventDao userEventDao;

    @Autowired
    private FinLogDao finLogDao;

    @Autowired
    private InvoiceDao invoiceDao;

    @Transactional
    boolean canPaymentAdd(String ip, BigDecimal amount, long invoiceId) throws
            StaleObjectStateException {
        Invoice invoice = invoiceDao.getById(invoiceId);
        if (invoice.getInvoiceStatus() != InvoiceStatus.NOT_PAID) {
            return false;
        }
        if (invoice.getInvoiceType() == InvoiceType.INTERKASSA
                ) {
            invoice.setAmount(amount);
        }
        invoice.setInvoiceStatus(InvoiceStatus.PAID);
        invoiceDao.saveModel(invoice);

        User user = invoice.getUser();
        simpleAddToBallance(user, amount);

        FinLog finLogRecord = new FinLog(user, amount, OperationType.IN, ip);
        finLogRecord.setDescription(invoice.getExternalInvoiceNumber());
        finLogDao.save(finLogRecord);

        userEventDao.save(
                new UserEvent(UserEventType.MONEY_IN, ip, invoice.getExternalInvoiceNumber(), user)
        );
        return true;
    }

    private void simpleAddToBallance(User user, BigDecimal amount) {
        UserBalance userBalance = user.getUserBalance();
        userBalance.setBalance(
                userBalance.getBalance().add(amount)
        );
        userBalanceDao.updateModel(userBalance);
    }

    @Transactional
    void orderErrorPaymentReturn(Sportsman user, BigDecimal amount, String errorCause) {
        simpleAddToBallance(user, amount);

        FinLog finLogRecord = new FinLog(user, amount, OperationType.RETURN, "");
        finLogRecord.setDescription(errorCause);
        finLogDao.save(finLogRecord);

        userEventDao.save(
                new UserEvent(UserEventType.RETURN, "", errorCause, user)
        );
    }

    @Transactional
    boolean canPaymentReturn(long orderId, String ip) {
        //todo implement
//        Order order = orderDao.getByIdNotRemoved(orderId);
//        if (order == null) {
//            return false;
//        }
//
//        BackendUser user = order.getUser();
//        BigDecimal amount = order.getCartCost();
//        simpleAddToBallance(user, amount);
//
//        order.setDeleted(true);
//        orderDao.updateModel(order);
//
//
//        FinLog finLogRecord = new FinLog(user, amount, OperationType.RETURN, ip);
//        String description = String.valueOf(order.getId());
//        finLogRecord.setDescription(description);
//        finLogDao.save(finLogRecord);
//
//        userEventDao.save(
//                new UserEvent(UserEventType.RETURN, user, ip, description)
//        );

        return true;
    }

    @Transactional
    void paymentReturn(Sportsman user, BigDecimal amount, long orderId, String ip) {

        simpleAddToBallance(user, amount);

        FinLog finLogRecord = new FinLog(user, amount, OperationType.RETURN, ip);
        String description = String.valueOf(orderId);
        finLogRecord.setDescription(description);
        finLogDao.save(finLogRecord);

        userEventDao.save(
                new UserEvent(UserEventType.RETURN, ip, description, user)
        );
    }

    @Transactional
    boolean canPaymentWithdraw(User user, BigDecimal amount, String ip) {
        UserBalance userBalance = user.getUserBalance();
        BigDecimal balance = userBalance.getBalance();
        if (balance.compareTo(amount) < 0) {
            return false;
        }

        userBalance.setBalance(balance.subtract(amount));
        userBalanceDao.updateModel(userBalance);

        FinLog finLogRecord = new FinLog(user, amount, OperationType.PURCHASE, ip);
        finLogRecord.setDescription("PURCHASE");
        finLogDao.save(finLogRecord);

        userEventDao.save(
                new UserEvent(UserEventType.PURCHASE, ip, "PURCHASE", user)
        );

        return true;
    }
}
