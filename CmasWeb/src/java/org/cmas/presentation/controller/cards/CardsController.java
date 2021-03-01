package org.cmas.presentation.controller.cards;

import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;
import org.cmas.presentation.controller.user.DiverAwareController;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.cards.CardApprovalRequestDao;
import org.cmas.presentation.dao.user.sport.NationalFederationDao;
import org.cmas.presentation.model.cards.CardApprovalRequestFormObject;
import org.cmas.presentation.service.cards.CardApprovalRequestService;
import org.cmas.presentation.service.cards.PersonalCardService;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.cmas.util.mail.MailerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.util.List;

/**
 * Created on Oct 19, 2019
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class CardsController extends DiverAwareController {

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private CardApprovalRequestDao cardApprovalRequestDao;

    @Autowired
    private NationalFederationDao nationalFederationDao;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private MailerConfig mailerConfig;

    @Autowired
    private CardApprovalRequestService cardApprovalRequestService;

    @Autowired
    private CardDisplayManager cardDisplayManager;

    @Autowired
    private PersonalCardService personalCardService;

    @ModelAttribute("diverTypes")
    public DiverType[] getDiverTypes() {
        return DiverType.values();
    }

    @ModelAttribute("diverLevels")
    public DiverLevel[] getDiverLevels() {
        return DiverLevel.values();
    }

    @RequestMapping("/secure/cards.html")
    public ModelAndView getCards(Model model) {
        Diver diver = getCurrentDiver();
        List<PersonalCard> cards = personalCardService.getCardsToShow(diver);
        model.addAttribute("cards", cards);
        model.addAttribute("pendingCardApprovalRequests", cardApprovalRequestDao.getPendingByDiver(diver));
        return new ModelAndView("/secure/cards");
    }

    @RequestMapping(value = "/secure/addCard.html", method = RequestMethod.GET)
    public ModelAndView showGeneralReport(Model model) {
        CardApprovalRequestFormObject formObject = new CardApprovalRequestFormObject();
        model.addAttribute("command", formObject);
        model.addAttribute("siteEmail", mailerConfig.getSupportAddr());
        model.addAttribute("cardGroups", cardDisplayManager.getPersonalCardGroups());
        model.addAttribute("heliCardGroups", cardDisplayManager.getHeliCardGroups());
        model.addAttribute("federations", nationalFederationDao.getAll());
        return new ModelAndView("secure/addCard");
    }

    @RequestMapping(value = "/secure/submitCardApprovalRequest.html", method = RequestMethod.POST)
    public View submitCardApprovalRequest(
            @ModelAttribute("command") CardApprovalRequestFormObject cardApprovalRequestFormObject, Errors result) {
        Diver currentDiver = getCurrentDiver();
        cardApprovalRequestService.processCardApprovalRequest(result, cardApprovalRequestFormObject, currentDiver);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        }
        return gsonViewFactory.createSuccessGsonView();
    }
}
