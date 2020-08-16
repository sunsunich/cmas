package org.cmas.presentation.controller.fedadmin;

import com.google.myjson.GsonBuilder;
import org.cmas.Globals;
import org.cmas.backend.xls.XlsParseException;
import org.cmas.entities.Role;
import org.cmas.entities.cards.CardApprovalRequest;
import org.cmas.entities.cards.CardApprovalRequestStatus;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.presentation.controller.cards.CardDisplayManager;
import org.cmas.presentation.dao.cards.CardApprovalRequestDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.FileUploadBean;
import org.cmas.presentation.model.cards.CardApprovalRequestEditFormObject;
import org.cmas.presentation.model.cards.CardApprovalRequestSearchFormObject;
import org.cmas.presentation.model.user.AddingToFederationFormObject;
import org.cmas.presentation.model.user.PasswordEditFormObject;
import org.cmas.presentation.model.user.UserSearchFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.cards.CardApprovalRequestService;
import org.cmas.presentation.service.cards.PersonalCardService;
import org.cmas.presentation.service.user.DiverService;
import org.cmas.presentation.service.user.UploadDiversTask;
import org.cmas.presentation.service.user.UploadDiversTaskStatus;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.presentation.validator.fedadmin.DiverUploadValidator;
import org.cmas.util.StringUtil;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.http.HttpUtil;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class FederationAdminController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private static final int MAX_PAGE_ITEMS = 30;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private CardApprovalRequestDao cardApprovalRequestDao;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private DiverService diverService;

    @Autowired
    private PersonalCardService personalCardService;

    @Autowired
    private DiverUploadValidator diverUploadValidator;

    @Autowired
    private HibernateSpringValidator validator;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private CardDisplayManager cardDisplayManager;

    @Autowired
    private CardApprovalRequestService cardApprovalRequestService;

    @ModelAttribute("diverTypes")
    public DiverType[] getDiverTypes() {
        return DiverType.values();
    }

    @ModelAttribute("diverLevels")
    public DiverLevel[] getDiverLevels() {
        return DiverLevel.values();
    }

    @RequestMapping("/fed/index.html")
    public ModelAndView indexPage(@ModelAttribute UserSearchFormObject model, @ModelAttribute("xlsFileFormObject") FileUploadBean fileBean) {
        return showIndexPage(model, fileBean);
    }

    private ModelAndView showIndexPage(UserSearchFormObject model, FileUploadBean fileBean) {
        BackendUser<Diver> currentFedAdmin = authenticationService.getCurrentDiver();
        if (currentFedAdmin == null) {
            throw new BadRequestException();
        }
        model.setFederationId(String.valueOf(currentFedAdmin.getUser().getFederation().getId()));
        ModelMap mm = prepareSearchModelMap(model, true);
        mm.addAttribute("xlsFileFormObject", fileBean);
        return new ModelAndView("fed/index", mm);
    }

    private ModelMap prepareSearchModelMap(UserSearchFormObject model, boolean isSetupCards) {
        model.setUserRole(Role.ROLE_DIVER.name());
        model.setLimit(MAX_PAGE_ITEMS);
        ModelMap mm = new ModelMap();
        mm.addAttribute("command", model);
        List<Diver> users = diverDao.searchUsers(model);
        if (isSetupCards) {
            personalCardService.setupDisplayCardsForDivers(users);
        }
        mm.addAttribute("users", users);
        mm.addAttribute("count", diverDao.getMaxCountSearchUsers(model));
        return mm;
    }

    @RequestMapping("/fed/cardApprovalRequests.html")
    public ModelAndView cardApprovalRequests(HttpServletRequest request, @ModelAttribute CardApprovalRequestSearchFormObject model) {
        if (request.getParameterMap().isEmpty()) {
            model.setStatus(CardApprovalRequestStatus.NEW.getName());
        }
        BackendUser<Diver> currentFedAdmin = authenticationService.getCurrentDiver();
        if (currentFedAdmin == null) {
            throw new BadRequestException();
        }
        model.setLimit(MAX_PAGE_ITEMS);
        ModelMap mm = new ModelMap();
        mm.addAttribute("command", model);
        NationalFederation federation = currentFedAdmin.getUser().getFederation();
        List<CardApprovalRequest> requests = cardApprovalRequestDao.searchRequests(model, federation);
        mm.addAttribute("requests", requests);
        mm.addAttribute("requestStatuses", CardApprovalRequestStatus.values());
        mm.addAttribute("count", cardApprovalRequestDao.getMaxCountSearchRequests(model, federation));
        return new ModelAndView("fed/cardApprovalRequests", mm);
    }

    @RequestMapping("/fed/declineCardApprovalRequest.html")
    public ModelAndView declineCardApprovalRequest(@RequestParam("requestId") Long requestId) {
        CardApprovalRequest cardApprovalRequest = cardApprovalRequestDao.getModel(requestId);
        if (cardApprovalRequest == null || cardApprovalRequest.getStatus() == CardApprovalRequestStatus.APPROVED) {
            throw new BadRequestException();
        }
        cardApprovalRequestService.declineCardApprovalRequest(cardApprovalRequest);
        return new ModelAndView("redirect:/fed/cardApprovalRequests.html");
    }

    @RequestMapping("/fed/viewCardApprovalRequest.html")
    public ModelAndView viewCardApprovalRequest(@RequestParam("requestId") Long requestId) {
        CardApprovalRequest cardApprovalRequest = cardApprovalRequestDao.getModel(requestId);
        if (cardApprovalRequest == null) {
            throw new BadRequestException();
        }
        ModelMap mm = new ModelMap();
        mm.addAttribute("cardApprovalRequest", cardApprovalRequest);
        mm.addAttribute("command", new CardApprovalRequestEditFormObject());
        mm.addAttribute("cardGroups", cardDisplayManager.getPersonalCardGroups());
        return new ModelAndView("fed/viewCardApprovalRequest", mm);
    }

    @RequestMapping("/fed/approveCardApprovalRequest.html")
    public View approveCardApprovalRequest(@ModelAttribute CardApprovalRequestEditFormObject formObject, Errors result) {
        BackendUser<Diver> currentFedAdmin = authenticationService.getCurrentDiver();
        if (currentFedAdmin == null) {
            throw new BadRequestException();
        }
        formObject.setFederationId(String.valueOf(currentFedAdmin.getUser().getFederation().getId()));
        cardApprovalRequestService.approveCardApprovalRequest(formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(result);
        }
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/fed/addDiversToFederation.html")
    public ModelAndView addToFederationPage() {
        AddingToFederationFormObject model = new AddingToFederationFormObject();
        ModelMap mm = new ModelMap();
        mm.addAttribute("command", model);
        mm.addAttribute("users", Collections.<Diver>emptyList());
        mm.addAttribute("count", 0);
        mm.addAttribute("showErrors", false);
        return new ModelAndView("fed/addDiversToFederation", mm);
    }

    @RequestMapping("/fed/addDiversToFederationSubmit.html")
    public ModelAndView addToFederationPage(@ModelAttribute("command") AddingToFederationFormObject model, Errors errors) {
        ModelMap mm = new ModelMap();
        mm.addAttribute("command", model);
        validator.validate(model, errors);
        List<Diver> users = errors.hasErrors() ?
                Collections.<Diver>emptyList() :
                diverDao.searchDiversToAddToFederation(model);
        int count = errors.hasErrors() ? 0 : diverDao.getMaxCountDiversToAddToFederation(model);
        mm.addAttribute("users", users);
        mm.addAttribute("count", count);
        mm.addAttribute("showErrors", true);
        return new ModelAndView("fed/addDiversToFederation", mm);
    }

    @RequestMapping("/fed/addToFederation.html")
    public ModelAndView addToFederation(@RequestParam("diverId") Long diverId) {
        BackendUser<Diver> currentFedAdmin = authenticationService.getCurrentDiver();
        if (currentFedAdmin == null) {
            throw new BadRequestException();
        }
        Diver diver = diverDao.getModel(diverId);
        if (diver == null || diver.getFederation() != null) {
            throw new BadRequestException();
        }
        diverService.addGuestDiverToFederation(currentFedAdmin.getUser().getFederation(), diver);
        return new ModelAndView("redirect:/fed/editDiver.html?userId=" + diverId);
    }

    @SuppressWarnings("CallToStringEquals")
    @RequestMapping(value = "/fed/uploadUsers.html", method = RequestMethod.POST)
    public View uploadUsers(@ModelAttribute("xlsFileFormObject") FileUploadBean fileBean, Errors result) {
        BackendUser<Diver> currentDiver = authenticationService.getCurrentDiver();
        if (currentDiver == null) {
            throw new BadRequestException();
        }
        try {
            String errorCode = diverService.scheduleUploadDivers(currentDiver.getUser(), fileBean.getFile());
            if (!StringUtil.isTrimmedEmpty(errorCode)) {
                result.rejectValue("file", errorCode);
                return gsonViewFactory.createGsonView(new JsonBindingResult(result));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.rejectValue("file", "validation.xlsFileFormat");
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        }
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping(value = "/fed/getUploadUsersProgress.html", method = RequestMethod.GET)
    public View getUploadUsersProgress() {
        BackendUser<Diver> currentFedAdmin = authenticationService.getCurrentDiver();
        if (currentFedAdmin == null) {
            throw new BadRequestException();
        }
        UploadDiversTask uploadDiversTask = diverService.getUploadDiversTask(currentFedAdmin.getUser().getId());
        if (uploadDiversTask == null) {
            return gsonViewFactory.createGsonView(new XlsParseProgressJsonBean(UploadDiversTaskStatus.NOT_EXIST));
        } else {
            XlsParseErrorJsonBean xlsParseErrorJsonBean = null;
            String error = null;
            UploadDiversTaskStatus status = uploadDiversTask.getStatus();
            if (status == UploadDiversTaskStatus.ERROR) {
                Exception exception = uploadDiversTask.getException();
                if (exception instanceof XlsParseException) {
                    xlsParseErrorJsonBean = new XlsParseErrorJsonBean(exception.getMessage(),
                                                                      ((XlsParseException) exception).getRowNumber());
                } else {
                    error = "validation.xlsFileFormat";
                }
            }
            return gsonViewFactory.createGsonView(new XlsParseProgressJsonBean(status,
                                                                               uploadDiversTask.getProgress(),
                                                                               error,
                                                                               xlsParseErrorJsonBean));
        }
    }

    @RequestMapping("/fed/getCardPrintName.html")
    public View getCardPrintName(
            @RequestParam("cardJson") String cardJson
    ) {
        PersonalCard card = gsonViewFactory.getCommonGson().fromJson(cardJson, PersonalCard.class);
        return gsonViewFactory.createGsonView(new SimpleGsonResponse(true, card.getPrintName()));
    }

    @RequestMapping("/fed/addDiver.html")
    public ModelAndView addDiver() {
        return getUserInfoPage(new Diver());
    }

    @RequestMapping("/fed/editDiver.html")
    public ModelAndView editDiver(
            @RequestParam("userId") Long userId
    ) {
        Diver diver = diverDao.getModel(userId);
        if (diver == null) {
            throw new BadRequestException();
        }
        return getUserInfoPage(diver);
    }

    @RequestMapping(value = "/fed/deleteDiver.html", method = RequestMethod.GET)
    @Transactional
    public ModelAndView deleteUser(@RequestParam("userId") Long userId) {
        BackendUser<Diver> currentFedAdmin = authenticationService.getCurrentDiver();
        if (currentFedAdmin == null) {
            throw new BadRequestException();
        }
        Diver diver = diverDao.getModel(userId);
        if (diver == null) {
            throw new BadRequestException();
        }
        if (!diver.getFederation().equals(currentFedAdmin.getUser().getFederation())) {
            throw new BadRequestException();
        }
        diver.setEnabled(false);
        diverDao.updateModel(diver);
        return new ModelAndView("redirect:/fed/index.html");
    }

    @RequestMapping("/fed/uploadDiver.html")
    public View uploadDiver(
            @RequestParam("diverJson") String diverJson
    ) {
        BackendUser<Diver> currentFedAdmin = authenticationService.getCurrentDiver();
        if (currentFedAdmin == null) {
            throw new BadRequestException();
        }
        Diver diver = new GsonBuilder().setDateFormat(Globals.DTF).create().fromJson(diverJson, Diver.class);
        diver.setRole(Role.ROLE_DIVER);
        Errors errors = new MapBindingResult(new HashMap(), "diverJson");
        diverUploadValidator.validate(diver, errors);
        if (errors.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(errors));
        }
        diverService.uploadSingleDiver(currentFedAdmin.getUser().getFederation(), diver);
        return gsonViewFactory.createSuccessGsonView();
    }

    @NotNull
    private ModelAndView getUserInfoPage(Diver diver) {
        ModelMap mmap = new ModelMap();
        mmap.addAttribute("command", diver);
        List<PersonalCard> diverCards = diver.getCards();
        PersonalCard natFedCard = diverCards == null ? null : personalCardService.getMaxNationalCard(diver);
        mmap.addAttribute("natFedCard", natFedCard);

        PersonalCard natFedInstructorCard = null;
        if (diver.getInstructor() != null) {
            natFedInstructorCard = personalCardService.getMaxNationalCard(diver.getInstructor());
        }
        mmap.addAttribute("natFedInstructorCard", natFedInstructorCard);

        mmap.addAttribute("cardGroups", cardDisplayManager.getPersonalCardGroups());
        mmap.addAttribute("cardsJson", gsonViewFactory.getCommonGson().toJson(diverCards));

        return new ModelAndView("fed/userInfo", mmap);
    }

    @RequestMapping("/fed/passwdForm.html")
    public ModelAndView loadUserPasswd(Model mm) {
        BackendUser<Diver> currentFedAdmin = authenticationService.getCurrentDiver();
        if (currentFedAdmin == null) {
            throw new BadRequestException();
        }
        PasswordEditFormObject passwd = new PasswordEditFormObject();
        mm.addAttribute("command", passwd);
        return buidPassChangeForm(mm, currentFedAdmin.getUser());
    }

    @RequestMapping(value = "/fed/processEditPassword.html", method = RequestMethod.POST)
    public ModelAndView userEditPasswd(
            HttpServletRequest request
            , @ModelAttribute("command") PasswordEditFormObject formObject
            , BindingResult result
            , Model mm) {
        BackendUser<Diver> currentFedAdmin = authenticationService.getCurrentDiver();
        if (currentFedAdmin == null) {
            throw new BadRequestException();
        }
        Diver user = currentFedAdmin.getUser();
        diverService.changePassword(user, formObject, result, HttpUtil.getIP(request));
        if (result.hasErrors()) {
            return buidPassChangeForm(mm, user);
        } else {
            Map<String, Object> model = new HashMap<String, Object>();
            return new ModelAndView("fed/passwdChangeSuccess", model);
        }
    }

    private static ModelAndView buidPassChangeForm(Model model, Diver fedAdmin) {
        model.addAttribute("fedAdmin", fedAdmin);
        return new ModelAndView("fed/passwdForm");
    }
}
