package org.cmas.presentation.controller.user.billing;

import org.cmas.Globals;
import org.cmas.entities.billing.Invoice;
import org.cmas.entities.billing.InvoiceType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.loyalty.PaidFeature;
import org.cmas.presentation.controller.user.DiverAwareController;
import org.cmas.presentation.dao.billing.PaidFeatureDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.model.billing.PaymentAddFormObject;
import org.cmas.presentation.service.billing.BillingService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.util.json.gson.GsonViewFactory;
import org.cmas.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created on Jun 09, 2019
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class PaymentMasterController extends DiverAwareController {

    @Autowired
    private HibernateSpringValidator validator;

    @Autowired
    private BillingService billingService;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private PaidFeatureDao paidFeatureDao;

    @Autowired
    private DiverDao diverDao;

    @RequestMapping("/secure/pay.html")
    @Transactional
    public ModelAndView pay(@ModelAttribute("command") PaymentAddFormObject fo, Errors errors) {
        ModelMap mm = new ModelMap();
        Diver diver = getCurrentDiver();
        if (diver.isGold()) {
            mm.addAttribute("goldExpiryDate", diver.getDateGoldStatusPaymentIsDue());
        }
        mm.addAttribute("command", fo);
        if (StringUtil.isEmpty(fo.getFeaturesIdsJson()) ||
            StringUtil.isEmpty(fo.getPaymentType())) {
            return createPayForm(mm);
        }
        validator.validate(fo, errors);

        List<Long> featureIds = gsonViewFactory.getCommonGson()
                                               .fromJson(fo.getFeaturesIdsJson(), Globals.LONG_LIST_TYPE);
        List<PaidFeature> features = paidFeatureDao.getByIds(featureIds);
        if (features.isEmpty()) {
            errors.reject("validation.noPaidFeatureSelected");
        } else {
            DiverRegistrationStatus diverRegistrationStatus = diver.getDiverRegistrationStatus();
            if (featureIds.contains(Globals.CMAS_LICENCE_PAID_FEATURE_DB_ID)) {
                if (diverRegistrationStatus == DiverRegistrationStatus.GUEST) {
                    errors.reject("validation.guestCmasLicence");
                }
            } else {
                if (diverRegistrationStatus != DiverRegistrationStatus.GUEST
                    && diverRegistrationStatus != DiverRegistrationStatus.CMAS_FULL) {
                    errors.reject("validation.mustIncludeCmasLicence");
                }
            }
            if (featureIds.contains(Globals.GOLD_MEMBERSHIP_PAID_FEATURE_DB_ID)
                && diver.isGold()
            ) {
                //cannot buy gold status twice
                errors.reject("validation.errorGoldRequest");
            }
        }
        if (errors.hasErrors()) {
            return createPayForm(mm);
        }

        Invoice invoice = billingService.createInvoice(
                features,
                diver,
                InvoiceType.valueOf(fo.getPaymentType())
        );
        mm.addAttribute("invoiceId", invoice.getExternalInvoiceNumber());
        return new ModelAndView("redirect:/secure/billing/systempay/accept.html", mm);
    }

    @RequestMapping(value = "/secure/chooseNoPayment.html", method = RequestMethod.GET)
    public ModelAndView chooseNoPayment() {
        Diver diver = getCurrentDiver();
        diver.setPreviousRegistrationStatus(diver.getDiverRegistrationStatus());
        diverDao.updateModel(diver);
        return new ModelAndView("redirect:/secure/profile/getUser.html");
    }

    @NotNull
    private ModelAndView createPayForm(ModelMap mm) {
        List<PaidFeature> features = paidFeatureDao.getAll();
        mm.addAttribute("features", features);
        mm.addAttribute("featuresJson", gsonViewFactory.getCommonGson().toJson(features));
        return new ModelAndView("/secure/pay", mm);
    }
}
