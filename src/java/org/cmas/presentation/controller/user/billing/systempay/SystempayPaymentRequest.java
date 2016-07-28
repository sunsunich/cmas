package org.cmas.presentation.controller.user.billing.systempay;

import javax.servlet.ServletRequest;
import java.lang.reflect.Method;
import java.util.Enumeration;

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


    private String vads_page_action = "PAYMENT";
    private String vads_payment_config = "SINGLE";
    private String vads_action_mode = "INTERACTIVE";
    private String vads_version = "V2";
    private String vads_capture_delay = "0";
    private String vads_validation_mode = "0";
    private String vads_return_mode = "GET";

    //PRODUCTION, TEST
    private String vads_ctx_mode;

    //e.g. euro cents
    private String vads_amount;
    private String vads_currency;

    private String vads_cust_email;
    private String vads_order_id;

    //de, en, zh, es, fr, it, jp
    private String vads_language;

    private String vads_site_id;

    //YYYYMMDDHHMMSS
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

    private String vads_trans_status;
    private String vads_operation_type;
    private String vads_url_check_src;
    private String vads_auth_result;
    private String vads_card_brand;
    private String vads_card_number;
    private String vads_result;
    private String vads_extra_result;

    public static SystempayPaymentRequest fromServletRequest(ServletRequest request) {
        SystempayPaymentRequest result = new SystempayPaymentRequest();
        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (paramName.isEmpty()) {
                continue;
            }
            try {
                String setterName = "set" + paramName.substring(0, 1).toUpperCase()
                                    + paramName.substring(1, paramName.length());
                Method method = SystempayPaymentRequest.class.getDeclaredMethod(setterName, String.class);
                method.invoke(result, request.getParameter(paramName));
            } catch (Exception ignored) {
            }
        }
        return result;
    }

    public String getVads_trans_status() {
        return vads_trans_status;
    }

    public void setVads_trans_status(String vads_trans_status) {
        this.vads_trans_status = vads_trans_status;
    }

    public String getVads_operation_type() {
        return vads_operation_type;
    }

    public void setVads_operation_type(String vads_operation_type) {
        this.vads_operation_type = vads_operation_type;
    }

    public String getVads_url_check_src() {
        return vads_url_check_src;
    }

    public void setVads_url_check_src(String vads_url_check_src) {
        this.vads_url_check_src = vads_url_check_src;
    }

    public String getVads_ctx_mode() {
        return vads_ctx_mode;
    }

    public void setVads_ctx_mode(String vads_ctx_mode) {
        this.vads_ctx_mode = vads_ctx_mode;
    }

    public String getVads_amount() {
        return vads_amount;
    }

    public void setVads_amount(String vads_amount) {
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

    public String getVads_page_action() {
        return vads_page_action;
    }

    public void setVads_page_action(String vads_page_action) {
        this.vads_page_action = vads_page_action;
    }

    public String getVads_payment_config() {
        return vads_payment_config;
    }

    public void setVads_payment_config(String vads_payment_config) {
        this.vads_payment_config = vads_payment_config;
    }

    public String getVads_action_mode() {
        return vads_action_mode;
    }

    public void setVads_action_mode(String vads_action_mode) {
        this.vads_action_mode = vads_action_mode;
    }

    public String getVads_version() {
        return vads_version;
    }

    public void setVads_version(String vads_version) {
        this.vads_version = vads_version;
    }

    public String getVads_capture_delay() {
        return vads_capture_delay;
    }

    public void setVads_capture_delay(String vads_capture_delay) {
        this.vads_capture_delay = vads_capture_delay;
    }

    public String getVads_validation_mode() {
        return vads_validation_mode;
    }

    public void setVads_validation_mode(String vads_validation_mode) {
        this.vads_validation_mode = vads_validation_mode;
    }

    public String getVads_return_mode() {
        return vads_return_mode;
    }

    public void setVads_return_mode(String vads_return_mode) {
        this.vads_return_mode = vads_return_mode;
    }
}