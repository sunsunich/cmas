package org.cmas.presentation.controller.user.billing.systempay;

import org.cmas.Globals;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.controller.user.billing.PaySystemSettings;
import org.cmas.presentation.dao.billing.InvoiceDao;
import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.model.billing.PaymentAddData;
import org.cmas.presentation.service.billing.BillingService;
import org.cmas.util.StringUtil;
import org.cmas.util.http.HttpLogger;
import org.cmas.util.http.HttpUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class SystempayController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystempayController.class);
    private static final String SUCCESS_RESULT_URL = "/billing/systempay/success.html";
    private static final String ERROR_RESULT_URL = "/billing/systempay/fail.html";

    private static final String SYSTEMPAY_DATE_TIME_FORMAT = "YYYYMMDDHHMMSS";

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private TransactionSequenceDao transactionSequenceDao;

    @Autowired
    private BillingService billingService;

    @Autowired
    private PaySystemSettings paySystemSettings;

    @Autowired
    private SystempayValidator systempayValidator;

    @RequestMapping("/secure/billing/systempay/accept.html")
    public ModelAndView systempayAccept(
            @RequestParam("invoiceId") String externalInvoiceNumber
    ) {
        if (systempayValidator.isExternalInvoiceNumberValid(externalInvoiceNumber)) {
            Invoice invoice = invoiceDao.getByExternalInvoiceNumber(externalInvoiceNumber);
            Map<String, Object> model = new HashMap<String, Object>();
            SystempayPaymentRequest paymentRequest = new SystempayPaymentRequest();

            paymentRequest.setVads_ctx_mode(paySystemSettings.getSystempayMode());
            paymentRequest.setVads_currency(paySystemSettings.getSystempayCurrencyCode());
            paymentRequest.setVads_site_id(paySystemSettings.getSystempaySiteId());

            paymentRequest.setVads_order_id(invoice.getExternalInvoiceNumber());

            int transactionNumber = transactionSequenceDao.getCurrentTransactionNumber();
            paymentRequest.setVads_trans_id(StringUtil.addLeadingZerosDecimal(6, transactionNumber));

            BigDecimal localAmount = invoice.getAmount();
            localAmount = localAmount.setScale(2, RoundingMode.DOWN);
            paymentRequest.setVads_amount(localAmount.multiply(Globals.HUNDRED).intValue());

            Diver diver = invoice.getDiver();
            paymentRequest.setVads_cust_email(diver.getEmail());
            //todo check for japan
            paymentRequest.setVads_language(diver.getLocale().getLanguage());

            SimpleDateFormat dateFormat = new SimpleDateFormat(SYSTEMPAY_DATE_TIME_FORMAT, Locale.ENGLISH);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            paymentRequest.setVads_trans_date(dateFormat.format(new Date()));

            try {
                paymentRequest.setSignature(systempayValidator.createSignature(paymentRequest));
            } catch (Exception e) {
                LOGGER.error("error while creating signature", e);
                return new ModelAndView("errors/typeMismatch");
            }

            model.put("data", paymentRequest);
            return new ModelAndView("secure/billing/systempay/accept", model);


        } else {
            return new ModelAndView("errors/typeMismatch");
        }
    }

    @RequestMapping("/billing/systempay/result.html")
    public void systempayResult(
            HttpServletRequest request
            , HttpServletResponse response
            , @ModelAttribute SystempayPaymentRequest data
            , BindingResult errors
    ) throws IOException {
        HttpLogger.logHttp(request, response);
        systempayValidator.validate(data, errors);
        if (errors.hasErrors()) {
            LOGGER.error(systempayValidator.makeMessageFromErrors(errors));
            return;
        }
        String vadsAuthResult = data.getVads_auth_result();
        String vadsResult = data.getVads_result();
        String vadsExtraResult = data.getVads_extra_result();
        if ("00".equals(vadsAuthResult) && "00".equals(vadsResult)) {
            try {
                Invoice invoice = invoiceDao.getByExternalInvoiceNumber(data.getVads_order_id());

                PaymentAddData paymentAddData = new PaymentAddData();
                paymentAddData.setInvoiceId(invoice.getId());
                Diver user = invoice.getDiver();
                paymentAddData.setUserId(user.getId());
                BigDecimal amount = new BigDecimal(data.getVads_amount()).divide(Globals.HUNDRED, RoundingMode.DOWN)
                                                                         .setScale(2, RoundingMode.DOWN);
                paymentAddData.setAmount(amount.toString());
                if (!billingService.paymentAdd(paymentAddData, HttpUtil.getIP(request), true)) {
                    LOGGER.error(
                            "error while adding payment from systempay,"
                            + " possible cuncurrent data modification, invoiceId="
                            + invoice.getId()
                    );
                }
            } catch (Exception e) {
                LOGGER.error("error while adding payment from systempay", e);
            }
        } else {
            try {
                Invoice invoice = invoiceDao.getByExternalInvoiceNumber(data.getVads_order_id());
                invoice.setDescription(
                        String.valueOf(vadsAuthResult) + '_'
                        + String.valueOf(vadsResult) + '_'
                        + String.valueOf(vadsExtraResult)
                );
                billingService.paymentError(invoice);
            } catch (Exception e) {
                LOGGER.error("error while adding payment from systempay", e);
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @RequestMapping(SUCCESS_RESULT_URL)
    public ModelAndView systempaySuccess(
            @ModelAttribute SystempayPaymentRequest data
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        Invoice invoice = invoiceDao.getByExternalInvoiceNumber(data.getVads_order_id());
        if (invoice != null) {
            model.put("invoice", invoice);
            model.put("date", Globals.getDTF().format(invoice.getCreateDate().getTime()));
            model.put("invoiceType", "Systempay");
        }
        return new ModelAndView("billing/systempay/success", model);
    }

    @RequestMapping(ERROR_RESULT_URL)
    public ModelAndView systempayFail(
            @ModelAttribute SystempayPaymentRequest data
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        Invoice invoice = invoiceDao.getByExternalInvoiceNumber(data.getVads_order_id());
        if (invoice != null) {
            model.put("invoice", invoice);
            model.put("date", Globals.getDTF().format(invoice.getCreateDate().getTime()));
            model.put("invoiceType", "Systempay");
        }
        return new ModelAndView("billing/systempay/fail", model);
    }
}