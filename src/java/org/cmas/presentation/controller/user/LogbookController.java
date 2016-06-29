package org.cmas.presentation.controller.user;

import org.cmas.entities.User;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.presentation.dao.divespot.DiveSpotDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.divespot.LatLngBounds;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.json.gson.GsonViewFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.io.IOException;

/**
 * Created on Jun 27, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class LogbookController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private DiveSpotDao diveSpotDao;

    //  private LogbookEntryD

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @ModelAttribute("user")
    public BackendUser getUser() {
        BackendUser<? extends User> user = authenticationService.getCurrentUser();
        if (user == null) {
            throw new BadRequestException();
        }
        return user;
    }

    @RequestMapping(value = "/secure/showSpots.html", method = RequestMethod.GET)
    public ModelAndView showSpotsPage() throws IOException {
        ModelMap mm = new ModelMap();
        return new ModelAndView("/secure/spots", mm);
    }

    @RequestMapping(value = "/secure/getSpots.html", method = RequestMethod.GET)
    public View showSpots(@ModelAttribute("command") LatLngBounds bounds) throws IOException {
        return gsonViewFactory.createGsonView(diveSpotDao.getInMapBounds(bounds));
    }

    @RequestMapping(value = "/secure/createRecord.html", method = RequestMethod.GET)
    public View createRecordWithSpot(@RequestParam("spotId") Long spotId) throws IOException {
        DiveSpot diveSpot = diveSpotDao.getById(spotId);
        if (diveSpot == null) {
            throw new BadRequestException();
        }
        //todo implement
        return gsonViewFactory.createSuccessGsonView();
    }
}
