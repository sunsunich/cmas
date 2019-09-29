package org.cmas.presentation.controller.user;

import org.cmas.Globals;
import org.cmas.backend.ImageStorageManager;
import org.cmas.entities.Country;
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
import org.cmas.presentation.controller.filter.AccessInterceptor;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.divespot.DiveSpotDao;
import org.cmas.presentation.dao.logbook.LogbookEntryDao;
import org.cmas.presentation.model.FileUploadBean;
import org.cmas.presentation.model.logbook.SearchLogbookEntryFormObject;
import org.cmas.presentation.service.mobile.DictionaryDataService;
import org.cmas.presentation.service.user.LogbookService;
import org.cmas.presentation.validator.user.LogbookEntryValidator;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.imageio.ImageIO;
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
public class LogbookController extends DiverAwareController {

    private final Logger log = LoggerFactory.getLogger(getClass());

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

    @Autowired
    private ImageStorageManager imageStorageManager;

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
            @RequestParam(AccessInterceptor.LOGBOOK_ENTRY_ID) Long logbookEntryId
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
            long logbookEntryId = logbookService.createOrUpdateRecord(diver, formObject, isDraft);
            return gsonViewFactory.createGsonView(logbookEntryDao.getModel(logbookEntryId));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return gsonViewFactory.createErrorGsonView("validation.internal");
        }
    }

    @RequestMapping(value = "/secure/addPhotoToRecord.html", method = RequestMethod.POST)
    public View addPhotoToRecord(@RequestParam(AccessInterceptor.LOGBOOK_ENTRY_ID) Long logbookEntryId, @ModelAttribute FileUploadBean fileBean) {
        MultipartFile file = fileBean.getFile();
        if (file == null) {
            return gsonViewFactory.createErrorGsonView("validation.emptyField");
        }
        if (!file.getContentType().startsWith("image")) {
            return gsonViewFactory.createErrorGsonView("validation.imageFormat");
        }
        if (file.getSize() > Globals.MAX_IMAGE_SIZE) {
            return gsonViewFactory.createErrorGsonView("validation.imageSize");
        }
        LogbookEntry logbookEntry = logbookEntryDao.getModel(logbookEntryId);
        if (logbookEntry == null) {
            throw new BadRequestException();
        }
        try {
            imageStorageManager.storeLogbookEntryImage(
                    logbookEntry, ImageIO.read(file.getInputStream()));
            logbookService.imageChanged(logbookEntry);
            return gsonViewFactory.createSuccessGsonView();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return gsonViewFactory.createErrorGsonView("validation.imageFormat");
        }
    }

    @RequestMapping(value = "/secure/deletePhotoFromRecord.html", method = RequestMethod.GET)
    public View deletePhotoFromRecord(@RequestParam(AccessInterceptor.LOGBOOK_ENTRY_ID) Long logbookEntryId) {
        LogbookEntry logbookEntry = logbookEntryDao.getModel(logbookEntryId);
        if (logbookEntry == null) {
            throw new BadRequestException();
        }
        imageStorageManager.deleteImage(logbookEntry);
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping(value = "/secure/getRecord.html", method = RequestMethod.GET)
    public View getRecord(@RequestParam(AccessInterceptor.LOGBOOK_ENTRY_ID) Long logbookEntryId
    ) {
        LogbookEntry logbookEntry = logbookEntryDao.getModel(logbookEntryId);
        if (logbookEntry == null) {
            throw new BadRequestException();
        }
        return gsonViewFactory.createGsonFeedView(Arrays.asList(logbookEntry));
    }

    @RequestMapping(value = "/secure/deleteRecord.html", method = RequestMethod.GET)
    public View deleteRecord(
            @RequestParam(AccessInterceptor.LOGBOOK_ENTRY_ID) Long logbookEntryId
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
        mm.addAttribute("countries", countryDao.getAll());
        return new ModelAndView("/secure/logbook", mm);
    }

    @RequestMapping(value = "/secure/getMyLogbookFeed.html", method = RequestMethod.GET)
    public View getMyLogbookFeed(
            @ModelAttribute("command") SearchLogbookEntryFormObject formObject, Errors errors
    ) throws IOException {
        Diver diver = getCurrentDiver();
        return gsonViewFactory.createGsonFeedView(
                logbookEntryDao.getDiverLogbookFeed(diver, formObject)
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
                logbookEntryDao.getDiverPublicLogbookFeed(diver, countries, formObject)
        );
    }

    @RequestMapping(value = "/getPublicLogbookFeed.html", method = RequestMethod.GET)
    public View getPublicLogbookFeed(
            @ModelAttribute("command") SearchLogbookEntryFormObject formObject, Errors errors
    ) throws IOException {
        //todo filters ?
        List<Country> countries = null;
        return gsonViewFactory.createGsonFeedView(
                logbookEntryDao.getPublicLogbookFeed(countries, formObject)
        );
    }

    @RequestMapping(value = "/secure/getMyFriendsLogbookFeed.html", method = RequestMethod.GET)
    public View getMyFriendsLogbookFeed(
            @ModelAttribute("command") SearchLogbookEntryFormObject formObject, Errors errors
    ) throws IOException {
        Diver diver = getCurrentDiver();
        return gsonViewFactory.createGsonFeedView(
                logbookEntryDao.getDiverFriendsLogbookFeed(diver, formObject)
        );
    }

    @RequestMapping(value = "/secure/getFriendsOnlyLogbookFeed.html", method = RequestMethod.GET)
    public View getFriendsOnlyLogbookFeed(
            @ModelAttribute("command") SearchLogbookEntryFormObject formObject, Errors errors
    ) throws IOException {
        Diver diver = getCurrentDiver();
        return gsonViewFactory.createGsonFeedView(
                logbookEntryDao.getFriendsOnlyLogbookFeed(diver, formObject)
        );
    }
}
