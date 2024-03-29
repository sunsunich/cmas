package org.cmas.presentation.controller.admin;

import com.google.myjson.Gson;
import org.cmas.Globals;
import org.cmas.backend.ImageStorageManager;
import org.cmas.entities.Country;
import org.cmas.entities.Gender;
import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.UserFile;
import org.cmas.entities.UserFileType;
import org.cmas.entities.cards.CardApprovalRequest;
import org.cmas.entities.cards.CardApprovalRequestStatus;
import org.cmas.entities.cards.PersonalCard;
import org.cmas.entities.cards.PersonalCardType;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverRegistrationStatus;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.loyalty.InsuranceRequest;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.UserFileDao;
import org.cmas.presentation.dao.cards.CardApprovalRequestDao;
import org.cmas.presentation.dao.cards.PersonalCardDao;
import org.cmas.presentation.dao.user.AmateurDao;
import org.cmas.presentation.dao.user.UserDao;
import org.cmas.presentation.dao.user.sport.AthleteDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.admin.AdminUserFormObject;
import org.cmas.presentation.model.admin.FederationFormObject;
import org.cmas.presentation.model.admin.PasswordChangeFormObject;
import org.cmas.presentation.model.user.UserFormObject;
import org.cmas.presentation.model.user.UserSearchFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.admin.AdminService;
import org.cmas.presentation.service.cards.PersonalCardService;
import org.cmas.presentation.service.loyalty.InsuranceRequestService;
import org.cmas.presentation.service.sports.NationalFederationService;
import org.cmas.presentation.service.user.DiverMobileService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.presentation.validator.admin.EditUserValidator;
import org.cmas.presentation.validator.admin.PasswdValidator;
import org.cmas.presentation.validator.loyalty.InsuranceRequestValidator;
import org.cmas.util.StringUtil;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.cmas.util.presentation.SpringRole;
import org.hibernate.criterion.Restrictions;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings({"HardcodedFileSeparator", "StringConcatenation"})
@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    protected AthleteDao athleteDao;
    @Autowired
    protected AmateurDao amateurDao;
    @Autowired
    private DiverDao diverDao;
    @Autowired
    private NationalFederationService federationService;
    @Autowired
    private PersonalCardService personalCardService;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    @Qualifier("editUserValidator")
    private EditUserValidator editUserValidator;
    @Autowired
    private PasswdValidator passwdValidator;


    @Autowired
    private HibernateSpringValidator validator;

    //
    public static final int MAX_PAGE_ITEMS = 30;

    @ModelAttribute("roles")
    public Role[] getRoles() {
        return new Role[]{Role.ROLE_DIVER, Role.ROLE_FEDERATION_ADMIN};
    }

    @ModelAttribute("diverTypes")
    public DiverType[] getDiverTypes() {
        return DiverType.values();
    }

    private <T extends User> UserDao<T> getDao(Role role) {
        switch (role) {
            case ROLE_AMATEUR:
                return (UserDao<T>) amateurDao;
            case ROLE_ATHLETE:
                return (UserDao<T>) athleteDao;
            case ROLE_DIVER:
            case ROLE_FEDERATION_ADMIN:
                return (UserDao<T>) diverDao;
            case ROLE_ADMIN:
                break;
        }
        throw new IllegalStateException("unsupported user role");
    }

    @RequestMapping("/admin/index.html")
    public ModelAndView indexPage(@ModelAttribute UserSearchFormObject model, Errors errors) {
        validator.validate(model, errors);
        if (errors.hasErrors()) {
            return getIndexPage(model, Collections.<User>emptyList(), 0);
        }
        model.setLimit(MAX_PAGE_ITEMS);
        String userRole = model.getUserRole();
        UserDao<? extends User> dao;
        if (StringUtil.isTrimmedEmpty(userRole)) {
            dao = diverDao;
            model.setUserRole(Role.ROLE_DIVER.name());
        } else {
            dao = getDao(Role.valueOf(userRole));
        }
        List<? extends User> users = dao.searchUsers(model);
        if (dao instanceof DiverDao) {
            personalCardService.setupDisplayCardsForDivers((List<Diver>) users);
        }
        int count = dao.getMaxCountSearchUsers(model);
        return getIndexPage(model, users, count);
    }

    private ModelAndView getIndexPage(@ModelAttribute UserSearchFormObject model, List<? extends User> users, int count) {
        ModelMap mm = new ModelMap();
        List<Country> countries = countryDao.getAll();
        mm.addAttribute("countries", countries.toArray(new Country[countries.size()]));
        mm.addAttribute("command", model);
        mm.addAttribute("users", users);
        mm.addAttribute("count", count);
        return new ModelAndView("admin/index", mm);
    }

    @RequestMapping("/admin/addFederation.html")
    public ModelAndView addFederation() {
        return getModelAndViewForAddFederation(new FederationFormObject());
    }

    @RequestMapping("/admin/informAllFederations.html")
    public String informAllFederations() {
        federationService.informAllFederations();
        return "redirect:/admin/index.html";
    }

    @NotNull
    protected ModelAndView getModelAndViewForAddFederation(FederationFormObject formObject) {
        ModelMap mm = new ModelMap();
        mm.addAttribute("command", formObject);
        List<Country> countries = countryDao.getAll();
        mm.addAttribute("countries", countries.toArray(new Country[countries.size()]));
        return new ModelAndView("admin/newFederation", mm);
    }

    @RequestMapping("/admin/addFederationSubmit.html")
    public ModelAndView addFederationSubmit(
            @ModelAttribute("command") FederationFormObject formObject, Errors errors
    ) {
        validator.validate(formObject, errors);
        if (errors.hasErrors()) {
            return getModelAndViewForAddFederation(formObject);
        }
        Country country = countryDao.getByCode(formObject.getCountryCode());
        if (country == null) {
            errors.rejectValue("countryCode", "validation.incorrectField");
            return getModelAndViewForAddFederation(formObject);
        }
        Diver federationAdmin = federationService.createNewFederation(formObject);
        return new ModelAndView(
                "redirect:/admin/index.html?userRole=" + Role.ROLE_FEDERATION_ADMIN.name()
                + "&email=" + formObject.getEmail()
                + "&countryCode=" + formObject.getCountryCode()
                //switchToUserAsAdmin(federationAdmin.getId(), Role.ROLE_FEDERATION_ADMIN.getName())
        );
    }


    @RequestMapping("/admin/userInfo.html")
    public ModelAndView showUserInfo(
            @RequestParam("userId") Long userId,
            @RequestParam("userRole") String userRole
    ) {
        User user = getDao(Role.valueOf(userRole)).getModel(userId);
        if (user == null) {
            throw new BadRequestException();
        }
        ModelMap mmap = new ModelMap();
        mmap.addAttribute("user", user);
        return new ModelAndView("admin/userInfo", mmap);
    }

    @RequestMapping("/admin/toUser.html")
    @Transactional
    public String switchToUserAsAdmin(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam("userRole") String userRole
    ) {
        Role role = Role.valueOf(userRole);
        User user = getDao(role).getModel(userId);

        SpringRole springRole = SpringRole.fromRole(role);
        authenticationService.loginAs(new BackendUser(user), new SpringRole[]{springRole});
        return "redirect:" + springRole.getIndexUrl();
    }

    @RequestMapping("/admin/cloneUser.html")
    @Transactional
    public String cloneUser(
            @RequestParam(value = "userId", required = false) Long userId,
            @RequestParam("userRole") String userRole
    ) {
        Role role = Role.valueOf(userRole);
        if (role != Role.ROLE_DIVER) {
            return "redirect:/admin/index.html";
        }
        Diver diver = diverDao.getModel(userId);
        adminService.cloneUser(diver);

        return "redirect:/admin/index.html";
    }

    @RequestMapping(value = "/admin/loadUser.html", method = RequestMethod.GET)
    @Transactional
    public ModelAndView loadUser(@RequestParam("userId") Long userId,
                                 @RequestParam("userRole") String userRole) {
        AdminUserFormObject data = new AdminUserFormObject();
        User user = getDao(Role.valueOf(userRole)).getModel(userId);
        if (user == null) {
            throw new BadRequestException();
        }
        data.transferFromEntity(user);
        return getModelAndViewForEditUser(data);
    }

    @RequestMapping("/admin/updateUser.html")
    public ModelAndView updateUser(@ModelAttribute("command") AdminUserFormObject data, BindingResult result) {
        editUserValidator.validate(data, result);
        if (result.hasErrors()) {
            return getModelAndViewForEditUser(data);
        }
        adminService.editUser(data);
        return new ModelAndView("redirect:/admin/userInfo.html?userId=" + data.getId());
    }

    private ModelAndView getModelAndViewForEditUser(AdminUserFormObject data) {
        ModelMap mm = new ModelMap();
        mm.addAttribute("command", data);
        return new ModelAndView("admin/editUser", mm);
    }

    @RequestMapping("/admin/passwdForm.html")
    public ModelAndView loadPasswordForm(
            @ModelAttribute("command") PasswordChangeFormObject formObject
    ) {
        ModelMap mm = new ModelMap();
        mm.addAttribute("command", formObject);
        return new ModelAndView("admin/passwdForm", mm);
    }

    @RequestMapping("/admin/processPasswdChange.html")
    public ModelAndView processPasswdChange(
            @ModelAttribute("command") PasswordChangeFormObject formObject, BindingResult result) {

        passwdValidator.validate(formObject, result);
        if (result.hasErrors()) {
            return loadPasswordForm(formObject);
        } else {
            adminService.changePassword(formObject);
            return new ModelAndView("redirect:/admin/index.html");
        }
    }

    @Autowired
    private InsuranceRequestValidator insuranceRequestValidator;

    @Autowired
    private InsuranceRequestService insuranceRequestService;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @RequestMapping("/admin/testInsuranceForm.html")
    public ModelAndView testInsuranceForm(
            @RequestParam("diverId") Long diverId
    ) {
        Diver diver = diverDao.getById(diverId);

        ModelMap mm = new ModelMap();
        List<Country> countries = countryDao.getAll();
        mm.addAttribute("countries", countries.toArray(new Country[countries.size()]));
        mm.addAttribute("genders", Gender.values());
        mm.addAttribute("diver", diver);
        return new ModelAndView("admin/insuranceTest", mm);
    }

    @RequestMapping("/admin/testInsurance.html")
    @Transactional
    public View sendInsuranceRequest(
            @RequestParam("insuranceRequestJson") String insuranceRequestJson) {
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

    @RequestMapping(value = "/admin/deleteUser.html", method = RequestMethod.GET)
    @Transactional
    public ModelAndView deleteUser(@RequestParam("userId") Long userId,
                                   @RequestParam("userRole") String userRole) {
        UserDao<User> dao = getDao(Role.valueOf(userRole));
        User user = dao.getModel(userId);
        if (user == null) {
            throw new BadRequestException();
        }
        user.setEnabled(false);
        dao.updateModel(user);
        return new ModelAndView("redirect:/admin/index.html");
    }

    private static UserFormObject setupAddUserForm() {
        UserFormObject data = new UserFormObject();
        return data;
    }

    private static ModelAndView getModelAndViewForNewUser(UserFormObject data) {
        ModelMap mm = new ModelMap();
        mm.addAttribute("command", data);
        return new ModelAndView("admin/newUser", mm);
    }

    @RequestMapping(value = "/admin/newUser.html", method = RequestMethod.GET)
    public ModelAndView newUser() {
        UserFormObject data = setupAddUserForm();
        return getModelAndViewForNewUser(data);
    }

    @Autowired
    private PersonalCardDao personalCardDao;


    @RequestMapping(value = "/admin/regenerateApnoeaCards.html", method = RequestMethod.GET)
    public ModelAndView regenerateApnoeaCards() {
        @SuppressWarnings("unchecked")
        List<PersonalCard> cardsToRegenerate = personalCardDao
                .createCriteria()
                .createAlias("diver", "d")
                .add(Restrictions.ne("d.diverRegistrationStatus", DiverRegistrationStatus.NEVER_REGISTERED))
                .add(Restrictions.eq("cardType", PersonalCardType.APNOEA))
                .list();
        for (PersonalCard card : cardsToRegenerate) {
            personalCardService.generateAndSaveCardImage(card.getId());
        }
        return new ModelAndView("redirect:/admin/index.html");
    }

    @RequestMapping(value = "/admin/regenerateAllCards.html", method = RequestMethod.GET)
    public ModelAndView regenerateAllCards() {
        @SuppressWarnings("unchecked")
        List<PersonalCard> cardsToRegenerate = personalCardDao
                .createCriteria()
                .createAlias("diver", "d")
                .add(Restrictions.isNotNull("d.primaryPersonalCard"))
                .list();
        for (PersonalCard card : cardsToRegenerate) {
            personalCardService.generateAndSaveCardImage(card.getId());
        }
        return new ModelAndView("redirect:/admin/index.html");
    }

    @RequestMapping(value = "/admin/regenerateGuestCards.html", method = RequestMethod.GET)
    public ModelAndView regenerateGuestCards() {
        @SuppressWarnings("unchecked")
        List<PersonalCard> cardsToRegenerate = personalCardDao
                .createCriteria()
                .createAlias("diver", "d")
                .add(Restrictions.or(
                        Restrictions.eq("d.diverRegistrationStatus", DiverRegistrationStatus.GUEST),
                        Restrictions.eq("d.diverRegistrationStatus", DiverRegistrationStatus.DEMO)
                     )
                )
                .list();
        for (PersonalCard card : cardsToRegenerate) {
            personalCardService.generateAndSaveCardImage(card.getId());
        }
        return new ModelAndView("redirect:/admin/index.html");
    }

    @RequestMapping(value = "/admin/regenerateCards.html", method = RequestMethod.GET)
    public ModelAndView regenerateCards(@RequestParam("diverId") Long diverId) {
        @SuppressWarnings("unchecked")
        List<PersonalCard> cardsToRegenerate = personalCardDao
                .createCriteria()
                .add(Restrictions.eq("diver.id", diverId))
                .list();
        for (PersonalCard card : cardsToRegenerate) {
            personalCardService.generateAndSaveCardImage(card.getId());
        }
        return new ModelAndView("redirect:/admin/index.html");
    }

    @Autowired
    private DiverMobileService diverMobileService;

    @RequestMapping(value = "/admin/mobilePreparations.html", method = RequestMethod.GET)
    public ModelAndView mobilePreparations() {
        @SuppressWarnings("unchecked")
        List<Diver> cmasDivers = diverDao.createCriteria().add(
                Restrictions.disjunction()
                            .add(Restrictions.eq("diverRegistrationStatus", DiverRegistrationStatus.CMAS_BASIC))
                            .add(Restrictions.eq("diverRegistrationStatus", DiverRegistrationStatus.CMAS_FULL))
        ).list();
        for (Diver diver : cmasDivers) {
            diverMobileService.setMobileAuthCode(diver);
            diverDao.updateModel(diver);

            @SuppressWarnings("unchecked")
            List<PersonalCard> cardsToRegenerate = personalCardDao
                    .createCriteria()
                    .add(Restrictions.eq("diver", diver))
                    .list();
            if (diver.getPrimaryPersonalCard() == null) {
                personalCardService.generatePrimaryCard(diver, diverDao, true);
            }
            for (PersonalCard card : cardsToRegenerate) {
                personalCardService.generateAndSaveCardImage(card.getId());
            }
        }
        return new ModelAndView("redirect:/admin/index.html");
    }

    @Autowired
    private UserAnnouncesService userAnnouncesService;

    @RequestMapping(value = "/admin/cmasMobileAnnounceTo.html", method = RequestMethod.GET)
    public ModelAndView cmasMobileAnnounceTo(@RequestParam("email") String email) {
        List<Diver> cmasDivers = new ArrayList<>();
        cmasDivers.add(diverDao.getByEmail(email));
        userAnnouncesService.sendMobileReadyAnnounce(cmasDivers);
        return new ModelAndView("redirect:/admin/index.html");
    }

    @RequestMapping(value = "/admin/cmasMobileAnnounce.html", method = RequestMethod.GET)
    public ModelAndView cmasMobileAnnounce() {
        @SuppressWarnings("unchecked")
        List<Diver> cmasDivers = diverDao.createCriteria().add(
                Restrictions.disjunction()
                            .add(Restrictions.eq("diverRegistrationStatus", DiverRegistrationStatus.CMAS_BASIC))
                            .add(Restrictions.eq("diverRegistrationStatus", DiverRegistrationStatus.CMAS_FULL))
        ).list();
        userAnnouncesService.sendMobileReadyAnnounce(cmasDivers);
        return new ModelAndView("redirect:/admin/index.html");
    }

    @RequestMapping(value = "/admin/federationManualsAnnounceTo.html", method = RequestMethod.GET)
    public ModelAndView federationManualsAnnounceTo(@RequestParam("email") String email) {
        Diver federationAdmin = diverDao.getByEmail(email);
        if (federationAdmin.getRole() != Role.ROLE_FEDERATION_ADMIN) {
            throw new BadRequestException();
        }
        List<Diver> cmasDivers = new ArrayList<>();
        cmasDivers.add(federationAdmin);
        userAnnouncesService.sendManualsToFederations(cmasDivers);
        return new ModelAndView("redirect:/admin/index.html");
    }

    @RequestMapping(value = "/admin/federationManualsAnnounce.html", method = RequestMethod.GET)
    public ModelAndView federationManualsAnnounce() {
        @SuppressWarnings("unchecked")
        List<Diver> cmasDivers = diverDao.createCriteria().add(
                Restrictions.eq("role", Role.ROLE_FEDERATION_ADMIN)
        ).list();
        userAnnouncesService.sendManualsToFederations(cmasDivers);
        return new ModelAndView("redirect:/admin/index.html");
    }

    @Autowired
    private CardApprovalRequestDao cardApprovalRequestDao;

    @Autowired
    private ImageStorageManager imageStorageManager;

    @Autowired
    private UserFileDao userFileDao;

    @RequestMapping(value = "/admin/cleanUpCars.html", method = RequestMethod.GET)
    public ModelAndView cleanUpCars() {
        @SuppressWarnings("unchecked")
        List<Diver> bots = diverDao.createCriteria().add(Restrictions.eq("bot", true)).list();
        for (Diver bot : bots) {
            @SuppressWarnings("unchecked")
            List<CardApprovalRequest> requests = cardApprovalRequestDao.createCriteria()
                                                                       .add(Restrictions.eq("diver", bot))
                                                                       .list();
            for (CardApprovalRequest request : requests) {
                UserFile frontImage = request.getFrontImage();
                UserFile backImage = request.getBackImage();
                cardApprovalRequestDao.deleteModel(request);
                if (frontImage != null) {
                    imageStorageManager.deleteUserFile(frontImage);
                    userFileDao.deleteModel(frontImage);
                }
                if (backImage != null) {
                    imageStorageManager.deleteUserFile(backImage);
                    userFileDao.deleteModel(backImage);
                }
            }
            @SuppressWarnings("unchecked")
            List<UserFile> userFiles = userFileDao.createCriteria()
                                                  .add(Restrictions.eq("creator", bot))
                                                  .add(Restrictions.eq("userFileType",
                                                                       UserFileType.CARD_APPROVAL_REQUEST))
                                                  .list();
            for (UserFile userFile : userFiles) {
                imageStorageManager.deleteUserFile(userFile);
                userFileDao.deleteModel(userFile);
            }
        }
        return new ModelAndView("redirect:/admin/index.html");
    }

    @RequestMapping(value = "/admin/cleanUpPrecessedCars.html", method = RequestMethod.GET)
    public ModelAndView cleanUpPrecessedCars() {
        @SuppressWarnings("unchecked")
        List<CardApprovalRequest> requests = cardApprovalRequestDao
                .createCriteria()
                .add(Restrictions.ne("status",
                                     CardApprovalRequestStatus.NEW))
                .add(
                        Restrictions.le("createDate", new Date(System.currentTimeMillis() - 14 * Globals.ONE_DAY_IN_MS))
                )
                .list();
        for (CardApprovalRequest request : requests) {
            UserFile frontImage = request.getFrontImage();
            UserFile backImage = request.getBackImage();
            cardApprovalRequestDao.deleteModel(request);
            if (frontImage != null) {
                imageStorageManager.deleteUserFile(frontImage);
                userFileDao.deleteModel(frontImage);
            }
            if (backImage != null) {
                imageStorageManager.deleteUserFile(backImage);
                userFileDao.deleteModel(backImage);
            }
        }
        return new ModelAndView("redirect:/admin/index.html");
    }
}
