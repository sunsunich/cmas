package org.cmas.presentation.controller.user;

import org.cmas.entities.PersonalCard;
import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.controller.filter.AccessInterceptor;
import org.cmas.presentation.dao.user.PersonalCardDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.ImageDTO;
import org.cmas.presentation.model.user.EmailEditFormObject;
import org.cmas.presentation.model.user.PasswordEditFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.user.PersonalCardService;
import org.cmas.presentation.service.user.UserService;
import org.cmas.util.Base64Coder;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.http.HttpUtil;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**

 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class UserProfileController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    @Qualifier("diverService")
    private UserService<Diver> userService;

    @Autowired
    private PersonalCardService personalCardService;

    @Autowired
    private PersonalCardDao personalCardDao;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @ModelAttribute("user")
    public BackendUser getUser() {
        BackendUser<? extends User> user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        return user;
    }

    @ModelAttribute("diver")
    public Diver getCurrentDiver() {
        BackendUser<? extends User> user = getUser();
        Role role = user.getUser().getRole();
        Diver diver = null;
        if (role == Role.ROLE_DIVER_INSTRUCTOR || role == Role.ROLE_DIVER) {
            diver = (Diver) user.getUser();
        }
        if (diver == null) {
            throw new BadRequestException();
        }
        return diver;
    }

    @RequestMapping("/secure/profile/getUser.html")
    public ModelAndView getUser(Model model) {
        return new ModelAndView("/secure/userInfo");
    }

    @RequestMapping("/secure/profile/getCardImage.html")
    public View getUserCard(@RequestParam(AccessInterceptor.CARD_ID) long cardId) throws IOException {

        PersonalCard personalCard = personalCardDao.getById(cardId);
        byte[] imageBytes = personalCard.getImage();
        if (imageBytes == null || imageBytes.length == 0) {
            personalCard = personalCardService.generateAndSaveCardImage(cardId);
        }
        imageBytes = personalCard.getImage();
        if (imageBytes == null || imageBytes.length == 0) {
            return gsonViewFactory.createErrorGsonView("error.card.not.ready");
        } else {
            return gsonViewFactory.createGsonView(
                    new ImageDTO(true, Base64Coder.encodeString(imageBytes)));
        }
    }

    /*
     * Submit формы редактирования пароля
     */
    @RequestMapping("/secure/processEditPasswd.html")
    public View userEditPasswd(
            HttpServletRequest request
            , @ModelAttribute("command") PasswordEditFormObject formObject
            , BindingResult result
            , Model mm) {
        BackendUser<? extends User> user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        userService.changePassword((Diver) user.getUser(), formObject, result, HttpUtil.getIP(request));
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        } else {
            return gsonViewFactory.createSuccessGsonView();
        }
    }

    @RequestMapping("/secure/processEditEmail.html")
    public View userEditEmail(@ModelAttribute("command") EmailEditFormObject formObject,
                              BindingResult result, Model mm) {
        BackendUser<? extends User> user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        userService.changeEmail((Diver) user.getUser(), formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        } else {
            return gsonViewFactory.createSuccessGsonView();
        }
    }

    //todo implement
    @RequestMapping("/secure/processEditUserpic.html")
    public View userEditUserpic(@ModelAttribute("command") EmailEditFormObject formObject,
                                BindingResult result, Model mm) {
        BackendUser<? extends User> user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        userService.changeEmail((Diver) user.getUser(), formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        } else {
            return gsonViewFactory.createSuccessGsonView();
        }
    }
}