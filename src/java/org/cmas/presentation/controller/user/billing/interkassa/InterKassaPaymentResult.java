package org.cmas.presentation.controller.user.billing.interkassa;

public class InterKassaPaymentResult extends BaseInterKassaData {


    private String ik_payment_timestamp;

    private String ik_payment_state;

    private String ik_trans_id;

    private String ik_currency_exch;

    private String ik_fees_payer;

    private String ik_sign_hash;

    public String getIk_payment_timestamp() {
        return ik_payment_timestamp;
    }

    public void setIk_payment_timestamp(String ik_payment_timestamp) {
        this.ik_payment_timestamp = ik_payment_timestamp;
    }

    public String getIk_payment_state() {
        return ik_payment_state;
    }

    public void setIk_payment_state(String ik_payment_state) {
        this.ik_payment_state = ik_payment_state;
    }

    public String getIk_trans_id() {
        return ik_trans_id;
    }

    public void setIk_trans_id(String ik_trans_id) {
        this.ik_trans_id = ik_trans_id;
    }

    public String getIk_currency_exch() {
        return ik_currency_exch;
    }

    public void setIk_currency_exch(String ik_currency_exch) {
        this.ik_currency_exch = ik_currency_exch;
    }

    public String getIk_fees_payer() {
        return ik_fees_payer;
    }

    public void setIk_fees_payer(String ik_fees_payer) {
        this.ik_fees_payer = ik_fees_payer;
    }

    public String getIk_sign_hash() {
        return ik_sign_hash;
    }

    public void setIk_sign_hash(String ik_sign_hash) {
        this.ik_sign_hash = ik_sign_hash;
    }

    @Override
    public String toString() {
        return "InterKassaPaymentResult{" +
                "ik_payment_timestamp='" + ik_payment_timestamp + '\'' +
                ", ik_payment_state='" + ik_payment_state + '\'' +
                ", ik_trans_id='" + ik_trans_id + '\'' +
                ", ik_currency_exch='" + ik_currency_exch + '\'' +
                ", ik_fees_payer='" + ik_fees_payer + '\'' +
                ", ik_sign_hash='" + ik_sign_hash + '\'' +
                "} " + super.toString();
    }
}