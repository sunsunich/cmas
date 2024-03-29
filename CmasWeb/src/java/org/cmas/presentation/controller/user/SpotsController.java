package org.cmas.presentation.controller.user;

import org.cmas.Globals;
import org.cmas.entities.Country;
import org.cmas.entities.Toponym;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.presentation.controller.filter.AccessInterceptor;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.ToponymDao;
import org.cmas.presentation.dao.divespot.DiveSpotDao;
import org.cmas.presentation.model.divespot.DiveSpotFormObject;
import org.cmas.presentation.model.divespot.LatLngBounds;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.remote.json.IdObject;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.HashMap;
import java.util.List;

/**
 * Created on Dec 03, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class SpotsController extends DiverAwareController {

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @Autowired
    private DiveSpotDao diveSpotDao;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private ToponymDao toponymDao;

    @Autowired
    private HibernateSpringValidator validator;

    @RequestMapping(value = "/secure/showSpots.html", method = RequestMethod.GET)
    public ModelAndView showSpotsPage(
            @RequestParam(value = AccessInterceptor.LOGBOOK_ENTRY_ID, required = false) Long logbookEntryId)
            throws IOException {
        ModelMap mm = new ModelMap();
        mm.addAttribute("countries", countryDao.getAll());
        mm.addAttribute("logbookEntryId", logbookEntryId);
        return new ModelAndView("/secure/spots", mm);
    }

    @RequestMapping(value = "/secure/getSpots.html", method = RequestMethod.GET)
    public View getSpots(@ModelAttribute("command") LatLngBounds bounds) throws IOException {
        Diver diver = getCurrentDiver();
        return gsonViewFactory.createGsonView(diveSpotDao.getInMapBounds(bounds, diver, null));
    }

    private List<DiveSpot> getDiveSpotsByCoords(Diver diver, double latitude, double longitude) {
        LatLngBounds bounds = new LatLngBounds();
        bounds.setSwLatitude(latitude - Globals.HALF_DIVE_SPOT_DELTA_DEGREES);
        bounds.setNeLatitude(latitude + Globals.HALF_DIVE_SPOT_DELTA_DEGREES);
        bounds.setSwLongitude(longitude - Globals.DIVE_SPOT_DELTA_DEGREES);
        bounds.setNeLongitude(longitude + Globals.DIVE_SPOT_DELTA_DEGREES);

        return diveSpotDao.getInMapBounds(bounds, diver, null);
    }

    @RequestMapping(value = "/secure/getSpotByCoords.html", method = RequestMethod.GET)
    public View getSpotByCoords(@RequestParam("latitude") double latitude, @RequestParam("longitude") double longitude)
            throws IOException {
        List<DiveSpot> diveSpots = getDiveSpotsByCoords(getCurrentDiver(), latitude, longitude);
        if (diveSpots.isEmpty()) {
            return gsonViewFactory.createErrorGsonView("validation.spotNotFound");
        }
        return gsonViewFactory.createGsonView(diveSpots.get(0));
    }

    @RequestMapping(value = "/secure/createSpot.html", method = RequestMethod.GET)
    public View createSpot(@ModelAttribute("command") DiveSpotFormObject spotFormObject) throws IOException {
        Errors errors = new MapBindingResult(new HashMap(), "command");
        validator.validate(spotFormObject, errors);
        if (errors.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(errors));
        }
        double latitude = Double.parseDouble(spotFormObject.getLatitude());
        double longitude = Double.parseDouble(spotFormObject.getLongitude());
        Diver diver = getCurrentDiver();
        List<DiveSpot> diveSpots = getDiveSpotsByCoords(diver, latitude, longitude);
        boolean isNewDiveSpot = diveSpots.isEmpty();
        DiveSpot diveSpot;
        if (isNewDiveSpot) {
            diveSpot = new DiveSpot();
            diveSpot.setEditable(true);
            diveSpot.setCreator(diver);
        } else {
            diveSpot = diveSpots.get(0);
        }
        if (!diveSpot.isEditable()) {
            return gsonViewFactory.createErrorGsonView("error.spot.creation");
        }
        Country country = countryDao.getByCode(spotFormObject.getCountryCode());
        if (country == null) {
            return gsonViewFactory.createErrorGsonView("validation.country");
        }
        String toponymName = spotFormObject.getToponymName();
        Toponym toponym = toponymDao.getByCountryAndName(toponymName, country);
        if (toponym == null) {
            toponym = new Toponym();
            toponym.setName(toponymName);
            List<Country> countries = new ArrayList<>();
            countries.add(country);
            toponym.setCountries(countries);
            toponymDao.save(toponym);
        }
        diveSpot.setLatinName(spotFormObject.getLatinName());
        diveSpot.setName(spotFormObject.getName());
        diveSpot.setLatitude(latitude);
        diveSpot.setLongitude(longitude);
        diveSpot.setCountry(country);
        diveSpot.setToponym(toponym);
        if (!diveSpot.isAutoGeoLocation()) {
            diveSpot.setIsAutoGeoLocation(spotFormObject.isAutoGeoLocation());
        }

        Long spotId;
        if (isNewDiveSpot) {
            spotId = (Long) diveSpotDao.save(diveSpot);
        } else {
            spotId = diveSpot.getId();
            diveSpotDao.updateModel(diveSpot);
        }

        return gsonViewFactory.createGsonView(new IdObject(spotId));
    }

    @RequestMapping(value = "/secure/deleteSpot.html", method = RequestMethod.GET)
    public View deleteSpot(@RequestParam("spotId") long spotId) throws IOException {
        Diver currentDiver = getCurrentDiver();
        DiveSpot spot = diveSpotDao.getById(spotId);
        if (!diveSpotDao.isSpotEditable(spot, currentDiver)) {
            throw new BadRequestException();
        }
        spot.setDeleted(true);
        diveSpotDao.updateModel(spot);
        return gsonViewFactory.createSuccessGsonView();
    }
}
