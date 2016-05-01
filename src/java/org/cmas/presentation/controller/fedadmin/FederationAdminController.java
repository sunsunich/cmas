package org.cmas.presentation.controller.fedadmin;

import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.FileUploadBean;
import org.cmas.presentation.model.user.UserSearchFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.user.DiverService;
import org.cmas.util.http.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class FederationAdminController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    public static final int MAX_PAGE_ITEMS = 30;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private DiverService diverService;

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
        mm.addAttribute("users", users);
        mm.addAttribute("count", diverDao.getMaxCountSearchUsers(model));
        return new ModelAndView("fed/index", mm);
    }

    @SuppressWarnings("CallToStringEquals")
    @RequestMapping(value = "/fed/uploadUsers.html", method = RequestMethod.POST)
    public ModelAndView uploadUsers(@ModelAttribute("xlsFileFormObject") FileUploadBean fileBean, Errors result) {
        BackendUser<Diver> currentFedAdmin = authenticationService.getCurrentDiver();
        if (currentFedAdmin == null) {
            throw new BadRequestException();
        }

        MultipartFile file = fileBean.getFile();
        UserSearchFormObject command = new UserSearchFormObject();
        if (file == null) {
            result.rejectValue("file", "validation.emptyField");
            return showIndexPage(command, fileBean);
        }
        String contentType = file.getContentType();
        if (!"application/vnd.ms-excel".equals(contentType)
            && !"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet".equals(contentType)) {
            result.rejectValue("file", "validation.xlsFileFormat");
            return showIndexPage(command, fileBean);
        }
        try {
            diverService.uploadDivers(currentFedAdmin.getUser().getFederation(), file.getInputStream());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.rejectValue("file", "validation.xlsFileFormat");
            return showIndexPage(command, fileBean);
        }
        return showIndexPage(command, fileBean);
    }


    @RequestMapping("/fed/userInfo.html")
    public ModelAndView showUserInfo(
            @RequestParam("userId") Long userId
    ) {
        User user = diverDao.getModel(userId);
        if (user == null) {
            throw new BadRequestException();
        }
        ModelMap mmap = new ModelMap();
        mmap.addAttribute("user", user);
        return new ModelAndView("federation/userInfo", mmap);
    }
}
