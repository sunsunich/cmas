package org.cmas.presentation.controller.user;

import org.cmas.entities.Country;
import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.entities.logbook.LogbookVisibility;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.divespot.DiveSpotDao;
import org.cmas.presentation.dao.logbook.LogbookEntryDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.logbook.LogbookEntryFormObject;
import org.cmas.presentation.model.logbook.SearchLogbookEntryFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.mobile.DictionaryDataService;
import org.cmas.presentation.service.user.LogbookService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.util.Base64Coder;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created on Jun 27, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class LogbookController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private LogbookService logbookService;

    @Autowired
    private DictionaryDataService dictionaryDataService;

    @Autowired
    private DiveSpotDao diveSpotDao;

    @Autowired
    private LogbookEntryDao logbookEntryDao;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private HibernateSpringValidator validator;

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
            byte[] userpic = diver.getUserpic();
            if (userpic != null) {
                diver.setPhoto(Base64Coder.encodeString(userpic));
            }
        }
        if (diver == null) {
            throw new BadRequestException();
        }
        return diver;
    }

    @RequestMapping(value = "/secure/createLogbookRecordForm.html", method = RequestMethod.GET)
    public ModelAndView createLogbookRecordForm(
            @RequestParam("spotId") Long spotId
    ) throws IOException {
        DiveSpot diveSpot = diveSpotDao.getById(spotId);
        if (diveSpot == null) {
            throw new BadRequestException();
        }
        return getLogbookRecordForm(diveSpot, null);
    }

    @RequestMapping(value = "/secure/editLogbookRecordForm.html", method = RequestMethod.GET)
    public ModelAndView editLogbookRecordForm(
            @RequestParam("logbookEntryId") Long logbookEntryId
    ) throws IOException {
        DiveSpot diveSpot = null;
        LogbookEntry logbookEntry = null;
        if (logbookEntryId != null) {
            logbookEntry = logbookEntryDao.getById(logbookEntryId);
            if (logbookEntry == null) {
                throw new BadRequestException();
            }
            diveSpot = logbookEntry.getDiveSpot();
        }
        if (diveSpot == null) {
            throw new BadRequestException();
        }
        return getLogbookRecordForm(diveSpot, logbookEntry);
    }

    private ModelAndView getLogbookRecordForm(DiveSpot diveSpot, LogbookEntry logbookEntry) {
        ModelMap mm = new ModelMap();
        mm.addAttribute("spot", diveSpot);
        if (logbookEntry != null) {
            mm.addAttribute("logbookEntry", logbookEntry);
            byte[] photo = logbookEntry.getPhoto();
            if (photo != null) {
                logbookEntry.setPhotoBase64(Base64Coder.encodeString(photo));
            }
        }
        try {
            mm.addAttribute("countries", countryDao.getAll());
            List<NationalFederation> nationalFederations = dictionaryDataService.getNationalFederations(0L);
            Collection<Country> federationCountries = new ArrayList<>(nationalFederations.size());
            for (NationalFederation federation : nationalFederations) {
                federationCountries.add(federation.getCountry());
            }
            mm.addAttribute("federationCountries", federationCountries);
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
        mm.addAttribute("visibilityTypes", LogbookVisibility.values());
        return new ModelAndView("/secure/logbookRecordForm", mm);
    }

    @RequestMapping(value = "/secure/createRecord.html", method = RequestMethod.POST)
    public View createRecord(@ModelAttribute("command") LogbookEntryFormObject formObject, Errors errors) throws IOException {
        validator.validate(formObject, errors);
        if (errors.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(errors));
        }
        try {
            Diver diver = getCurrentDiver();
            logbookService.createOrUpdateRecord(diver, formObject);
            return gsonViewFactory.createSuccessGsonView();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return gsonViewFactory.createErrorGsonView("validation.internal");
        }
    }

    @RequestMapping(value = "/secure/getRecord.html", method = RequestMethod.GET)
    public View getRecord(@RequestParam("logbookEntryId") Long logbookEntryId
    ) {
        LogbookEntry logbookEntry = logbookEntryDao.getModel(logbookEntryId);
        if (logbookEntry == null) {
            throw new BadRequestException();
        }
        byte[] photo = logbookEntry.getPhoto();
        if (photo != null) {
            logbookEntry.setPhotoBase64(Base64Coder.encodeString(photo));
        }
        return gsonViewFactory.createGsonFeedView(Arrays.asList(logbookEntry));
    }

    @RequestMapping(value = "/secure/deleteRecord.html", method = RequestMethod.GET)
    public View deleteRecord(
            @RequestParam("logbookEntryId") Long logbookEntryId
    ) {
        LogbookEntry logbookEntry = logbookEntryDao.getById(logbookEntryId);
        if (logbookEntry == null) {
            throw new BadRequestException();
        }
        logbookEntry.setDeleted(true);
        logbookEntryDao.updateModel(logbookEntry);
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping(value = "/secure/showLogbook.html", method = RequestMethod.GET)
    public ModelAndView showLogbookPage() throws IOException {
        ModelMap mm = new ModelMap();
        return new ModelAndView("/secure/logbook", mm);
    }

    @RequestMapping(value = "/secure/getMyLogbookFeed.html", method = RequestMethod.GET)
    public View getMyLogbookFeed(
            @ModelAttribute("command") SearchLogbookEntryFormObject formObject, Errors errors
    ) throws IOException {
        Diver diver = getCurrentDiver();
        return gsonViewFactory.createGsonFeedView(
                setPhotos(logbookEntryDao.getDiverLogbookFeed(diver, formObject))
        );
    }

    @RequestMapping(value = "/secure/getMyPublicLogbookFeed.html", method = RequestMethod.GET)
    public View getMyPublicLogbookFeed(
            @ModelAttribute("command") SearchLogbookEntryFormObject formObject, Errors errors
    ) throws IOException {
        Diver diver = getCurrentDiver();
        Set<Country> countries = diver.getNewsFromCountries();
        if (diver.isNewsFromCurrentLocation()) {
            Locale locale = LocaleContextHolder.getLocale();
            //todo fix codes
            Country currentCountry = countryDao.getByName(locale.getDisplayCountry());
            if (currentCountry != null) {
                countries.add(currentCountry);
            }
        }
        if (countries.isEmpty()) {
            countries = null;
        }
        return gsonViewFactory.createGsonFeedView(
                setPhotos(logbookEntryDao.getDiverPublicLogbookFeed(diver, countries, formObject))
        );
    }

    @RequestMapping(value = "/getPublicLogbookFeed.html", method = RequestMethod.GET)
    public View getPublicLogbookFeed(
            @ModelAttribute("command") SearchLogbookEntryFormObject formObject, Errors errors
    ) throws IOException {
        //todo filters ?
        List<Country> countries = null;
        return gsonViewFactory.createGsonFeedView(
                setPhotos(logbookEntryDao.getPublicLogbookFeed(countries, formObject))
        );
    }

    @RequestMapping(value = "/secure/getMyFriendsLogbookFeed.html", method = RequestMethod.GET)
    public View getMyFriendsLogbookFeed(
            @ModelAttribute("command") SearchLogbookEntryFormObject formObject, Errors errors
    ) throws IOException {
        Diver diver = getCurrentDiver();
        return gsonViewFactory.createGsonFeedView(
                setPhotos(logbookEntryDao.getDiverFriendsLogbookFeed(diver, formObject))
        );
    }

    private static List<LogbookEntry> setPhotos(List<LogbookEntry> logbookEntries) {
        for (LogbookEntry logbookEntry : logbookEntries) {
            Diver diver = logbookEntry.getDiver();
            byte[] userpic = diver.getUserpic();
            if (userpic != null) {
                diver.setPhoto(Base64Coder.encodeString(userpic));
            }
            byte[] photo = logbookEntry.getPhoto();
            if (photo != null) {
                logbookEntry.setPhotoBase64(Base64Coder.encodeString(photo));
            }
        }
        return logbookEntries;
    }
}
