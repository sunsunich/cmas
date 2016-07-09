package org.cmas.presentation.controller.user;

import com.google.myjson.Gson;
import org.cmas.Globals;
import org.cmas.entities.Role;
import org.cmas.entities.User;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.divespot.DiveSpot;
import org.cmas.entities.logbook.DiveScore;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.entities.logbook.LogbookVisibility;
import org.cmas.presentation.dao.divespot.DiveSpotDao;
import org.cmas.presentation.dao.logbook.LogbookEntryDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.divespot.LatLngBounds;
import org.cmas.presentation.model.logbook.CreateLogbookEntryFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.mobile.DictionaryDataService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.util.Base64Coder;
import org.cmas.util.StringUtil;
import org.cmas.util.http.BadRequestException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
    private DictionaryDataService dictionaryDataService;

    @Autowired
    private DiveSpotDao diveSpotDao;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private LogbookEntryDao logbookEntryDao;

    @Autowired
    private GsonViewFactory gsonViewFactory;

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
        }
        if (diver == null) {
            throw new BadRequestException();
        }
        byte[] userpic = diver.getUserpic();
        if (userpic != null) {
            diver.setPhoto(Base64Coder.encodeString(userpic));
        }
        return diver;
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

    @RequestMapping(value = "/secure/createRecordForm.html", method = RequestMethod.GET)
    public ModelAndView createRecordForm(@RequestParam("spotId") Long spotId) throws IOException {
        DiveSpot diveSpot = diveSpotDao.getById(spotId);
        if (diveSpot == null) {
            throw new BadRequestException();
        }
        ModelMap mm = new ModelMap();
        mm.addAttribute("spot", diveSpot);
        try {
            mm.addAttribute("countries", dictionaryDataService.getCountries(0L));
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
        mm.addAttribute("visibilityTypes", LogbookVisibility.values());
        return new ModelAndView("/secure/createRecordForm", mm);
    }

    @RequestMapping(value = "/secure/createRecord.html", method = RequestMethod.POST)
    public View createRecord(@ModelAttribute("command") CreateLogbookEntryFormObject formObject, Errors errors) throws IOException {
        validator.validate(formObject, errors);
        if (errors.hasErrors()) {
            return gsonViewFactory.createGsonView(errors);
        }
        Diver diver = getCurrentDiver();
        Long spotId = Long.parseLong(formObject.getSpotId());
        DiveSpot diveSpot = diveSpotDao.getById(spotId);
        if (diveSpot == null) {
            throw new BadRequestException();
        }
        String instructorIdStr = formObject.getInstructorId();
        Diver instructor = null;
        if (!StringUtil.isTrimmedEmpty(instructorIdStr)) {
            Long instructorId = Long.parseLong(instructorIdStr);
            instructor = diverDao.getModel(instructorId);
            if (instructor == null) {
                throw new BadRequestException();
            }
        }

        try {
            LogbookEntry logbookEntry = new LogbookEntry();
            logbookEntry.setDiver(diver);
            logbookEntry.setDateCreation(new Date());
            logbookEntry.setDateEdit(new Date());
            logbookEntry.setDiveDate(Globals.getDTF().parse(formObject.getDiveDate()));
            logbookEntry.setDiveSpot(diveSpot);
            logbookEntry.setDurationMinutes(Integer.parseInt(formObject.getDuration()));
            logbookEntry.setScore(DiveScore.valueOf(formObject.getScore()));
            logbookEntry.setNote(formObject.getNote());
            logbookEntry.setInstructor(instructor);
            logbookEntry.setVisibility(LogbookVisibility.valueOf(formObject.getVisibility()));
            String photo = formObject.getPhoto();
            if (!StringUtil.isTrimmedEmpty(photo)) {
                logbookEntry.setPhoto(Base64Coder.decode(photo));
            }
            List<Long> buddiesIds = new Gson().fromJson(formObject.getBuddiesIds(), Globals.LONG_LIST_TYPE);
            if (buddiesIds != null && !buddiesIds.isEmpty()) {
                Set<Diver> buddies = new HashSet<>(diverDao.getDiversByIds(buddiesIds));
                if (instructor != null) {
                    buddies.remove(instructor);
                }
                buddies.remove(diver);
                logbookEntry.setBuddies(buddies);
            }
            logbookEntryDao.save(logbookEntry);
            //todo send notification to instructor
            //todo send notification to buddies

            return gsonViewFactory.createSuccessGsonView();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return gsonViewFactory.createErrorGsonView("validation.internal");
        }
    }
}
