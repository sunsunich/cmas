package org.cmas.presentation.controller.user;

import com.google.myjson.Gson;
import org.cmas.Globals;
import org.cmas.entities.Country;
import org.cmas.entities.Gender;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.loyalty.InsuranceRequest;
import org.cmas.entities.loyalty.LoyaltyProgramItem;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.billing.LoyaltyProgramItemDao;
import org.cmas.presentation.dao.billing.PaidFeatureDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.service.loyalty.CameraOrderService;
import org.cmas.presentation.service.loyalty.InsuranceRequestService;
import org.cmas.presentation.service.user.RegistrationService;
import org.cmas.presentation.validator.loyalty.InsuranceRequestValidator;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 *
 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class UserHomeController extends DiverAwareController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private CameraOrderService cameraOrderService;

    @Autowired
    private LoyaltyProgramItemDao loyaltyProgramItemDao;

    @RequestMapping(value = "/secure/index.html", method = RequestMethod.GET)
    public ModelAndView showIndex() {
        Diver diver = getCurrentDiver();
        if (diver.getPreviousRegistrationStatus() == DiverRegistrationStatus.NEVER_REGISTERED) {
            return new ModelAndView("redirect:/secure/firstLogin.html");
        }
        if (diver.getPreviousRegistrationStatus() == DiverRegistrationStatus.INACTIVE) {
            registrationService.generateAllCardsImages(diver);
        }
        return new ModelAndView("redirect:/secure/profile/getUser.html");
    }

    @RequestMapping(value = "/secure/firstLogin.html", method = RequestMethod.GET)
    public ModelAndView showFirstLogin() {
        Diver diver = getCurrentDiver();
        if (diver.getDateReg() == null) {
            diver.setDateReg(new Date());
            diverDao.updateModel(diver);
        }
        registrationService.generateAllCardsImages(diver);
        return new ModelAndView("/secure/welcome");
    }

    @RequestMapping(value = "/secure/loyaltyProgram.html", method = RequestMethod.GET)
    public ModelAndView showLoyaltyProgram(ModelMap mm) {
        mm.addAttribute("loyaltyProgramItems", loyaltyProgramItemDao.getAll());
        return new ModelAndView("/secure/loyaltyProgram", mm);
    }

    @RequestMapping(value = "/secure/loyaltyProgramItem.html", method = RequestMethod.GET)
    public ModelAndView showLoyaltyProgramItem(@RequestParam("itemId") Long itemId) {
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
    public View createCameraOrder(@RequestParam("itemId") Long itemId) {
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

    @Autowired
    private CountryDao countryDao;
    @Autowired
    private PaidFeatureDao paidFeatureDao;

    @Autowired
    private InsuranceRequestValidator insuranceRequestValidator;

    @Autowired
    private InsuranceRequestService insuranceRequestService;

    @RequestMapping(value = "/secure/insurance.html", method = RequestMethod.GET)
    public ModelAndView showInsurance() {
        ModelMap mm = new ModelMap();
        Diver diver = getCurrentDiver();
        List<Country> countries = countryDao.getAll();
        BigDecimal insurancePrice = paidFeatureDao.getByIds(
                Collections.singletonList(Globals.GOLD_MEMBERSHIP_PAID_FEATURE_DB_ID)).get(0).getPrice();

        mm.addAttribute("insuranceExpiryDate", insuranceRequestService.getDiverInsuranceExpiryDate(diver));
        mm.addAttribute("isGold", diver.isGold());
        mm.addAttribute("countries", countries.toArray(new Country[countries.size()]));
        mm.addAttribute("genders", Gender.values());
        mm.addAttribute("insurancePrice", insurancePrice);
        return new ModelAndView("/secure/insurance", mm);
    }

    @RequestMapping("/secure/createInsuranceRequest.html")
    @Transactional
    public View createInsuranceRequest(
            @RequestParam("insuranceRequestJson") String insuranceRequestJson) {
        Diver diver = getCurrentDiver();
        if (insuranceRequestService.getDiverInsuranceExpiryDate(diver) != null) {
            return gsonViewFactory.createErrorGsonView("validation.insurance.alreadyHave");
        }
        InsuranceRequest formObject = new Gson().fromJson(insuranceRequestJson, InsuranceRequest.class);
        Errors errors = new MapBindingResult(new HashMap(), "insuranceRequestJson");
        insuranceRequestValidator.validate(formObject, errors);
        if (errors.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(errors));
        } else {
            insuranceRequestService.persistAndSendInsuranceRequest(formObject);
            return gsonViewFactory.createSuccessGsonView();
        }
    }

    @RequestMapping(value = "/secure/mobile.html", method = RequestMethod.GET)
    public ModelAndView showMobile(ModelMap mm) {
        return new ModelAndView("/secure/mobile", mm);
    }
}
