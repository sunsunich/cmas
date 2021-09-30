package org.cmas.presentation.controller.admin;

import org.cmas.presentation.model.FileUploadBean;
import org.cmas.presentation.service.elearning.ElearningService;
import org.cmas.util.StringUtil;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

@Controller
public class ElearningAdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElearningAdminController.class);

    @Autowired
    private ElearningService elearningService;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @RequestMapping("/admin/elearningTokens.html")
    public ModelAndView elearningTokens(@ModelAttribute("tokenFileFormObject") FileUploadBean fileBean) {
        ModelMap mm = new ModelMap();
        mm.addAttribute("tokenFileFormObject", fileBean);
        return new ModelAndView("admin/elearningTokens", mm);
    }

    @RequestMapping(value = "/admin/uploadElearningTokens.html", method = RequestMethod.POST)
    public View uploadTokens(@ModelAttribute("tokenFileFormObject") FileUploadBean fileBean, Errors result) {
        try {
            String errorCode = elearningService.uploadTokens(fileBean.getFile());
            if (!StringUtil.isTrimmedEmpty(errorCode)) {
                result.rejectValue("file", errorCode);
                return gsonViewFactory.createGsonView(new JsonBindingResult(result));
            }
        } catch (Exception e) {
            LOGGER.error("failed to upload tokens", e);
            result.reject(e.getMessage());
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        }
        return gsonViewFactory.createSuccessGsonView();
    }
}
