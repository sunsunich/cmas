package org.cmas.presentation.controller.user;

import org.cmas.Globals;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.loyalty.LoyaltyProgramItem;
import org.cmas.entities.loyalty.PaidFeature;
import org.cmas.presentation.dao.billing.LoyaltyProgramItemDao;
import org.cmas.presentation.dao.billing.PaidFeatureDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.billing.Invoice;
import org.cmas.presentation.entities.billing.InvoiceType;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.billing.PaymentAddFormObject;
import org.cmas.presentation.service.billing.BillingService;
import org.cmas.presentation.service.loyalty.CameraOrderService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.json.gson.GsonViewFactory;
import org.cmas.util.text.StringUtil;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.util.List;

/**

 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class UserHomeController extends DiverAwareController {

    private final Logger log = LoggerFactory.getLogger(getClass());

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

    @Autowired
    private CameraOrderService cameraOrderService;

    @Autowired
    private LoyaltyProgramItemDao loyaltyProgramItemDao;

    @RequestMapping(value = "/secure/index.html", method = RequestMethod.GET)
    public ModelAndView showIndex() throws IOException {
        Diver diver = getCurrentDiver();
        if (diver.getPreviousRegistrationStatus() == DiverRegistrationStatus.NEVER_REGISTERED) {
            return new ModelAndView("redirect:/secure/firstLogin.html");
        }
        return new ModelAndView("redirect:/secure/profile/getUser.html");
    }

    @RequestMapping(value = "/secure/firstLogin.html", method = RequestMethod.GET)
    public ModelAndView showFirstLogin() throws IOException {
        return new ModelAndView("/secure/welcome");
    }

    @RequestMapping(value = "/secure/chooseNoPayment.html", method = RequestMethod.GET)
    public ModelAndView chooseNoPayment() throws IOException {
        Diver diver = getCurrentDiver();
        diver.setPreviousRegistrationStatus(diver.getDiverRegistrationStatus());
        diverDao.updateModel(diver);
        return new ModelAndView("redirect:/secure/profile/getUser.html");
    }

    @RequestMapping("/secure/pay.html")
    @Transactional
    public ModelAndView pay(@ModelAttribute("command") PaymentAddFormObject fo, Errors errors) {
        ModelMap mm = new ModelMap();
        mm.addAttribute("command", fo);
        BackendUser<? extends User> user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        if (StringUtil.isEmpty(fo.getFeaturesIdsJson()) ||
            StringUtil.isEmpty(fo.getPaymentType())) {
            return createPayForm(mm);
        }
        validator.validate(fo, errors);
        if (errors.hasErrors()) {
            return createPayForm(mm);
        }
        List<Long> featureIds = gsonViewFactory.getCommonGson()
                                               .fromJson(fo.getFeaturesIdsJson(), Globals.LONG_LIST_TYPE);
        List<PaidFeature> features = paidFeatureDao.getByIds(featureIds);
        if (features.isEmpty()) {
            errors.reject("validation.noPaidFeatureSelected");
            return createPayForm(mm);
        }
        Invoice invoice = billingService.createInvoice(features,
                                                       user.getUser(),
                                                       InvoiceType.valueOf(fo.getPaymentType())
        );
        mm.addAttribute("invoiceId", invoice.getExternalInvoiceNumber());
        return new ModelAndView("redirect:/secure/billing/systempay/accept.html", mm);
    }

    @NotNull
    private ModelAndView createPayForm(ModelMap mm) {
        List<PaidFeature> features = paidFeatureDao.getAll();
        mm.addAttribute("features", features);
        mm.addAttribute("featuresJson", gsonViewFactory.getCommonGson().toJson(features));
        return new ModelAndView("/secure/pay", mm);
    }

    @RequestMapping(value = "/secure/loyaltyProgram.html", method = RequestMethod.GET)
    public ModelAndView showLoyaltyProgram(ModelMap mm) throws IOException {
        mm.addAttribute("loyaltyProgramItems", loyaltyProgramItemDao.getAll());
        return new ModelAndView("/secure/loyaltyProgram", mm);
    }

    @RequestMapping(value = "/secure/loyaltyProgramItem.html", method = RequestMethod.GET)
    public ModelAndView showLoyaltyProgramItem(@RequestParam("itemId") Long itemId) throws IOException {
        LoyaltyProgramItem loyaltyProgramItem = loyaltyProgramItemDao.getModel(itemId);
        if (loyaltyProgramItem == null) {
            throw new BadRequestException();
        }
        ModelMap mm = new ModelMap();
        Diver diver = getCurrentDiver();
        DiverRegistrationStatus diverRegistrationStatus = diver.getDiverRegistrationStatus();
        if (diverRegistrationStatus == DiverRegistrationStatus.CMAS_FULL
            || diverRegistrationStatus == DiverRegistrationStatus.GUEST) {
            mm.addAttribute("canOrderThisYear",
                            cameraOrderService.canCreateCameraOrder(diver, loyaltyProgramItem)
            );
        } else {
            mm.addAttribute("canOrderThisYear", false);
        }

        mm.addAttribute("loyaltyProgramItem", loyaltyProgramItem);
        return new ModelAndView("/secure/loyaltyProgramItem", mm);
    }

    @RequestMapping(value = "/secure/createCameraOrder.html", method = RequestMethod.GET)
    public View createCameraOrder(@RequestParam("itemId") Long itemId) throws IOException {
        LoyaltyProgramItem loyaltyProgramItem = loyaltyProgramItemDao.getModel(itemId);
        if (loyaltyProgramItem == null) {
            throw new BadRequestException();
        }
        Diver diver = getCurrentDiver();
        try {
            if (cameraOrderService.createCameraOrder(diver, loyaltyProgramItem)) {
                return gsonViewFactory.createSuccessGsonView();
            } else {
                return gsonViewFactory.createErrorGsonView("error.loyalty.program..camera.order.tooMany");
            }
        } catch (Exception e) {
            log.error("Error while creating camera order", e);
            return gsonViewFactory.createErrorGsonView("error.loyalty.program.camera.orderFailed");
        }
    }
}
