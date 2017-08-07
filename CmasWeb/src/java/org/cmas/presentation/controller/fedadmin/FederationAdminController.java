package org.cmas.presentation.controller.fedadmin;

import com.google.myjson.Gson;
import com.google.myjson.GsonBuilder;
import com.google.myjson.JsonElement;
import com.google.myjson.JsonObject;
import com.google.myjson.JsonSerializationContext;
import com.google.myjson.JsonSerializer;
import org.cmas.Globals;
import org.cmas.backend.xls.XlsParseException;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.PersonalCardType;
import org.cmas.entities.Role;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverLevel;
import org.cmas.entities.diver.DiverType;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.presentation.controller.CardDisplayManager;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.FileUploadBean;
import org.cmas.presentation.model.user.UserSearchFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.user.DiverService;
import org.cmas.presentation.service.user.UploadDiversTask;
import org.cmas.presentation.service.user.UploadDiversTaskStatus;
import org.cmas.presentation.validator.fedadmin.DiverUploadValidator;
import org.cmas.util.StringUtil;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class FederationAdminController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private static final int MAX_PAGE_ITEMS = 30;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private DiverService diverService;

    @Autowired
    private DiverUploadValidator diverUploadValidator;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private CardDisplayManager cardDisplayManager;

    @ModelAttribute("diverTypes")
    public DiverType[] getDiverTypes() {
        return DiverType.values();
    }

    @ModelAttribute("diverLevels")
    public DiverLevel[] getDiverLevels() {
        return DiverLevel.values();
    }

    @RequestMapping("/fed/index.html")
    public ModelAndView welcomePage(@ModelAttribute UserSearchFormObject model, @ModelAttribute("xlsFileFormObject") FileUploadBean fileBean) {
        return showIndexPage(model, fileBean);
    }

    private ModelAndView showIndexPage(@ModelAttribute UserSearchFormObject model, @ModelAttribute("xlsFileFormObject") FileUploadBean fileBean) {
        BackendUser<Diver> currentFedAdmin = authenticationService.getCurrentDiver();
        if (currentFedAdmin == null) {
            throw new BadRequestException();
        }
        model.setCountryCode(currentFedAdmin.getUser().getFederation().getCountry().getCode());
        model.setUserRole(Role.ROLE_DIVER.name());
        model.setLimit(MAX_PAGE_ITEMS);
        ModelMap mm = new ModelMap();
        mm.addAttribute("command", model);
        mm.addAttribute("xlsFileFormObject", fileBean);
        List<Diver> users = diverDao.searchUsers(model);
        for (Diver user : users) {
            List<PersonalCard> cardsToShow = diverService.getCardsToShow(user);
            user.setCards(cardsToShow);
        }
        mm.addAttribute("users", users);
        mm.addAttribute("count", diverDao.getMaxCountSearchUsers(model));
        return new ModelAndView("fed/index", mm);
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
        Gson gson = new Gson();
        PersonalCard card = gson.fromJson(cardJson, PersonalCard.class);
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
        diverService.uploadDiver(currentFedAdmin.getUser().getFederation(), diver, true);
        return gsonViewFactory.createSuccessGsonView();
    }

    @NotNull
    private ModelAndView getUserInfoPage(Diver diver) {
        ModelMap mmap = new ModelMap();
        mmap.addAttribute("command", diver);
        List<PersonalCard> diverCards = diver.getCards();
        PersonalCard natFedCard = getMaxNationalCard(diverCards);
        mmap.addAttribute("natFedCard", natFedCard);

        PersonalCard natFedInstructorCard = null;
        if (diver.getInstructor() != null) {
            natFedInstructorCard = getMaxNationalCard(diver.getInstructor().getCards());
        }
        mmap.addAttribute("natFedInstructorCard", natFedInstructorCard);

        mmap.addAttribute("cardGroups", cardDisplayManager.getPersonalCardGroups());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(PersonalCard.class, new JsonSerializer<PersonalCard>() {

            @Override
            public JsonElement serialize(PersonalCard t, Type type, JsonSerializationContext jsonSerializationContext) {
                Gson gson = new GsonBuilder()
                        .excludeFieldsWithoutExposeAnnotation()
                        .setDateFormat(Globals.DTF)
                        .create();
                JsonObject jObj = (JsonObject) gson.toJsonTree(t);
                jObj.addProperty("printName", t.getPrintName());
                return jObj;
            }
        });
        mmap.addAttribute("cardsJson", gsonBuilder.create().toJson(diverCards));

        return new ModelAndView("fed/userInfo", mmap);
    }

    private static PersonalCard getMaxNationalCard(List<PersonalCard> diverCards) {
        PersonalCard foundCard = null;
        if (diverCards != null) {
            for (PersonalCard card : diverCards) {
                if (PersonalCardType.NATIONAL == card.getCardType()) {
                    if (foundCard == null) {
                        foundCard = card;
                    } else {
                        if (card.getDiverLevel() != null) {
                            if (foundCard.getDiverLevel() == null ||
                                card.getDiverLevel().ordinal() > foundCard.getDiverLevel().ordinal()) {
                                foundCard = card;
                            }
                        }
                    }
                }
            }
        }
        return foundCard;
    }
}
