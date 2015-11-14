package org.cmas.presentation.controller.user.billing.interkassa;

import org.cmas.presentation.controller.user.billing.PaySystemSettings;
import org.cmas.presentation.dao.billing.InvoiceDao;
import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.user.UserClient;
import org.cmas.presentation.model.billing.PaymentAddData;
import org.cmas.presentation.service.billing.BillingService;
import org.cmas.Globals;
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
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

@Controller
public class InterKassaController {

    private static final Logger logger = LoggerFactory.getLogger(InterKassaController.class);
    private static final String SUCCESS_RESULT_URL = "/billing/interkassa/success.html";
    private static final String ERROR_RESULT_URL = "/billing/interkassa/fail.html";

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private BillingService billingService;

    @Autowired
    private PaySystemSettings paySystemSettings;

    @Autowired
    private InterKassaValidator interKassaValidator;

    @RequestMapping("/secure/billing/interkassa/accept.html")
    public ModelAndView interkassaAccept(
            @RequestParam("invoiceId") String externalInvoiceNumber
    ) {
        if (interKassaValidator.isExternalInvoiceNumberValid(externalInvoiceNumber)) {
            Invoice invoice = invoiceDao.getByExternalInvoiceNumber(externalInvoiceNumber);
            Map<String, Object> model = new HashMap<String, Object>();
            InterKassaPaymentRequest interKassaPaymentRequest = new InterKassaPaymentRequest();

            String shopId = paySystemSettings.getInterKassaShopId();
            interKassaPaymentRequest.setIk_shop_id(shopId);
            interKassaPaymentRequest.setIk_payment_id(externalInvoiceNumber);
            BigDecimal localAmount = invoice.getAmount();
            localAmount = localAmount.setScale(2, BigDecimal.ROUND_DOWN);
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            df.setMinimumFractionDigits(0);
            df.setGroupingUsed(false);
            String amount = df.format(localAmount);

            interKassaPaymentRequest.setIk_payment_amount(amount);

//            String ourHostName = uiSettings.getHostName();
//            interKassaPaymentRequest.setIk_success_url(ourHostName + SUCCESS_RESULT_URL + "?invoiceId=" + externalInvoiceNumber);
//            interKassaPaymentRequest.setIk_success_method(RETURN_METHOD);
//            interKassaPaymentRequest.setIk_fail_url(ourHostName + ERROR_RESULT_URL + "?invoiceId=" + externalInvoiceNumber);
//            interKassaPaymentRequest.setIk_fail_method(RETURN_METHOD);

            model.put("data", interKassaPaymentRequest);
            return new ModelAndView("secure/billing/interkassa/accept", model);


        } else {
            return new ModelAndView("errors/typeMismatch");
        }
    }

    @RequestMapping("/billing/interkassa/result.html")
    public void interkassaResult(
            HttpServletRequest request
            , HttpServletResponse response
            , @ModelAttribute InterKassaPaymentResult data
            , BindingResult errors
    ) throws IOException {
        logger.error("/billing/interkassa/result.html, params:" + data.toString());
        HttpLogger.logHttp(request, response);
        interKassaValidator.validate(data, errors);
        if (errors.hasErrors()) {
            logger.error(interKassaValidator.makeMessageFromErrors(errors));
        } else {
            if ("success".equals(data.getIk_payment_state())) {
                try {
                    Invoice invoice = invoiceDao.getByExternalInvoiceNumber(data.getIk_payment_id());

                    PaymentAddData paymentAddData = new PaymentAddData();
                    paymentAddData.setInvoiceId(invoice.getId());
                    UserClient user = invoice.getUser();
                    paymentAddData.setUserId(user.getNullableId());
                    paymentAddData.setAmount(data.getIk_payment_amount());
                    if (!billingService.paymentAdd(paymentAddData, HttpUtil.getIP(request), true)) {
                        logger.error(
                                "error while adding payment from interkassa,"
                                        + " possible cuncurrent data modification, invoiceId="
                                        + invoice.getId()
                        );
                    }
                }
                catch (Exception e) {
                    logger.error("error while adding payment from interkassa", e);
                }
            } else if ("fail".equals(data.getIk_payment_state())) {
                try {
                    Invoice invoice = invoiceDao.getByExternalInvoiceNumber(data.getIk_payment_id());
                    billingService.paymentError(invoice);
                }
                catch (Exception e) {
                    logger.error("error while adding payment from interkassa", e);
                }
            }
            else {
                try {
                    Invoice invoice = invoiceDao.getByExternalInvoiceNumber(data.getIk_payment_id());
                    logger.error(
                            "payed message was not success, it was '"
                                    + data.getIk_payment_state()
                                    + "' , invoiceId="
                                    + invoice.getId()
                    );
                }
                catch (Exception e) {
                    logger.error("error while adding payment from interkassa", e);
                }
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
    }

    @RequestMapping(SUCCESS_RESULT_URL)
    public ModelAndView interkassaSuccess(
            @RequestParam("ik_payment_id") String externalInvoiceNumber
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        Invoice invoice = invoiceDao.getByExternalInvoiceNumber(externalInvoiceNumber);
        if (invoice != null) {
            model.put("invoice", invoice);
            model.put("date", Globals.getDTF().format(invoice.getCreateDate().getTime()));
            model.put("invoiceType",  "Interkassa");
        }
        return new ModelAndView("billing/interkassa/success", model);
    }

    @RequestMapping(ERROR_RESULT_URL)
    public ModelAndView interkassaFail(
            @RequestParam("ik_payment_id") String externalInvoiceNumber
    ) {
        Map<String, Object> model = new HashMap<String, Object>();
        Invoice invoice = invoiceDao.getByExternalInvoiceNumber(externalInvoiceNumber);
        if (invoice != null) {
            model.put("invoice", invoice);
            model.put("date", Globals.getDTF().format(invoice.getCreateDate().getTime()));
            model.put("invoiceType", "Interkassa");
        }
        return new ModelAndView("billing/interkassa/fail", model);
    }
}