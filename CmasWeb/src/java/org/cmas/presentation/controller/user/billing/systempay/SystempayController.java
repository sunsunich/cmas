package org.cmas.presentation.controller.user.billing.systempay;

import org.cmas.Globals;
import org.cmas.entities.billing.Invoice;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.controller.filter.AccessInterceptor;
import org.cmas.presentation.controller.user.billing.PaySystemSettings;
import org.cmas.presentation.dao.billing.InvoiceDao;
import org.cmas.presentation.model.billing.PaymentAddData;
import org.cmas.presentation.service.billing.BillingService;
import org.cmas.presentation.service.user.DiverService;
import org.cmas.util.StringUtil;
import org.cmas.util.http.HttpLogger;
import org.cmas.util.http.HttpUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DirectFieldBindingResult;
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
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class SystempayController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystempayController.class);

    static final String SYSTEMPAY_DATE_TIME_FORMAT = "yyyyMMddHHmmss";

    @Autowired
    private InvoiceDao invoiceDao;

    @Autowired
    private TransactionSequenceDao transactionSequenceDao;

    @Autowired
    private BillingService billingService;

    @Autowired
    private DiverService diverService;

    @Autowired
    private PaySystemSettings paySystemSettings;

    @Autowired
    private SystempayValidator systempayValidator;

    @RequestMapping("/secure/billing/systempay/accept.html")
    public ModelAndView systempayAccept(
            @RequestParam(AccessInterceptor.INVOICE_ID) String externalInvoiceNumber
    ) {
        return processInvoice(externalInvoiceNumber, "secure/billing/systempay/accept");
    }

    //todo security for fed admins
    @RequestMapping("/fed/billing/systempay/accept.html")
    public ModelAndView fedAdminSystempayAccept(
            @RequestParam(AccessInterceptor.INVOICE_ID) String externalInvoiceNumber
    ) {
        return processInvoice(externalInvoiceNumber, "fed/billing/systempay/accept");
    }

    @NotNull
    private ModelAndView processInvoice(@RequestParam(AccessInterceptor.INVOICE_ID) String externalInvoiceNumber, String viewName) {
        if (systempayValidator.isExternalInvoiceNumberValid(externalInvoiceNumber)) {
            Invoice invoice = invoiceDao.getByExternalInvoiceNumber(externalInvoiceNumber);
            SystempayPaymentRequest paymentRequest = new SystempayPaymentRequest();

            paymentRequest.setVads_ctx_mode(paySystemSettings.getSystempayMode());
            paymentRequest.setVads_currency(paySystemSettings.getSystempayCurrencyCode());
            paymentRequest.setVads_site_id(paySystemSettings.getSystempaySiteId());

            paymentRequest.setVads_order_id(invoice.getExternalInvoiceNumber());

            int transactionNumber = transactionSequenceDao.getCurrentTransactionNumber();
            paymentRequest.setVads_trans_id(StringUtil.addLeadingZerosDecimal(6, transactionNumber));

            BigDecimal localAmount = invoice.getAmount();
            localAmount = localAmount.setScale(2, RoundingMode.DOWN);
            paymentRequest.setVads_amount(
                    String.valueOf(localAmount.multiply(Globals.HUNDRED).intValue())
            );

            Diver diver = invoice.getDiver();
            paymentRequest.setVads_cust_email(diver.getEmail());
// if the field has not been sent or is empty, the payment page will be shown in the language of the buyer's browser
            paymentRequest.setVads_language("");

            SimpleDateFormat dateFormat = new SimpleDateFormat(SYSTEMPAY_DATE_TIME_FORMAT, Locale.ENGLISH);
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date transactionDate = new Date();
            invoice.setTransactionDate(transactionDate);
            invoiceDao.updateModel(invoice);
            paymentRequest.setVads_trans_date(dateFormat.format(transactionDate));

            try {
                paymentRequest.setSignature(systempayValidator.createSignature(paymentRequest));
            } catch (Exception e) {
                LOGGER.error("error while creating signature", e);
                return new ModelAndView("errors/typeMismatch");
            }

            Map<String, Object> model = new HashMap<>();
            model.put("data", paymentRequest);
            return new ModelAndView(viewName, model);
        } else {
            return new ModelAndView("errors/typeMismatch");
        }
    }

    @RequestMapping("/billing/systempay/result.html")
    public void systempayResult(
            HttpServletRequest request
            , HttpServletResponse response
    ) throws IOException {
        SystempayPaymentRequest data = new SystempayPaymentRequest();
        BindingResult errors = new DirectFieldBindingResult(data, "SystempayPaymentRequest");
        HttpLogger.logHttp(request, response);
        try {
            String madeHash = systempayValidator.createSignature(request);
            String signature = request.getParameter("signature");
            if (!signature.equals(madeHash)) {
                errors.reject("validation.billing.paysystem.hash");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            errors.reject("validation.billing.paysystem.hash");
        }
        if (errors.hasErrors()) {
            LOGGER.error(systempayValidator.makeMessageFromErrors(errors));
            return;
        }
        data = SystempayPaymentRequest.fromServletRequest(request);
        //todo run all this in separate thread and create response asap
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
                Diver payerDiver = invoice.getDiver();
                paymentAddData.setUserId(payerDiver.getId());
                BigDecimal amount = new BigDecimal(data.getVads_amount()).divide(Globals.HUNDRED, RoundingMode.DOWN)
                                                                         .setScale(2, RoundingMode.DOWN);
                paymentAddData.setAmount(amount.toString());
                String ip = HttpUtil.getIP(request);
                // add to user's balance
                if (!billingService.paymentAdd(paymentAddData, ip)) {
                    LOGGER.error(
                            "error while adding payment from systempay,"
                            + " possible cuncurrent data modification, invoiceId="
                            + invoice.getId()
                    );
                }
                // pay for feature straight away
                billingService.paymentWithdraw(payerDiver, amount, ip);
                Set<Diver> paidForDivers = invoice.getPaidForDivers();
                boolean isConfirmEmail = false;
                if (paidForDivers == null || paidForDivers.isEmpty()) {
                    isConfirmEmail = true;
                    paidForDivers = new HashSet<>(1);
                    paidForDivers.add(payerDiver);
                }
                for (Diver diver : paidForDivers) {
                    diverService.diverPaidForFeature(diver, invoice, isConfirmEmail);
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

    /*
    vads_amount=300&vads_auth_mode=FULL&vads_auth_number=3fed91&vads_auth_result=00&vads_capture_delay=0
    &vads_card_brand=CB&vads_card_number=497010XXXXXX0000&vads_payment_certificate=1ab2f5b7c60d778b7201e1a8f52715182075d313
    &vads_ctx_mode=TEST&vads_currency=978&vads_effective_amount=300&vads_site_id=35521711
    &vads_trans_date=20160727015345&vads_trans_id=000004&vads_trans_uuid=7a4a508dd2504ad5a4d4cd35f2c7d45f
    &vads_validation_mode=0&vads_version=V2&vads_warranty_result=YES&vads_payment_src=EC
    &vads_order_id=420074843744471&vads_cust_email=3516246432961013032%40mailinator.com&vads_sequence_number=1
    &vads_contract_used=5464867&vads_trans_status=AUTHORISED&vads_expiry_month=6&vads_expiry_year=2017
    &vads_bank_product=F&vads_pays_ip=FI&vads_presentation_date=20160727015150
    &vads_effective_creation_date=20160727015150&vads_operation_type=DEBIT
    &vads_threeds_enrolled=Y&vads_threeds_cavv=Q2F2dkNhdnZDYXZ2Q2F2dkNhdnY%3D&vads_threeds_eci=05
    &vads_threeds_xid=Q0s3bHFyMXlHOHNESXVpM0RPbEI%3D&vads_threeds_cavvAlgorithm=2&vads_threeds_status=Y
    &vads_threeds_sign_valid=1&vads_threeds_error_code=&vads_threeds_exit_status=10&vads_result=00
    &vads_extra_result=&vads_card_country=FR&vads_language=en&vads_action_mode=INTERACTIVE
    &vads_payment_config=SINGLE&vads_page_action=PAYMENT&signature=eda451e553d1f2d414067edbaca472304b53f082


    vads_amount=300&vads_auth_mode=MARK&vads_auth_number=&vads_auth_result=&vads_capture_delay=0
    &vads_card_brand=&vads_card_number=&vads_payment_certificate=&vads_ctx_mode=TEST
    &vads_currency=978&vads_effective_amount=&vads_site_id=35521711&vads_trans_date=20160726201858
    &vads_trans_id=000012&vads_validation_mode=0&vads_version=V2&vads_warranty_result=&vads_payment_src=EC
    &vads_order_id=593452636725564&vads_cust_email=5616353820540402181%40mailinator.com&vads_contract_used=
    &vads_trans_status=ABANDONED&vads_pays_ip=FI&vads_presentation_date=20160726201456
    &vads_threeds_enrolled=&vads_threeds_cavv=&vads_threeds_eci=&vads_threeds_xid=
    &vads_threeds_cavvAlgorithm=&vads_threeds_status=&vads_threeds_sign_valid=
    &vads_threeds_error_code=&vads_threeds_exit_status=&vads_result=17
    &vads_extra_result=&vads_card_country=&vads_language=en&vads_action_mode=INTERACTIVE
    &vads_payment_config=SINGLE&vads_page_action=PAYMENT&signature=ebb9c2839a275417399f36ad41f7386f9d7f22af
     */
    @RequestMapping("/billing/systempay/return.html")
    public ModelAndView systempaySuccess(
            @ModelAttribute SystempayPaymentRequest data
    ) {
        Map<String, Object> model = new HashMap<>();
        Invoice invoice = invoiceDao.getByExternalInvoiceNumber(data.getVads_order_id());
        String vadsAuthResult = data.getVads_auth_result();
        String vadsResult = data.getVads_result();
        if (invoice != null) {
            model.put("invoice", invoice);
            model.put("isSuccess",
                      SystempayValidator.SUCCESSFUL_PAYMENT_STATUSES.contains(data.getVads_trans_status())
                      && "00".equals(vadsAuthResult)
                      && "00".equals(vadsResult)
            );
        }
        return new ModelAndView("billing/systempay/return", model);
    }


}