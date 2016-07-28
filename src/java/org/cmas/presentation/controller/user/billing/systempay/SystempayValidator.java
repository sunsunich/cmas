package org.cmas.presentation.controller.user.billing.systempay;

import org.cmas.Globals;
import org.cmas.presentation.controller.user.billing.PaySystemValidator;
import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.billing.InvoiceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;

import javax.servlet.ServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.TreeSet;

public class SystempayValidator extends PaySystemValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystempayValidator.class);

    @Override
    public boolean supports(Class clazz) {
        return SystempayPaymentRequest.class.equals(clazz);
    }

    private void checkEmpty(SystempayPaymentRequest data, Errors errors) {
        checkEmptyStringField("vads_trans_status", data.getVads_trans_status(), errors);
        checkEmptyStringField("vads_capture_delay", data.getVads_capture_delay(), errors);
        checkEmptyStringField("vads_url_check_src", data.getVads_url_check_src(), errors);
        checkEmptyStringField("vads_operation_type", data.getVads_operation_type(), errors);
        checkEmptyStringField("vads_payment_config", data.getVads_payment_config(), errors);
        checkEmptyStringField("vads_ctx_mode", data.getVads_ctx_mode(), errors);
        checkEmptyStringField("vads_amount", data.getVads_amount(), errors);
        checkEmptyStringField("vads_currency", data.getVads_currency(), errors);
        checkEmptyStringField("vads_cust_email", data.getVads_cust_email(), errors);
        checkEmptyStringField("vads_order_id", data.getVads_order_id(), errors);
        checkEmptyStringField("vads_language", data.getVads_language(), errors);
        checkEmptyStringField("vads_site_id", data.getVads_site_id(), errors);
        checkEmptyStringField("vads_trans_date", data.getVads_trans_date(), errors);
        checkEmptyStringField("vads_trans_id", data.getVads_trans_id(), errors);
        checkEmptyStringField("signature", data.getSignature(), errors);
    }

    public String createSignature(ServletRequest request) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Collection<String> vadsFields = new TreeSet<>();
        Enumeration<String> paramNames = request.getParameterNames();
        // take and sort the fields starting with vads_* alphabetically
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            if (paramName.startsWith("vads_")) {
                vadsFields.add(paramName);
            }
        }
        // Compute the signature
        String sep = Sha.SEPARATOR;
        StringBuilder sb = new StringBuilder();
        for (String vadsParamName : vadsFields) {
            String vadsParamValue = request.getParameter(vadsParamName);
            if (vadsParamValue != null) {
                sb.append(vadsParamValue);
            }
            sb.append(sep);
        }
        sb.append(paySystemSettings.getSystempayCertificate());
        return Sha.encode(sb.toString());
    }

    public String createSignature(SystempayPaymentRequest paymentRequest) throws Exception {
        Map<String, Field> vadsFields = new TreeMap<>();
        // Retrieves and sorts the names of the vads_ parameters in alphabetical order
        for (Field field : SystempayPaymentRequest.class.getDeclaredFields()) {
            String fieldName = field.getName();
            if (fieldName.startsWith("vads_")) {
                vadsFields.put(fieldName, field);
            }
        }
        // Calculates the signature
        String sep = Sha.SEPARATOR;
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Field> entry : vadsFields.entrySet()) {
            Field field = entry.getValue();
            field.setAccessible(true);
            Object obj = field.get(paymentRequest);
            if (obj == null) {
                continue;
            }
            String vadsParamValue = String.valueOf(obj);
            if (vadsParamValue != null) {
                sb.append(vadsParamValue);
                sb.append(sep);
            }
        }
        sb.append(paySystemSettings.getSystempayCertificate());
        return Sha.encode(sb.toString());
    }
 /*
 vads_amount = 3000
vads_auth_mode = FULL
vads_auth_number = 3fb0de
vads_auth_result = 00
vads_capture_delay = 0
vads_card_brand = VISA
vads_card_number = 497010XXXXXX0000
vads_payment_certificate = a50d15063b5ec6cb140043138b8d7576470b71a9
vads_ctx_mode = TEST
vads_currency = "978" />
vads_effective_amount = 3000
vads_site_id = 12345678
vads_trans_date = 20140902094139
vads_trans_id = 454058
vads_validation_mode = 0
vads_version = V2
vads_warranty_result = YES
vads_payment_src = EC
vads_sequence_number = 1
vads_contract_used = 5785350
vads_trans_status = AUTHORISED
vads_expiry_month = 6
vads_expiry_year = 2015
vads_bank_code = 17807
vads_bank_product = A
vads_pays_ip = FR
vads_presentation_date = 20140902094202
vads_effective_creation_date = 20140902094202
vads_operation_type = DEBIT
vads_threeds_enrolled = Y
vads_threeds_cavv = Q2F2dkNhdnZDYXZ2Q2F2dkNhdnY=
vads_threeds_eci = 05
vads_threeds_xid = WXJsVXpHVjFoMktzNmw5dTd1ekQ=
vads_threeds_cavvAlgorithm = 2
vads_threeds_status = Y
vads_threeds_sign_valid = 1
vads_threeds_error_code =
vads_threeds_exit_status = 10
vads_risk_control = CARD_FRAUD=OK;COMMERCIAL_CARD=OK
vads_result = 00
vads_extra_result = 00
vads_card_country = "FR" />
vads_language = fr
vads_hash = 299d81f4b175bfb7583d904cd19ef5e38b2b79b2373d9b2b4aab74e5753b10bc
vads_url_check_src = PAY
vads_action_mode = INTERACTIVE
vads_payment_config = SINGLE
vads_page_action = PAYMENT
signature = 3132f1e451075f2408cda41f2e647e9b4747d421
  */

    public static final Set<String> SUCCESSFUL_PAYMENT_STATUSES =
            new HashSet<>(Arrays.asList(new String[]{"AUTHORISED", "AUTHORISED_TO_VALIDATE", "CAPTURED"}));

    @SuppressWarnings("CallToStringEquals")
    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof SystempayPaymentRequest) {
            SystempayPaymentRequest data = (SystempayPaymentRequest) target;
            checkEmpty(data, errors);
            if (errors.hasErrors()) {
                return;
            }
            if (!"PAY".equals(data.getVads_url_check_src())) {
                errors.rejectValue("vads_url_check_src", "validation.billing.paysystem.incorrectField");
            }
            if (!"DEBIT".equals(data.getVads_operation_type())) {
                errors.rejectValue("vads_operation_type", "validation.billing.paysystem.incorrectField");
            }
            //todo asl about it
            if (!SUCCESSFUL_PAYMENT_STATUSES.contains(data.getVads_trans_status())) {
                errors.rejectValue("vads_trans_status", "validation.billing.paysystem.incorrectField");
            }
            if (!"SINGLE".equals(data.getVads_payment_config())) {
                errors.rejectValue("vads_payment_config", "validation.billing.paysystem.incorrectField");
            }
            if (!"0".equals(data.getVads_capture_delay())) {
                errors.rejectValue("vads_capture_delay", "validation.billing.paysystem.incorrectField");
            }
            if (!paySystemSettings.getSystempayCurrencyCode().equals(data.getVads_currency())) {
                errors.rejectValue("vads_currency", "validation.billing.paysystem.incorrectField");
            }
            if (!paySystemSettings.getSystempayMode().equals(data.getVads_ctx_mode())) {
                errors.rejectValue("vads_currency", "validation.billing.paysystem.incorrectField");
            }
            if (!paySystemSettings.getSystempaySiteId().equals(data.getVads_site_id())) {
                errors.rejectValue("vads_site_id", "validation.billing.paysystem.incorrectField");
            }
            Invoice invoice = invoiceDao.getByExternalInvoiceNumber(data.getVads_order_id());
            if (invoice == null) {
                errors.rejectValue("vads_order_id", "validation.billing.paysystem.incorrectField");
            } else {
                Comparable<BigDecimal> amount = new BigDecimal(data.getVads_amount())
                        .divide(Globals.HUNDRED, RoundingMode.HALF_UP);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    errors.rejectValue("vads_amount","validation.billing.paysystem.amount.incorrect");
                }
                if (amount.compareTo(invoice.getAmount()) != 0) {
                    errors.rejectValue("vads_amount","validation.billing.paysystem.amount");
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat(
                        SystempayController.SYSTEMPAY_DATE_TIME_FORMAT, Locale.ENGLISH
                );
                dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
                if (!dateFormat.format(invoice.getTransactionDate()).equals(data.getVads_trans_date())) {
                    errors.rejectValue("vads_trans_date","validation.billing.paysystem.date");
                }
                if (!invoice.getUser().getEmail().equals(data.getVads_cust_email())){
                    errors.rejectValue("vads_cust_email","validation.billing.paysystem.incorrectUser");
                }
                if(invoice.getInvoiceStatus() != InvoiceStatus.NOT_PAID){
                    errors.rejectValue("vads_order_id","validation.billing.paysystem.duplicate");
                }
            }
        }
    }

    public static void main(String[] args) {
    }
}