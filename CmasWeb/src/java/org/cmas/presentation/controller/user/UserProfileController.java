package org.cmas.presentation.controller.user;

import org.cmas.Globals;
import org.cmas.backend.ImageStorageManager;
import org.cmas.entities.PersonalCard;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.controller.filter.AccessInterceptor;
import org.cmas.presentation.dao.user.PersonalCardDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.FileUploadBean;
import org.cmas.presentation.model.user.EmailEditFormObject;
import org.cmas.presentation.model.user.PasswordEditFormObject;
import org.cmas.presentation.service.user.DiverService;
import org.cmas.presentation.service.user.PersonalCardService;
import org.cmas.presentation.service.user.RegistrationService;
import org.cmas.util.Base64Coder;
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
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**

 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class UserProfileController extends DiverAwareController{

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DiverService diverService;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private PersonalCardService personalCardService;

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private PersonalCardDao personalCardDao;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private ImageStorageManager imageStorageManager;

    @RequestMapping("/secure/getDiver.html")
    public View getDiver(@RequestParam("diverId") long diverId) throws IOException {
        Diver diver = diverDao.getModel(diverId);
        if (diver == null) {
            throw new BadRequestException();
        }
        return gsonViewFactory.createGsonView(diver);
    }

    @RequestMapping(value = "/secure/getDivers.html", method = RequestMethod.GET)
    public View showSpots(@RequestParam("diverIdsJson") String diverIdsJson) {
        List<Long> diverIds = null;
        try {
            diverIds = gsonViewFactory.getCommonGson().fromJson(diverIdsJson, Globals.LONG_LIST_TYPE);
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
        Diver diver = getCurrentDiver();
        if (diver.getPrimaryPersonalCard() == null) {
            if (diver.getDateReg() == null) {
                diver.setDateReg(new Date());
                diverDao.updateModel(diver);
            }
            registrationService.createDiverPrimaryCard(diver);
        }
        return new ModelAndView("/secure/userInfo");
    }

    @RequestMapping("/secure/cards.html")
    public ModelAndView getCards(Model model) {
        Diver diver = getCurrentDiver();
        List<PersonalCard> cards = diverService.getCardsToShow(diver);
        model.addAttribute("cards", cards);
        return new ModelAndView("/secure/cards");
    }

    @RequestMapping("/secure/profile/getCardImageUrl.html")
    public View getCardImageUrl(@RequestParam(AccessInterceptor.CARD_ID) long cardId) throws IOException {
        PersonalCard personalCard = personalCardDao.getById(cardId);
        if (StringUtil.isTrimmedEmpty(personalCard.getImageUrl())) {
            personalCard = personalCardService.generateAndSaveCardImage(cardId);
        }
        String imageUrl = personalCard.getImageUrl();
        if (StringUtil.isTrimmedEmpty(imageUrl)) {
            return gsonViewFactory.createErrorGsonView("error.card.not.ready");
        } else {
            return gsonViewFactory.createGsonView(
                    new ImageUrlDTO(true, imageUrl));
        }
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
    public View getUserpicUrl() throws IOException {
        Diver user = getCurrentDiver();
        if (user == null) {
            throw new BadRequestException();
        }
        String userpicUrl = user.getUserpicUrl();
        if (StringUtil.isTrimmedEmpty(userpicUrl)) {
            return gsonViewFactory.createErrorGsonView("error.no.image");
        } else {
            return gsonViewFactory.createGsonView(
                    new ImageUrlDTO(true, userpicUrl));
        }
    }

    private static final long MAX_IMAGE_SIZE = 10L * 1024L * 1024L * 1024L;

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
        if (StringUtil.isTrimmedEmpty(imageBase64Bytes)) {
            return gsonViewFactory.createErrorGsonView("validation.emptyField");
        }
        Diver diver = getCurrentDiver();
        try {
            byte[] imageBytes = Base64Coder.decode(imageBase64Bytes);
            if ((long) imageBytes.length > MAX_IMAGE_SIZE) {
                return gsonViewFactory.createErrorGsonView("validation.imageSize");
            }
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (image == null) {
                return gsonViewFactory.createErrorGsonView("validation.imageFormat");
            }
            imageStorageManager.storeUserpic(diver, image);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return gsonViewFactory.createErrorGsonView("validation.imageFormat");
        }
        return gsonViewFactory.createSuccessGsonView();
    }
}