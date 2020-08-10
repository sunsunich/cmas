package org.cmas.presentation.controller.user;

import org.cmas.Globals;
import org.cmas.backend.ImageStorageManager;
import org.cmas.entities.User;
import org.cmas.entities.diver.AreaOfInterest;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.FileUploadBean;
import org.cmas.presentation.model.user.DiverFormObject;
import org.cmas.presentation.model.user.EmailEditFormObject;
import org.cmas.presentation.model.user.PasswordEditFormObject;
import org.cmas.presentation.service.cards.PersonalCardService;
import org.cmas.presentation.service.user.DiverService;
import org.cmas.presentation.validator.UploadImageValidator;
import org.cmas.presentation.validator.user.DiverEditValidator;
import org.cmas.util.ImageUtils;
import org.cmas.util.StringUtil;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.http.HttpUtil;
import org.cmas.util.json.ImageUrlDTO;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;

/**
 *
 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class UserProfileController extends DiverAwareController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DiverService diverService;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private PersonalCardService personalCardService;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private ImageStorageManager imageStorageManager;

    @Autowired
    private DiverEditValidator diverEditValidator;

    @RequestMapping("/secure/getDiver.html")
    public View getDiver(@RequestParam("diverId") long diverId) {
        Diver diver = diverDao.getModel(diverId);
        if (diver == null) {
            throw new BadRequestException();
        }
        personalCardService.setupDisplayCardsForDivers(Collections.singletonList(diver));
        return gsonViewFactory.createDiverView(diver);
    }

    @RequestMapping(value = "/secure/getDivers.html", method = RequestMethod.GET)
    public View getDivers(@RequestParam("diverIdsJson") String diverIdsJson) {
        List<Long> diverIds = null;
        try {
            diverIds = gsonViewFactory.getCommonGson().fromJson(diverIdsJson, Globals.LONG_LIST_TYPE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (diverIds == null || diverIds.isEmpty()) {
            return gsonViewFactory.createDiverView(Collections.<Diver>emptyList());
        }
        return gsonViewFactory.createDiverView(diverDao.getDiversByIds(diverIds));
    }

    @RequestMapping("/secure/profile/getUser.html")
    public ModelAndView getUser(Model model) {
        return new ModelAndView("/secure/userInfo");
    }

    @RequestMapping("/secure/profile/editDiver.html")
    public ModelAndView editUser(Model model) {
        Diver diver = getCurrentDiver();
        if (diver == null) {
            throw new BadRequestException();
        }
        DiverFormObject formObject = new DiverFormObject();
        formObject.transferFromEntity(diver);
        model.addAttribute("command", formObject);
        model.addAttribute("countries", countryDao.getAll());
        model.addAttribute("areas", AreaOfInterest.values());
        return new ModelAndView("/secure/editDiver");
    }

    @RequestMapping("/secure/profile/submitEditDiver.html")
    public View submitEditUser(@ModelAttribute("command") DiverFormObject formObject, BindingResult result) {
        Diver diver = getCurrentDiver();
        if (diver == null) {
            throw new BadRequestException();
        }
        diverEditValidator.validate(formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        }
        diverService.editDiver(formObject, diver);
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/secure/editPassword.html")
    public ModelAndView editPassword(Model model) {
        PasswordEditFormObject formObject = new PasswordEditFormObject();
        model.addAttribute("command", formObject);
        return new ModelAndView("/secure/changePasswordForm");
    }

    @RequestMapping("/secure/processEditPassword.html")
    public View processEditPassword(
            HttpServletRequest request
            , HttpServletResponse response
            , @ModelAttribute("command") PasswordEditFormObject formObject
            , BindingResult result
            , Model mm) {
        BackendUser<? extends User> user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        diverService.changePassword((Diver) user.getUser(), formObject, result, HttpUtil.getIP(request));
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        } else {
            authenticationService.logout(request, response);
            return gsonViewFactory.createSuccessGsonView();
        }
    }

    @RequestMapping("/secure/editEmail.html")
    public ModelAndView editEmail(Model model) {
        EmailEditFormObject formObject = new EmailEditFormObject();
        model.addAttribute("command", formObject);
        return new ModelAndView("/secure/changeEmailForm");
    }

    @RequestMapping("/secure/processEditEmail.html")
    public View processEditEmail(@ModelAttribute("command") EmailEditFormObject formObject,
                                 BindingResult result, Model mm) {
        BackendUser<? extends User> user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        diverService.changeEmail((Diver) user.getUser(), formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        } else {
            return gsonViewFactory.createSuccessGsonView();
        }
    }

    @RequestMapping("/secure/profile/getUserpicUrl.html")
    public View getUserpicUrl() {
        Diver user = getCurrentDiver();
        if (user == null) {
            throw new BadRequestException();
        }
        String userpicUrl = user.getUserpicUrl();
        if (StringUtil.isTrimmedEmpty(userpicUrl)) {
            return gsonViewFactory.createErrorGsonView("error.no.image");
        } else {
            return gsonViewFactory.createGsonView(new ImageUrlDTO(true, userpicUrl));
        }
    }

    @RequestMapping(value = "/secure/uploadFileUserpic.html", method = RequestMethod.POST)
    public View userEditUserpicFile(@ModelAttribute FileUploadBean fileBean) {
        MultipartFile file = fileBean.getFile();
        String errorCode = UploadImageValidator.validateImage(file);
        if (errorCode != null) {
            return gsonViewFactory.createErrorGsonView(errorCode);
        }
        Diver diver = getCurrentDiver();
        if (diver == null) {
            throw new BadRequestException();
        }
        try {
            imageStorageManager.storeUserpic(diver, ImageIO.read(file.getInputStream()));
            return gsonViewFactory.createSuccessGsonView();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return gsonViewFactory.createErrorGsonView("validation.imageFormat");
        }
    }

    @RequestMapping(value = "/secure/processEditUserpic.html", method = RequestMethod.POST)
    public View userEditUserpic(@RequestParam String imageBase64Bytes) {
        Diver diver = getCurrentDiver();
        ImageUtils.ImageConversionResult imageConversionResult = ImageUtils.base64ToImage(imageBase64Bytes);
        if (imageConversionResult.image == null) {
            return gsonViewFactory.createErrorGsonView(imageConversionResult.errorCode);
        }
        try {
            imageStorageManager.storeUserpic(diver, imageConversionResult.image);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return gsonViewFactory.createErrorGsonView("validation.imageFormat");
        }
        return gsonViewFactory.createSuccessGsonView();
    }
}