package org.cmas.presentation.controller.user.billing.interkassa;

import org.cmas.presentation.controller.user.billing.PaySystemValidator;
import org.cmas.presentation.entities.billing.Invoice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.Errors;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class InterKassaValidator extends PaySystemValidator {

    private static final Logger logger = LoggerFactory.getLogger(InterKassaValidator.class);

    @Override
    public boolean supports(Class clazz) {
        return InterKassaPaymentResult.class.equals(clazz);
    }

    private void checkEmpty(InterKassaPaymentResult data, Errors errors) {

        checkEmptyStringField("ik_shop_id", data.getIk_shop_id(), errors);
        checkEmptyStringField("ik_payment_amount", data.getIk_payment_amount(), errors);
        checkEmptyStringField("ik_payment_id", data.getIk_payment_id(), errors);
        checkEmptyStringField("ik_paysystem_alias", data.getIk_paysystem_alias(), errors);
        checkEmptyStringField("ik_payment_timestamp", data.getIk_payment_timestamp(), errors);
        checkEmptyStringField("ik_payment_state", data.getIk_payment_state(), errors);
        checkEmptyStringField("ik_trans_id", data.getIk_trans_id(), errors);
        checkEmptyStringField("ik_currency_exch", data.getIk_currency_exch(), errors);
        checkEmptyStringField("ik_sign_hash", data.getIk_sign_hash(), errors);

    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof InterKassaPaymentResult) {
            InterKassaPaymentResult data = (InterKassaPaymentResult) target;
            checkEmpty(data, errors);
            if (errors.hasErrors()) {
                return;
            }
            if (!paySystemSettings.getInterKassaShopId().equals(data.getIk_shop_id())) {
                errors.rejectValue("ik_shop_id", "validation.billing.paysystem.incorrectField");
            }
            Invoice invoice = invoiceDao.getByExternalInvoiceNumber(data.getIk_payment_id());
            if (invoice == null) {
                errors.rejectValue("ik_payment_id", "validation.billing.paysystem.incorrectField");
            } else {

                BigDecimal amount = new BigDecimal(data.getIk_payment_amount());
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    errors.reject("validation.billing.paysystem.amount");
                }
                try {
                    MessageDigest md5 = MessageDigest.getInstance("MD5");
                    md5.reset();
                    /*
                разделителем – «:», в следующем порядке:
Идентификатор магазина (ik_shop_id);
Сумма платежа (ik_payment_amount);
Идентификатор платежа (ik_payment_id);
Способ оплаты (ik_paysystem_alias);
Пользовательское поле (ik_baggage_fields);
Состояние платежа (ik_payment_state);
Внутренний номер платежа в системе «INTERKASSA» (ik_trans_id);
Курс валюты (ik_currency_exch);
Плательщик комиссии (ik_fees_payer);
Секретный ключ (secret_key);
                 */
                    String toDigest =
                                      data.getIk_shop_id() + ':'
                                    + data.getIk_payment_amount() + ':'
                                    + data.getIk_payment_id() + ':'
                                    + data.getIk_paysystem_alias() + ':'
                                    + data.getIk_baggage_fields() + ':'
                                    + data.getIk_payment_state() + ':'
                                    + data.getIk_trans_id() + ':'
                                    + data.getIk_currency_exch() + ':'
                                    + data.getIk_fees_payer() + ':'
                                    + paySystemSettings.getInterKassaKey();

                    md5.update(toDigest.getBytes());
                    String madeHash = new BigInteger(1, md5.digest()).toString(16).toUpperCase();
                    String ik_sign_hash = data.getIk_sign_hash();
                    if (!ik_sign_hash.equals(madeHash)) {                        
                        if (ik_sign_hash.charAt(0) == '0') {
                            String nonZeroHash = ik_sign_hash.substring(1);
                            if (!nonZeroHash.equals(madeHash)) {
                                errors.reject("validation.billing.paysystem.hash");
                            }
                        } else {
                            errors.reject("validation.billing.paysystem.hash");
                        }
                    }

                } catch (NoSuchAlgorithmException e) {
                    logger.error(e.getMessage(), e);
                    errors.reject("validation.billing.paysystem.hash");
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            String s = "89421A5E-09A7-9101-5618-3235141A8061" + ':'
                    + "250" + ':'
                    + "35931069228612810" + ':'
                    + "privat24u" + ':'
                    + "" + ':'
                    + "success" + ':'
                    + "IK_12323190" + ':'
                    + "0.123" + ':'
                    + "0" + ':'
                    + "Rf4EHctIEJaUPujY";
            /*
paySystemSettings.interKassaShopId=89421A5E-09A7-9101-5618-3235141A8061
paySystemSettings.interKassaKey=Rf4EHctIEJaUPujY

             */
            md5.update(s.getBytes());
            byte[] bytes = md5.digest();
            String hash = new BigInteger(1, bytes).toString(16).toUpperCase();
            System.out.println("hash=" + hash);
            System.out.println("ik_sign_hash=0B7849AA923475CD7128654F91A92843");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        /*
InterKassaController - /billing/interkassa/result.html, params:I
nterKassaPaymentResult{ik_payment_timestamp='1364397779',
 ik_payment_state='success',
 ik_trans_id='IK_12323190',                                                                      s
  ik_currency_exch='0.123',
  ik_fees_payer='0',
   ik_sign_hash='0B7849AA923475CD7128654F91A92843'}
    BaseInterKassaData{ik_shop_id='89421A5E-09A7-9101-5618-3235141A8061',
     ik_payment_amount='250', ik_payment_id='35931069228612810',
      ik_payment_desc='
���������� ����� � QUQURAMA', ik_paysystem_alias='privat24u', ik_baggage_fields=''}


2013.03.15 16:26:19,867 [http-217.72.144.69-80-4] ERROR  InterKassaController - /billing/interkassa/result.html, params:
InterKassaPaymentResult{ik_payment_timestamp='1363350319', ik_payment_state='success',
 ik_trans_id='IK_11990299', ik_currency_exch='0.0325595', ik_fees_payer='0',
  ik_sign_hash='F329B33A9583DB108A9BF9026C2ABD7E'}
   BaseInterKassaData{ik_shop_id='4A16A1BD-1A6E-6205-12A1-C01EEE23F235', ik_payment_amount='1.00', ik_payment_id='40718614568292856', ik_payment_
desc='���������� ����� � QUQURAMA', ik_paysystem_alias='qiwir', ik_baggage_fields=''}
2013.03.15 16:26:19,939 [http-217.72.144.69-80-4] ERROR  InterKassaController - Пришедшие данные не прошли валидацию. Не
верная цифровая подпись
        
         */
    }
}