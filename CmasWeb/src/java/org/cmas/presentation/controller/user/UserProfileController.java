package org.cmas.presentation.controller.user;

import com.google.myjson.Gson;
import org.cmas.Globals;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.LogbookVisibility;
import org.cmas.presentation.controller.filter.AccessInterceptor;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.user.PersonalCardDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.FileUploadBean;
import org.cmas.presentation.model.ImageDTO;
import org.cmas.presentation.model.user.EmailEditFormObject;
import org.cmas.presentation.model.user.PasswordEditFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.user.DiverService;
import org.cmas.presentation.service.user.PersonalCardService;
import org.cmas.util.Base64Coder;
import org.cmas.util.StringUtil;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.http.HttpUtil;
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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**

 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class UserProfileController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private DiverService diverService;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private PersonalCardService personalCardService;

    @Autowired
    private PersonalCardDao personalCardDao;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private CountryDao countryDao;

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
        if (role == Role.ROLE_DIVER) {
            diver = (Diver) user.getUser();
        }
        if (diver == null) {
            throw new BadRequestException();
        }
        return diver;
    }

    @RequestMapping("/secure/getDiver.html")
    public View getDiver(@RequestParam("diverId") long diverId) throws IOException {
        Diver diver = diverDao.getModel(diverId);
        if (diver == null) {
            throw new BadRequestException();
        }
        byte[] userpic = diver.getUserpic();
        if (userpic != null) {
            diver.setPhoto(Base64Coder.encodeString(userpic));
        }
        return gsonViewFactory.createGsonView(diver);
    }

    @RequestMapping(value = "/secure/getDivers.html", method = RequestMethod.GET)
    public View showSpots(@RequestParam("diverIdsJson") String diverIdsJson) {
        List<Long> diverIds = null;
        try {
            diverIds = new Gson().fromJson(diverIdsJson, Globals.LONG_LIST_TYPE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (diverIds == null || diverIds.isEmpty()) {
            return gsonViewFactory.createGsonView(Collections.emptyList());
        }
        return gsonViewFactory.createGsonView(diverDao.getDiversByIds(diverIds));
    }

    @RequestMapping("/secure/profile/getUser.html")
    public ModelAndView getUser(Model model) {
        try {
            model.addAttribute("countries", countryDao.getAll());
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
        model.addAttribute("visibilityTypes", LogbookVisibility.values());
        return new ModelAndView("/secure/userInfo");
    }

    @RequestMapping("/secure/cards.html")
    public ModelAndView getCards(Model model) {
        Diver diver = getCurrentDiver();
        List<PersonalCard> cards = diverService.getCardsToShow(diver);
        model.addAttribute("cards", cards);
        return new ModelAndView("/secure/cards");
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

    @RequestMapping("/secure/profile/getUserpic.html")
    public View getUserpic() throws IOException {
        Diver user = getCurrentDiver();
        if (user == null) {
            throw new BadRequestException();
        }
        byte[] imageBytes = user.getUserpic();
        if (imageBytes == null || imageBytes.length == 0) {
            return gsonViewFactory.createErrorGsonView("error.no.image");
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
        diverService.changePassword((Diver) user.getUser(), formObject, result, HttpUtil.getIP(request));
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
        diverService.changeEmail((Diver) user.getUser(), formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        } else {
            return gsonViewFactory.createSuccessGsonView();
        }
    }

    private static final long MAX_IMAGE_SIZE = 250L * 1024L * 1024L;

    @RequestMapping(value = "/secure/uploadFileUserpic.html", method = RequestMethod.POST)
    public View userEditUserpicFile(@ModelAttribute FileUploadBean fileBean) {
        MultipartFile file = fileBean.getFile();
        if (file == null) {
            return gsonViewFactory.createErrorGsonView("validation.emptyField");
        }
        if (!file.getContentType().startsWith("image")) {
            return gsonViewFactory.createErrorGsonView("validation.imageFormat");
        }
        if (file.getSize() > MAX_IMAGE_SIZE) {
            return gsonViewFactory.createErrorGsonView("validation.imageSize");
        }
        Diver user = getCurrentDiver();
        if (user == null) {
            throw new BadRequestException();
        }
        try {
            user.setUserpic(file.getBytes());
            diverDao.updateModel(user);
            return gsonViewFactory.createSuccessGsonView();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return gsonViewFactory.createErrorGsonView("validation.imageFormat");
        }
    }

    @RequestMapping(value = "/secure/processEditUserpic.html", method = RequestMethod.POST)
    public View userEditUserpic(@RequestParam String imageBase64Bytes) {
        if (StringUtil.isTrimmedEmpty(imageBase64Bytes)) {
            return gsonViewFactory.createErrorGsonView("validation.emptyField");
        }
        byte[] imageBytes;
        try {
            imageBytes = Base64Coder.decode(imageBase64Bytes);
            if ((long) imageBytes.length > MAX_IMAGE_SIZE) {
                return gsonViewFactory.createErrorGsonView("validation.imageSize");
            }
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (image == null) {
                return gsonViewFactory.createErrorGsonView("validation.imageFormat");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return gsonViewFactory.createErrorGsonView("validation.imageFormat");
        }
        Diver user = getCurrentDiver();
        if (user == null) {
            throw new BadRequestException();
        }

        user.setUserpic(imageBytes);
        diverDao.updateModel(user);
        return gsonViewFactory.createSuccessGsonView();
    }
}