package org.cmas.presentation.controller.user.billing.systempay;

/**
 * Created on Jun 23, 2016
 *
 * @author Alexander Petukhov
 */
public class TransactionSequence {

    private int transactionNumber;

    private int euroCentFirstPayment;

    public int getEuroCentFirstPayment() {
        return euroCentFirstPayment;
    }

    public void setEuroCentFirstPayment(int euroCentFirstPayment) {
        this.euroCentFirstPayment = euroCentFirstPayment;
    }

    public int getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(int transactionNumber) {
        this.transactionNumber = transactionNumber;
    }
}
