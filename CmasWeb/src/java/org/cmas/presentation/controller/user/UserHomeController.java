package org.cmas.presentation.controller.user;

import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.loyalty.LoyaltyProgramItem;
import org.cmas.presentation.dao.billing.LoyaltyProgramItemDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.service.loyalty.CameraOrderService;
import org.cmas.presentation.service.user.RegistrationService;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.json.gson.GsonViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.Date;

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
        return new ModelAndView("redirect:/secure/profile/getUser.html");
    }

    @RequestMapping(value = "/secure/firstLogin.html", method = RequestMethod.GET)
    public ModelAndView showFirstLogin() {
        Diver diver = getCurrentDiver();
        if (diver.getPrimaryPersonalCard() == null) {
            if (diver.getDateReg() == null) {
                diver.setDateReg(new Date());
                diverDao.updateModel(diver);
            }
            registrationService.generateAllCardsImages(diver);
        }
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
}
