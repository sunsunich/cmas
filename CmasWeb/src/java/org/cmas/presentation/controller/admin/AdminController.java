package org.cmas.presentation.controller.admin;

import com.google.myjson.Gson;
import org.cmas.backend.ImageStorageManager;
import org.cmas.entities.Country;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.diver.DiverType;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.entities.loyalty.InsuranceRequest;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.logbook.LogbookEntryDao;
import org.cmas.presentation.dao.user.AmateurDao;
import org.cmas.presentation.dao.user.PersonalCardDao;
import org.cmas.presentation.dao.user.UserDao;
import org.cmas.presentation.dao.user.sport.AthleteDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.admin.AdminUserFormObject;
import org.cmas.presentation.model.admin.PasswordChangeFormObject;
import org.cmas.presentation.model.user.UserFormObject;
import org.cmas.presentation.model.user.UserSearchFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.admin.AdminService;
import org.cmas.presentation.service.loyalty.InsuranceRequestService;
import org.cmas.presentation.service.user.DiverService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Работа администратора в системе.
 */

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
    private DiverService diverService;

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
            for (User user : users) {
                Diver diver = (Diver) user;
                List<PersonalCard> cardsToShow = diverService.getCardsToShow(diver);
                diver.setCards(cardsToShow);
            }
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

    /*
     * Переключаемся на обычного пользователя
     */
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

    /*
     * Загрузка данных для смены пароля клиента
     */
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
        InsuranceRequest formObject = new InsuranceRequest();
        formObject.setDiver(diver);

        ModelMap mm = new ModelMap();
        mm.addAttribute("insuranceRequest", formObject);
        return new ModelAndView("admin/insuranceTest", mm);
    }

    @RequestMapping("/admin/testInsurance.html")
    @Transactional
    public View sendInsuranceRequest(
            @RequestParam("insuranceRequestJson") String insuranceRequestJson, BindingResult result) {
        InsuranceRequest formObject = new Gson().fromJson(insuranceRequestJson, InsuranceRequest.class);
        insuranceRequestValidator.validate(formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
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
    private ImageStorageManager imageStorageManager;
    @Autowired
    private LogbookEntryDao logbookEntryDao;
    @Autowired
    private PersonalCardDao personalCardDao;

    @RequestMapping(value = "/admin/dbBlobsToFiles.html", method = RequestMethod.GET)
    public ModelAndView dbBlobsToFiles() throws IOException {
        List<Diver> divers = diverDao.createCriteria().add(Restrictions.isNotNull("userpic")).list();
        for (Diver diver : divers) {
            imageStorageManager.storeUserpic(diver, ImageIO.read(new ByteArrayInputStream(diver.getUserpic())));
        }
        List<PersonalCard> personalCards = personalCardDao.createCriteria()
                                                          .add(Restrictions.isNotNull("image")).list();
        for (PersonalCard personalCard : personalCards) {
            imageStorageManager.storeCardImage(personalCard,
                                               ImageIO.read(new ByteArrayInputStream(personalCard.getImage())));
        }
        List<LogbookEntry> LogbookEntries = logbookEntryDao.createCriteria()
                                                           .add(Restrictions.isNotNull("photo"))
                                                           .list();
        for (LogbookEntry logbookEntry : LogbookEntries) {
            imageStorageManager.storeLogbookEntryImage(logbookEntry,
                                                       ImageIO.read(new ByteArrayInputStream(logbookEntry.getPhoto())));
        }
        return new ModelAndView("redirect:/admin/index.html");
    }
}
