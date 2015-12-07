package org.cmas.presentation.controller.admin;

import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.sport.Athlete;
import org.cmas.presentation.dao.user.AmateurDao;
import org.cmas.presentation.dao.user.sport.AthleteDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.admin.AdminUserFormObject;
import org.cmas.presentation.model.admin.PasswordChangeFormObject;
import org.cmas.presentation.model.user.UserFormObject;
import org.cmas.presentation.model.user.UserSearchFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.admin.AdminService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.presentation.validator.admin.EditUserValidator;
import org.cmas.presentation.validator.admin.PasswdValidator;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.presentation.SpringRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Работа администратора в системе.
 *
 */

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
    private EditUserValidator editUserValidator;
    @Autowired
    private PasswdValidator passwdValidator;


    @Autowired
    private HibernateSpringValidator validator;

    //
    public static final int MAX_PAGE_ITEMS = 30;

    @ModelAttribute("roleList")
    public Collection<SpringRole> getRoleList() {
        SpringRole[] roles = {SpringRole.ROLE_AMATEUR, SpringRole.ROLE_ATHLETE, SpringRole.ROLE_ADMIN};
        return Arrays.asList(roles);
    }

    @RequestMapping(value = "/admin/index.html")
    public ModelAndView welcomePage(@ModelAttribute UserSearchFormObject model) {
        model.setLimit(MAX_PAGE_ITEMS);
        ModelMap mm = new ModelMap();
        mm.addAttribute("command", model);
        List<Athlete> users = athleteDao.searchUsers(model);
        mm.addAttribute("users", users);
        mm.addAttribute("count", athleteDao.getMaxCountSearchUsers(model));
        return new ModelAndView("admin/index", mm);
    }


    @RequestMapping(value = "/admin/userInfo.html")
    public ModelAndView showUserInfo(
            @RequestParam("userId") final Long userId,
            @RequestParam("userRole") final String userRole
    ) {
        User user = null;
        Role role = Role.valueOf(userRole);
        switch (role) {
            case ROLE_AMATEUR:
                user = amateurDao.getModel(userId);
                break;
            case ROLE_ATHLETE:
                user = athleteDao.getModel(userId);
                break;
            case ROLE_ADMIN:
                break;
        }
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
    @RequestMapping(value = "/admin/toUser.html")
    @Transactional
    public String switchToUserAsAdmin(@RequestParam(value = "userId", required = false) final Long userId ) {
        IdName res = getUserAndView(userId);
        if (res.user == null) {
            return "";
        }
        authenticationService.loginAs(res.user, new SpringRole[]{SpringRole.ROLE_AMATEUR});
        return "redirect:" + res.redirectTo;
    }

    private IdName getUserAndView(Long userId) {
        IdName res = new IdName();
        if (userId != null) {
            res.user = new BackendUser(athleteDao.getModel(userId));
            res.redirectTo = "/secure/index.html";
        }
        return res;
    }

    private static class IdName { // нет в яве кортежей, какая досада.
        BackendUser user;
        String redirectTo;
    }


    @RequestMapping(value = "/admin/loadUser.html", method = RequestMethod.GET)
    @Transactional
    public ModelAndView loadUser(@RequestParam("userId") final Long userId) {
        AdminUserFormObject data = new AdminUserFormObject();
        Athlete user = athleteDao.getModel(userId);
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
    @RequestMapping(value = "/admin/passwdForm.html")
    public ModelAndView loadPasswordForm(
             @ModelAttribute("command") PasswordChangeFormObject formObject
    ) {
        ModelMap mm = new ModelMap();
        mm.addAttribute("command", formObject);
        return new ModelAndView("admin/passwdForm", mm);
    }

    /*
     * Смена пароля пользователя в админ части
     */
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

    @RequestMapping(value = "/admin/deleteUser.html", method = RequestMethod.GET)
    @Transactional
    public ModelAndView deleteUser(@RequestParam("userId") final Long userId) {
        Athlete user = athleteDao.getModel(userId);
        if (user == null) {
            throw new BadRequestException();
        }
        user.setEnabled(false);
        athleteDao.updateModel(user);

        return new ModelAndView("redirect:/admin/index.html");
    }

    private UserFormObject setupAddUserForm() {
        UserFormObject data = new UserFormObject();
        return data;
    }

    private ModelAndView getModelAndViewForNewUser(UserFormObject data) {
        ModelMap mm = new ModelMap();
        mm.addAttribute("command", data);
        return new ModelAndView("admin/newUser", mm);
    }

    @RequestMapping(value = "/admin/newUser.html", method = RequestMethod.GET)
    public ModelAndView newUser() {
        UserFormObject data = setupAddUserForm();
        return getModelAndViewForNewUser(data);
    }
}
