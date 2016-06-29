package org.cmas.presentation.controller.user.billing.systempay;

/*
vads_action_mode ￼ ￼ 47 X 
vads_amount ￼ ￼ 09 X 
 vads_currency ￼ ￼ 10 X 
vads_cust_email ￼ ￼ 15 C(1)
 vads_order_id ￼ ￼ 13 F
 vads_language ￼ ￼ 12 F
vads_ctx_mode ￼ ￼ 11 X 
vads_page_action ￼ ￼ 46 X 
vads_payment_config ￼ ￼ 07 X
vads_return_mode ￼ ￼ 48 C(2)

 signature ￼ ￼ X
 vads_site_id ￼ ￼ 02 X
  vads_trans_date ￼ ￼ 04 X
 vads_trans_id ￼ ￼ 03 X
 vads_version ￼ ￼ 01 X

vads_capture_delay ￼ ￼ 06 F
 vads_contrib ￼ ￼ 31 F 

 vads_cust_address ￼ ￼ 19 F 
 vads_cust_country ￼ ￼ 22 F 

 vads_cust_name ￼ ￼ 18 F
vads_cust_phone ￼ ￼ 23 F
vads_cust_title ￼ ￼ 17 F
vads_cust_city ￼ ￼ 21 F
 vads_cust_zip ￼ ￼ 20 F 
 vads_cust_id ￼ ￼ 16 F

 vads_order_info ￼ ￼ 14 F 
 vads_order_info2 ￼ ￼ 14 F 
 vads_order_info3 ￼ ￼ 14 F 
 
 vads_payment_cards ￼ ￼ 08 F

 vads_theme_config ￼ ￼ 32 F

 vads_validation_mode ￼ ￼ 05 F

 vads_url_success ￼ ￼ 24 F
 vads_url_referral ￼ ￼ 26 F
 vads_url_refused ￼ ￼ 25 F
 vads_url_cancel ￼ ￼ 27 F
 vads_url_error ￼ ￼ 29 F
 vads_url_return ￼ ￼ 28 F
 vads_user_info ￼ ￼ 61 .
 vads_contracts ￼ ￼ 62 C(3)
 */
@SuppressWarnings({
        "InstanceVariableNamingConvention",
        "InstanceMethodNamingConvention",
        "MethodParameterNamingConvention"})
public class SystempayPaymentRequest {

    //PRODUCTION, TEST
    private String vads_ctx_mode;

    //e.g. euro cents
    private int vads_amount;
    private String vads_currency;

    private String vads_cust_email;
    private String vads_order_id;

    //de, en, zh, es, fr, it, jp
    private String vads_language;

    private String vads_site_id;

    //YYYYMMDDHHMMSS time zone?
    private String vads_trans_date;

    /*
    It is the responsibility of the commercial site to warrant its uniqueness for the current day.
    It must necessarily range from 000000 to 899999. The 900000 to 999999 bracket is not allowed.
    Note: a value with a length under 6 characters generates an error when the payment URL is called.
    Please try and abide by the 6 characters length.
     */
    private String vads_trans_id;

    /*
    The signature will be constituted of all the fields with names starting with the « vads_ » character chain.
    The fields should be sorted in alphabetical order.
    The values of the fields are concatenated with the « + » character.
    Example: when the request parameters are:
- vads_version = V2
- vads_page_action = PAYMENT
- vads_action_mode = INTERACTIVE
- vads_payment_config = SINGLE
- vads_site_id = 12345678
- vads_ctx_mode = TEST
- vads_trans_id = 654321
- vads_trans_date = 20090501193530
- vads_amount = 1524
- vads_currency = 978
and the value of the certificate (depending on the mode) is =1122334455667788
    SHA1(INTERACTIVE+1524+TEST+978+PAYMENT+SINGLE+12345678+20090501193530+654321+V2+1122334455667788)
     */
    private String signature;

    private String vads_auth_result;
    private String vads_card_brand;
    private String vads_card_number;
    private String vads_result;
    private String vads_extra_result;

    public String getVads_ctx_mode() {
        return vads_ctx_mode;
    }

    public void setVads_ctx_mode(String vads_ctx_mode) {
        this.vads_ctx_mode = vads_ctx_mode;
    }

    public int getVads_amount() {
        return vads_amount;
    }

    public void setVads_amount(int vads_amount) {
        this.vads_amount = vads_amount;
    }

    public String getVads_currency() {
        return vads_currency;
    }

    public void setVads_currency(String vads_currency) {
        this.vads_currency = vads_currency;
    }

    public String getVads_cust_email() {
        return vads_cust_email;
    }

    public void setVads_cust_email(String vads_cust_email) {
        this.vads_cust_email = vads_cust_email;
    }

    public String getVads_order_id() {
        return vads_order_id;
    }

    public void setVads_order_id(String vads_order_id) {
        this.vads_order_id = vads_order_id;
    }

    public String getVads_language() {
        return vads_language;
    }

    public void setVads_language(String vads_language) {
        this.vads_language = vads_language;
    }

    public String getVads_site_id() {
        return vads_site_id;
    }

    public void setVads_site_id(String vads_site_id) {
        this.vads_site_id = vads_site_id;
    }

    public String getVads_trans_date() {
        return vads_trans_date;
    }

    public void setVads_trans_date(String vads_trans_date) {
        this.vads_trans_date = vads_trans_date;
    }

    public String getVads_trans_id() {
        return vads_trans_id;
    }

    public void setVads_trans_id(String vads_trans_id) {
        this.vads_trans_id = vads_trans_id;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getVads_auth_result() {
        return vads_auth_result;
    }

    public void setVads_auth_result(String vads_auth_result) {
        this.vads_auth_result = vads_auth_result;
    }

    public String getVads_card_brand() {
        return vads_card_brand;
    }

    public void setVads_card_brand(String vads_card_brand) {
        this.vads_card_brand = vads_card_brand;
    }

    public String getVads_card_number() {
        return vads_card_number;
    }

    public void setVads_card_number(String vads_card_number) {
        this.vads_card_number = vads_card_number;
    }

    public String getVads_result() {
        return vads_result;
    }

    public void setVads_result(String vads_result) {
        this.vads_result = vads_result;
    }

    public String getVads_extra_result() {
        return vads_extra_result;
    }

    public void setVads_extra_result(String vads_extra_result) {
        this.vads_extra_result = vads_extra_result;
    }
}