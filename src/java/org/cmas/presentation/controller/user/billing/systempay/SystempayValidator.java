package org.cmas.presentation.controller.user.billing.systempay;

import org.cmas.presentation.controller.user.billing.PaySystemValidator;
import org.cmas.presentation.entities.billing.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.TreeSet;

public class SystempayValidator extends PaySystemValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystempayValidator.class);

    @Override
    public boolean supports(Class clazz) {
        return SystempayPaymentRequest.class.equals(clazz);
    }

    private void checkEmpty(SystempayPaymentRequest data, Errors errors) {
        checkEmptyStringField("vads_ctx_mode", data.getVads_ctx_mode(), errors);
        checkEmptyStringField("vads_currency", data.getVads_currency(), errors);
        checkEmptyStringField("vads_cust_email", data.getVads_cust_email(), errors);
        checkEmptyStringField("vads_order_id", data.getVads_order_id(), errors);
        checkEmptyStringField("vads_language", data.getVads_language(), errors);
        checkEmptyStringField("vads_site_id", data.getVads_site_id(), errors);
        checkEmptyStringField("vads_trans_date", data.getVads_trans_date(), errors);
        checkEmptyStringField("vads_trans_id", data.getVads_trans_id(), errors);
        checkEmptyStringField("signature", data.getSignature(), errors);
    }

    public String createSignature(SystempayPaymentRequest paymentRequest) throws Exception {
        Collection<Field> vadsFields = new TreeSet<>();
        // Retrieves and sorts the names of the vads_ parameters in alphabetical order
        for (Field field : SystempayPaymentRequest.class.getDeclaredFields()) {
            if (field.getName().startsWith("vads_")) {
                vadsFields.add(field);
            }
        }
        // Calculates the signature
        String sep = Sha.SEPARATOR;
        StringBuilder sb = new StringBuilder();
        for (Field field : vadsFields) {
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

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof SystempayPaymentRequest) {
            SystempayPaymentRequest data = (SystempayPaymentRequest) target;
            checkEmpty(data, errors);
            if (errors.hasErrors()) {
                return;
            }
            if (!paySystemSettings.getSystempaySiteId().equals(data.getVads_site_id())) {
                errors.rejectValue("vads_site_id", "validation.billing.paysystem.incorrectField");
            }
            Invoice invoice = invoiceDao.getByExternalInvoiceNumber(data.getVads_order_id());
            if (invoice == null) {
                errors.rejectValue("vads_order_id", "validation.billing.paysystem.incorrectField");
            } else {
                Comparable<BigDecimal> amount = new BigDecimal(data.getVads_amount());
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    errors.reject("validation.billing.paysystem.amount");
                }
                try {
                    String madeHash = createSignature(data);
                    String signature = data.getSignature();
                    if (!signature.equals(madeHash)) {
                        errors.reject("validation.billing.paysystem.hash");
                    }
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                    errors.reject("validation.billing.paysystem.hash");
                }
            }
        }
    }

    public static void main(String[] args) {
    }
}