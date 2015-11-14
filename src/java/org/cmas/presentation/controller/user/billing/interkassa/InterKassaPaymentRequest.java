package org.cmas.presentation.controller.user.billing.interkassa;

public class InterKassaPaymentRequest extends BaseInterKassaData{


    private String ik_success_url;
    private String ik_success_method;
    private String ik_fail_url;
    private String ik_fail_method;
    private String ik_status_url;
    private String ik_status_method;

    public InterKassaPaymentRequest() {
        ik_payment_desc = "Пополнение счета";
        ik_paysystem_alias = "";
    }

    public String getIk_success_url() {
        return ik_success_url;
    }

    public void setIk_success_url(String ik_success_url) {
        this.ik_success_url = ik_success_url;
    }

    public String getIk_success_method() {
        return ik_success_method;
    }

    public void setIk_success_method(String ik_success_method) {
        this.ik_success_method = ik_success_method;
    }

    public String getIk_fail_url() {
        return ik_fail_url;
    }

    public void setIk_fail_url(String ik_fail_url) {
        this.ik_fail_url = ik_fail_url;
    }

    public String getIk_fail_method() {
        return ik_fail_method;
    }

    public void setIk_fail_method(String ik_fail_method) {
        this.ik_fail_method = ik_fail_method;
    }

    public String getIk_status_url() {
        return ik_status_url;
    }

    public void setIk_status_url(String ik_status_url) {
        this.ik_status_url = ik_status_url;
    }

    public String getIk_status_method() {
        return ik_status_method;
    }

    public void setIk_status_method(String ik_status_method) {
        this.ik_status_method = ik_status_method;
    }
}