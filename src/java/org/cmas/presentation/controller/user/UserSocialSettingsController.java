package org.cmas.presentation.controller.user;

import org.cmas.entities.Country;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.DiverFriendRequest;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.logbook.DiverFriendRequestDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.social.FindDiverFormObject;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.json.gson.GsonViewFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import java.util.List;

/**
 * Created on May 02, 2016
 *
 * @author Alexander Petukhov
 */
@SuppressWarnings("HardcodedFileSeparator")
@Controller
public class UserSocialSettingsController {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private HibernateSpringValidator validator;

    @Autowired
    private DiverDao diverDao;

    @Autowired
    private CountryDao countryDao;

    @Autowired
    private DiverFriendRequestDao diverFriendRequestDao;

    @Autowired
    private GsonViewFactory gsonViewFactory;

    @RequestMapping("/secure/social/getFriends.html")
    public View getFriends() {
        return gsonViewFactory.createGsonView(diverDao.getFriends(getDiver()));
    }

    @RequestMapping("/secure/social/getFromRequests.html")
    public View getFromRequests() {
        return gsonViewFactory.createGsonView(diverFriendRequestDao.getRequestsFromDiver(getDiver()));
    }

    @RequestMapping("/secure/social/getToRequests.html")
    public View getToRequests() {
        return gsonViewFactory.createGsonView(diverFriendRequestDao.getRequestsToDiver(getDiver()));
    }

    @RequestMapping("/secure/social/searchNewFriends.html")
    public View searchNewFriends(@ModelAttribute("command") FindDiverFormObject formObject, Errors result) {
        Diver diver = getDiver();
        validator.validate(formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(result);
        }
        List<Diver> divers = diverDao.searchNotFriendDivers(diver.getId(), formObject);
        return gsonViewFactory.createGsonView(divers);
    }

    @RequestMapping("/secure/social/sendFriendRequest.html")
    public View sendFriendRequest(@RequestParam("diverId") long diverId) {
        Diver diver = getDiver();
        Diver friend = diverDao.getModel(diverId);
        if (friend == null) {
            throw new BadRequestException();
        }
        if (diverFriendRequestDao.hasDiverFriendRequest(diver, friend)) {
            return gsonViewFactory.createErrorGsonView("validation.friendRequestAlreadyExists");
        }
        if (diverDao.isFriend(diver, friend)) {
            return gsonViewFactory.createErrorGsonView("validation.friendAlready");
        }
        if (diverFriendRequestDao.hasDiverFriendRequest(friend, diver)) {
            addFriend(diver, friend);
            //todo separate case
            return gsonViewFactory.createGsonView(new SimpleGsonResponse(true, "validation.friendRequestSentToYou"));
        }
        diverFriendRequestDao.save(new DiverFriendRequest(diver, friend));
        //todo send notification
        return gsonViewFactory.createSuccessGsonView();
    }

    private FriendRequestValidationResult validateFriendRequest(@RequestParam("friendRequestId") long friendRequestId) {
        Diver diver = getDiver();
        DiverFriendRequest friendRequest = diverFriendRequestDao.getModel(friendRequestId);
        if (friendRequest == null) {
            return new FriendRequestValidationResult("validation.friendRequestAlreadyProcessed", null);
        }
        Diver from = friendRequest.getFrom();
        if (from.getId() != diver.getId()) {
            throw new BadRequestException();
        }
        return new FriendRequestValidationResult(null, friendRequest);
    }

    @RequestMapping("/secure/social/acceptFriendRequest.html")
    public View acceptFriendRequest(@RequestParam("friendRequestId") long friendRequestId) {
        FriendRequestValidationResult validationResult = validateFriendRequest(friendRequestId);
        if (validationResult.errorCode != null) {
            return gsonViewFactory.createErrorGsonView(validationResult.errorCode);
        }
        DiverFriendRequest friendRequest = validationResult.friendRequest;
        if (diverDao.isFriend(friendRequest.getFrom(), friendRequest.getTo())) {
            return gsonViewFactory.createErrorGsonView("validation.friendAlready");
        }
        Diver from = friendRequest.getFrom();
        Diver to = friendRequest.getTo();
        addFriend(from, to);
        return gsonViewFactory.createSuccessGsonView();
    }

    private void addFriend(Diver from, Diver to) {
        from.getFriends().add(to);
        diverDao.save(from);
        //todo send notification
    }

    @RequestMapping("/secure/social/rejectFriendRequest.html")
    public View rejectFriendRequest(@RequestParam("friendRequestId") long friendRequestId) {
        FriendRequestValidationResult validationResult = validateFriendRequest(friendRequestId);
        if (validationResult.errorCode != null) {
            return gsonViewFactory.createErrorGsonView(validationResult.errorCode);
        }
        DiverFriendRequest friendRequest = validationResult.friendRequest;
        diverFriendRequestDao.deleteModel(friendRequest);
        //todo send notification
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/secure/social/removeFriendRequest.html")
    public View removeFriendRequest(@RequestParam("friendRequestId") long friendRequestId) {
        FriendRequestValidationResult validationResult = validateFriendRequest(friendRequestId);
        if (validationResult.errorCode != null) {
            return gsonViewFactory.createErrorGsonView(validationResult.errorCode);
        }
        DiverFriendRequest friendRequest = validationResult.friendRequest;
        diverFriendRequestDao.deleteModel(friendRequest);
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/secure/social/removeFriend.html")
    public View removeFriend(@RequestParam("diverId") long diverId) {
        Diver diver = getDiver();
        Diver friend = diverDao.getModel(diverId);
        if (friend == null) {
            throw new BadRequestException();
        }
        diverDao.removeFriend(diver, friend);
        //todo send notification
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/secure/social/addCountryToNews.html")
    public View addCountryToNews(@RequestParam("countryCode") String countryCode) {
        Diver diver = getDiver();
        Country country = countryDao.getByCode(countryCode);
        if (country == null) {
            throw new BadRequestException();
        }
        diver.getNewsFromCountries().add(country);
        diverDao.save(diver);
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/secure/social/removeCountryFromNews.html")
    public View removeCountryFromNews(@RequestParam("countryCode") String countryCode) {
        Diver diver = getDiver();
        Country country = countryDao.getByCode(countryCode);
        if (country == null) {
            throw new BadRequestException();
        }
        diver.getNewsFromCountries().remove(country);
        diverDao.save(diver);
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/secure/social/setAddTeamToLogbook.html")
    public View setAddTeamToLogbook(@RequestParam("addTeamToLogbook") boolean addTeamToLogbook) {
        Diver diver = getDiver();
        diver.setIsAddFriendsToLogbookEntries(addTeamToLogbook);
        diverDao.save(diver);
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/secure/social/setAddLocationCountryToNewsFeed.html")
    public View setAddLocationCountryToNewsFeed(@RequestParam("addLocationCountryToNewsFeed") boolean addLocationCountryToNewsFeed) {
        Diver diver = getDiver();
        diver.setIsNewsFromCurrentLocation(addLocationCountryToNewsFeed);
        diverDao.save(diver);
        return gsonViewFactory.createSuccessGsonView();
    }

    private Diver getDiver() {
        BackendUser<Diver> currentDiver = authenticationService.getCurrentDiver();
        if (currentDiver == null) {
            throw new BadRequestException();
        }
        return currentDiver.getUser();
    }

    private static final class FriendRequestValidationResult {
        private final String errorCode;
        private final DiverFriendRequest friendRequest;

        private FriendRequestValidationResult(String errorCode, DiverFriendRequest friendRequest) {
            this.errorCode = errorCode;
            this.friendRequest = friendRequest;
        }
    }
}
