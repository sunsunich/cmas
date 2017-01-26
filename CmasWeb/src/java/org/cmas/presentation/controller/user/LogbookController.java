package org.cmas.presentation.controller.user;

import org.cmas.entities.Country;
import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.entities.logbook.CurrentType;
import org.cmas.entities.logbook.DivePurposeType;
import org.cmas.entities.logbook.DiveSuitType;
import org.cmas.entities.logbook.EntryType;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.entities.logbook.LogbookVisibility;
import org.cmas.entities.logbook.PressureMeasureUnit;
import org.cmas.entities.logbook.SurfaceType;
import org.cmas.entities.logbook.TankSupplyType;
import org.cmas.entities.logbook.TemperatureMeasureUnit;
import org.cmas.entities.logbook.UnderWaterVisibilityType;
import org.cmas.entities.logbook.VolumeMeasureUnit;
import org.cmas.entities.logbook.WaterType;
import org.cmas.entities.logbook.WeatherType;
import org.cmas.entities.sport.NationalFederation;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.divespot.DiveSpotDao;
import org.cmas.presentation.dao.logbook.LogbookEntryDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.logbook.SearchLogbookEntryFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.mobile.DictionaryDataService;
import org.cmas.presentation.service.user.LogbookService;
import org.cmas.presentation.validator.user.LogbookEntryValidator;
import org.cmas.remote.json.SuccessIdObject;
import org.cmas.util.Base64Coder;
import org.cmas.util.dao.HibernateDaoImpl;
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
import org.springframework.validation.MapBindingResult;
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
import java.util.HashMap;
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
    private LogbookEntryValidator logbookEntryValidator;

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
            @RequestParam(value = "spotId", required = false) Long spotId
    ) throws IOException {
        DiveSpot diveSpot = null;
        if (spotId != null) {
            diveSpot = diveSpotDao.getModel(spotId);
        }
        return getLogbookRecordForm(diveSpot, null);
    }

    @RequestMapping(value = "/secure/editLogbookRecordForm.html", method = RequestMethod.GET)
    public ModelAndView editLogbookRecordForm(
            @RequestParam(value = "spotId", required = false) Long spotId,
            @RequestParam("logbookEntryId") Long logbookEntryId
    ) throws IOException {
        DiveSpot diveSpot = null;
        if (spotId != null) {
            diveSpot = diveSpotDao.getModel(spotId);
        }

        LogbookEntry logbookEntry = null;
        if (logbookEntryId != null) {
            logbookEntry = logbookEntryDao.getModel(logbookEntryId);
            if (logbookEntry == null) {
                throw new BadRequestException();
            }
            if (logbookEntry.getDigest() != null) {
                throw new BadRequestException();
            }
            if (spotId == null) {
                diveSpot = logbookEntry.getDiveSpot();
            }
        }
        return getLogbookRecordForm(diveSpot, logbookEntry);
    }

    private ModelAndView getLogbookRecordForm(DiveSpot diveSpot, LogbookEntry logbookEntry) {
        if (diveSpot != null) {
            diveSpot = HibernateDaoImpl.initializeAndUnproxy(diveSpot);
        }
        if (logbookEntry != null) {
            logbookEntry = HibernateDaoImpl.initializeAndUnproxy(logbookEntry);
        }
        ModelMap mm = new ModelMap();
        if (diveSpot != null) {
            mm.addAttribute("spot", diveSpot);
            mm.addAttribute("spotJson", gsonViewFactory.getLogbookEntryEditGson().toJson(diveSpot));
        }
        if (logbookEntry != null) {
            byte[] photo = logbookEntry.getPhoto();
            if (photo != null) {
                logbookEntry.setPhotoBase64(Base64Coder.encodeString(photo));
            }
            mm.addAttribute("logbookEntryJson", gsonViewFactory.getLogbookEntryEditGson().toJson(logbookEntry));
            mm.addAttribute("logbookEntry", logbookEntry);
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

        mm.addAttribute("weatherTypes", WeatherType.values());
        mm.addAttribute("surfaceTypes", SurfaceType.values());
        mm.addAttribute("currentTypes", CurrentType.values());
        mm.addAttribute("underWaterVisibilityTypes", UnderWaterVisibilityType.values());
        mm.addAttribute("waterTypes", WaterType.values());

        mm.addAttribute("temperatureMeasureUnits", TemperatureMeasureUnit.values());
        mm.addAttribute("divePurposeTypes", DivePurposeType.values());
        mm.addAttribute("entryTypes", EntryType.values());
        mm.addAttribute("diveSuitTypes", DiveSuitType.values());

        mm.addAttribute("volumeMeasureUnits", VolumeMeasureUnit.values());
        mm.addAttribute("pressureMeasureUnits", PressureMeasureUnit.values());
        mm.addAttribute("supplyTypes", TankSupplyType.values());

        return new ModelAndView("/secure/logbookRecordForm", mm);
    }

    @RequestMapping(value = "/secure/createRecord.html", method = RequestMethod.POST)
    public View createRecord(@RequestParam("logbookEntryJson") String logbookEntryJson) throws IOException {
        return saveOrUpdateRecord(logbookEntryJson, false);
    }

    @RequestMapping(value = "/secure/saveDraftRecord.html", method = RequestMethod.POST)
    public View saveDraftRecord(@RequestParam("logbookEntryJson") String logbookEntryJson) throws IOException {
        return saveOrUpdateRecord(logbookEntryJson, true);
    }

    private View saveOrUpdateRecord(String logbookEntryJson, boolean isDraft) {
        LogbookEntry formObject;
        try {
            formObject = gsonViewFactory.getLogbookEntryEditGson().fromJson(logbookEntryJson, LogbookEntry.class);
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
        if (!isDraft) {
            Errors errors = new MapBindingResult(new HashMap(), "logbookEntryJson");
            logbookEntryValidator.validate(formObject, errors);
            if (errors.hasErrors()) {
                return gsonViewFactory.createGsonView(new JsonBindingResult(errors));
            }
        }
        try {
            Diver diver = getCurrentDiver();
            long logbookEntryId = logbookService.createOrUpdateRecord(diver, formObject);
            return gsonViewFactory.createGsonView(new SuccessIdObject(logbookEntryId));
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
