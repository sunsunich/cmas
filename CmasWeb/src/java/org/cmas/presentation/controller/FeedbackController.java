package org.cmas.presentation.controller;

import org.cmas.entities.FeedbackItem;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.presentation.controller.user.DiverAwareController;
import org.cmas.presentation.dao.divespot.DiveSpotDao;
import org.cmas.presentation.dao.logbook.LogbookEntryDao;
import org.cmas.presentation.model.FeedbackFormObject;
import org.cmas.presentation.service.CaptchaService;
import org.cmas.presentation.service.FeedbackService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.presentation.validator.UploadImageValidator;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.mail.MailerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created on Sep 30, 2019
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class FeedbackController extends DiverAwareController {

    @Autowired
    private DiveSpotDao diveSpotDao;

    @Autowired
    private LogbookEntryDao logbookEntryDao;

    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private HibernateSpringValidator validator;

    @Autowired
    private MailerConfig mailerConfig;

    @Autowired
    private FeedbackService feedbackService;

    @RequestMapping(value = "/secure/reportSpot.html", method = RequestMethod.GET)
    public ModelAndView showReportSpot(@RequestParam("spotId") Long spotId, Model model) {
        DiveSpot diveSpot = diveSpotDao.getModel(spotId);
        if (diveSpot == null) {
            throw new BadRequestException();
        }
        FeedbackFormObject formObject = new FeedbackFormObject();
        model.addAttribute("command", formObject);
        model.addAttribute("diveSpot", diveSpot);
        return buildFeedbackForm(model, true);
    }

    @RequestMapping(value = "/secure/reportError.html", method = RequestMethod.GET)
    public ModelAndView showGeneralReport(Model model) {
        FeedbackFormObject formObject = new FeedbackFormObject();
        model.addAttribute("command", formObject);
        return buildFeedbackForm(model, true);
    }

    private ModelAndView buildFeedbackForm(Model model, boolean isCaptchaCorrect) {
        model.addAttribute("captchaError", !isCaptchaCorrect);
        model.addAttribute("reCaptchaPublicKey", captchaService.getReCaptchaPublicKey());
        return new ModelAndView("secure/feedbackForm");
    }

    @RequestMapping(value = "/secure/submitFeedback.html", method = RequestMethod.POST)
    public ModelAndView submitFeedback(
            HttpServletRequest servletRequest, HttpServletResponse servletResponse,
            @ModelAttribute("command") FeedbackFormObject feedbackFormObject,
            Errors result, Model model) {
        Diver currentDiver = getCurrentDiver();
        boolean isCaptchaCorrect = captchaService.validateCaptcha(servletRequest, servletResponse);
        validator.validate(feedbackFormObject, result);
        FeedbackFormObject.FeedbackType feedbackType = null;
        if (!result.hasFieldErrors("feedbackType")) {
            feedbackType = FeedbackFormObject.FeedbackType.valueOf(
                    feedbackFormObject.getFeedbackType());
        }
        DiveSpot diveSpot = null;
        LogbookEntry logbookEntry = null;
        if (feedbackType != null) {
            switch (feedbackType) {
                case GENERAL:
                    break;
                case DIVE_SPOT:
                    if (!result.hasFieldErrors("diveSpotId")) {
                        diveSpot = diveSpotDao.getModel(Long.parseLong(feedbackFormObject.getDiveSpotId()));
                    }
                    model.addAttribute("diveSpot", diveSpot);
                    break;
                case LOGBOOK_ENTRY:
                    if (!result.hasFieldErrors("logbookEntryId")) {
                        logbookEntry = logbookEntryDao.getModel(Long.parseLong(feedbackFormObject.getLogbookEntryId()));
                    }
                    model.addAttribute("logbookEntry", logbookEntry);
                    break;
            }
        }
        if (result.hasErrors() || !isCaptchaCorrect) {
            return buildFeedbackForm(model, isCaptchaCorrect);
        }
        FeedbackItem feedbackItem = new FeedbackItem();
        switch (feedbackType) {
            case GENERAL:
                break;
            case DIVE_SPOT:
                if (diveSpot == null) {
                    result.rejectValue("diveSpotId", "validation.incorrectField");
                } else {
                    feedbackItem.setDiveSpot(diveSpot);
                }
                break;
            case LOGBOOK_ENTRY:
                if (logbookEntry == null) {
                    result.rejectValue("logbookEntryId", "validation.incorrectField");
                } else {
                    feedbackItem.setLogbookEntry(logbookEntry);
                }
                break;
        }
        validateOptionalImage(result, feedbackFormObject.getImage1(), "image1");
        validateOptionalImage(result, feedbackFormObject.getImage2(), "image2");
        if (result.hasErrors()) {
            return buildFeedbackForm(model, true);
        }
        feedbackService.processFeedback(result, feedbackFormObject, currentDiver, feedbackItem);
        if (result.hasErrors()) {
            return buildFeedbackForm(model, true);
        }

        ModelMap mm = new ModelMap();
        mm.addAttribute("siteEmail", mailerConfig.getSupportAddr());
        return new ModelAndView("secure/submitFeedbackSuccess", mm);
    }

    private static void validateOptionalImage(Errors result, MultipartFile multipartFile, String fieldName) {
        String errorCode = UploadImageValidator.validateOptionalImage(multipartFile);
        if (errorCode != null) {
            result.rejectValue(fieldName, errorCode);
        }
    }
}
