package org.cmas.presentation.controller.user;

import org.cmas.Globals;
import org.cmas.entities.Country;
import org.cmas.entities.diver.Diver;
import org.cmas.entities.logbook.DiverFriendRequest;
import org.cmas.entities.logbook.LogbookBuddieRequest;
import org.cmas.entities.logbook.LogbookEntry;
import org.cmas.entities.logbook.LogbookVisibility;
import org.cmas.entities.logbook.Request;
import org.cmas.json.SimpleGsonResponse;
import org.cmas.presentation.dao.CountryDao;
import org.cmas.presentation.dao.logbook.DiverFriendRequestDao;
import org.cmas.presentation.dao.logbook.LogbookBuddieRequestDao;
import org.cmas.presentation.dao.logbook.LogbookEntryDao;
import org.cmas.presentation.dao.user.sport.DiverDao;
import org.cmas.presentation.entities.user.BackendUser;
import org.cmas.presentation.model.logbook.LogbookEntryRequestFormObject;
import org.cmas.presentation.model.social.FindDiverFormObject;
import org.cmas.presentation.model.social.SocialUpdates;
import org.cmas.presentation.service.AuthenticationService;
import org.cmas.presentation.service.user.LogbookService;
import org.cmas.presentation.validator.HibernateSpringValidator;
import org.cmas.util.Base64Coder;
import org.cmas.util.StringUtil;
import org.cmas.util.http.BadRequestException;
import org.cmas.util.json.JsonBindingResult;
import org.cmas.util.json.gson.GsonViewFactory;
import org.hibernate.StaleObjectStateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger log = LoggerFactory.getLogger(getClass());

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
    private LogbookBuddieRequestDao logbookBuddieRequestDao;

    @Autowired
    private LogbookEntryDao logbookEntryDao;

    @Autowired
    private LogbookService logbookService;

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

    @RequestMapping("/secure/social/getSocialUpdates.html")
    public View getSocialUpdates(@RequestParam("version") long version) {
        Diver diver = getDiver();
        SocialUpdates socialUpdates = new SocialUpdates();
        long currentVersion = diver.getSocialUpdatesVersion();
        if (currentVersion > version) {
            socialUpdates.setFriends(diverDao.getFriends(diver));
            socialUpdates.setFromRequests(diverFriendRequestDao.getRequestsFromDiver(diver));
            socialUpdates.setToRequests(diverFriendRequestDao.getRequestsToDiver(diver));
            socialUpdates.setBuddieRequests(logbookBuddieRequestDao.getRequestsToDiver(diver));
        }
        socialUpdates.setSocialUpdatesVersion(currentVersion);
        return gsonViewFactory.createSocialUpdatesGsonView(socialUpdates);
    }

    @RequestMapping("/secure/social/getNewsCountries.html")
    public View getNewsCountries() {
        return gsonViewFactory.createGsonView(getDiver().getNewsFromCountries());
    }

    @RequestMapping("/secure/social/searchNewFriends.html")
    public View searchNewFriends(@ModelAttribute("command") FindDiverFormObject formObject, Errors result) {
        Diver currentDiver = getDiver();
        validator.validate(formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        }
        List<Diver> divers = diverDao.searchNotFriendDivers(currentDiver.getId(), formObject);
        for (Diver diver : divers) {
            byte[] userpic = diver.getUserpic();
            if (userpic != null) {
                diver.setPhoto(Base64Coder.encodeString(userpic));
            }
        }
        return gsonViewFactory.createGsonView(divers);
    }

    @RequestMapping("/secure/social/searchFriendsFast.html")
    public View searchFriendsFast(@RequestParam String input) {
        Diver currentDiver = getDiver();
        if (StringUtil.isTrimmedEmpty(input)) {
            return gsonViewFactory.createErrorGsonView("validation.diver.fast.search.empty");
        }
        if (input.length() < Globals.FAST_SEARCH_MIN_INPUT) {
            return gsonViewFactory.createErrorGsonView("validation.diver.fast.search.tooSmall");
        }
        List<Diver> divers = diverDao.searchFriendsFast(currentDiver.getId(), input);
        for (Diver diver : divers) {
            byte[] userpic = diver.getUserpic();
            if (userpic != null) {
                diver.setPhoto(Base64Coder.encodeString(userpic));
            }
        }
        return gsonViewFactory.createGsonView(divers);
    }

    @RequestMapping("/secure/social/searchDivers.html")
    public View searchDivers(@ModelAttribute("command") FindDiverFormObject formObject, Errors result) {
        validator.validate(formObject, result);
        if (result.hasErrors()) {
            return gsonViewFactory.createGsonView(new JsonBindingResult(result));
        }
        List<Diver> divers = diverDao.searchDivers(formObject);
        for (Diver diver : divers) {
            byte[] userpic = diver.getUserpic();
            if (userpic != null) {
                diver.setPhoto(Base64Coder.encodeString(userpic));
            }
        }
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
        DiverFriendRequest reverseRequest = diverFriendRequestDao.getDiverFriendRequest(friend, diver);
        if (reverseRequest != null) {
            addFriend(diver, friend);
            diverFriendRequestDao.deleteModel(reverseRequest);
            //todo separate case
            return gsonViewFactory.createGsonView(new SimpleGsonResponse(true, "validation.friendRequestSentToYou"));
        }
        diverFriendRequestDao.save(new DiverFriendRequest(diver, friend));
        updateSocialVersions(diver, friend);
        //todo send notification
        return gsonViewFactory.createSuccessGsonView();
    }

    private <T extends Request> FriendRequestValidationResult<T> validateFriendRequest(
            long requestId, boolean isFromCurrentDiver, boolean isLogbookRequest) {
        Diver diver = getDiver();
        @SuppressWarnings("unchecked")
        T request = isLogbookRequest
                ? (T) logbookBuddieRequestDao.getModel(requestId)
                : (T) diverFriendRequestDao.getModel(requestId);
        if (request == null) {
            return new FriendRequestValidationResult<>("validation.friendRequestAlreadyProcessed", null);
        }
        Diver from = isFromCurrentDiver ? request.getFrom() : request.getTo();
        if (from.getId() != diver.getId()) {
            throw new BadRequestException();
        }
        return new FriendRequestValidationResult<>(null, request);
    }

    @RequestMapping("/secure/social/acceptFriendRequest.html")
    public View acceptFriendRequest(@RequestParam("friendRequestId") long friendRequestId) {
        FriendRequestValidationResult<DiverFriendRequest> validationResult = validateFriendRequest(friendRequestId,
                                                                                                   false,
                                                                                                   false);
        if (validationResult.errorCode != null) {
            return gsonViewFactory.createErrorGsonView(validationResult.errorCode);
        }
        DiverFriendRequest friendRequest = validationResult.request;
        if (diverDao.isFriend(friendRequest.getFrom(), friendRequest.getTo())) {
            return gsonViewFactory.createErrorGsonView("validation.friendAlready");
        }
        Diver from = friendRequest.getFrom();
        Diver to = friendRequest.getTo();
        addFriend(from, to);
        diverFriendRequestDao.deleteModel(friendRequest);
        return gsonViewFactory.createSuccessGsonView();
    }

    private void updateSocialVersions(Diver from, Diver to) {
        from.setSocialUpdatesVersion(from.getSocialUpdatesVersion() + 1L);
        diverDao.updateModel(from);
        to.setSocialUpdatesVersion(to.getSocialUpdatesVersion() + 1L);
        diverDao.updateModel(to);
    }

    private void addFriend(Diver from, Diver to) {
        from.getFriends().add(to);
        updateSocialVersions(from, to);
        //todo send notification
    }

    @RequestMapping("/secure/social/rejectFriendRequest.html")
    public View rejectFriendRequest(@RequestParam("friendRequestId") long friendRequestId) {
        FriendRequestValidationResult<DiverFriendRequest> validationResult = validateFriendRequest(friendRequestId,
                                                                                                   false,
                                                                                                   false);
        if (validationResult.errorCode != null) {
            return gsonViewFactory.createErrorGsonView(validationResult.errorCode);
        }
        DiverFriendRequest friendRequest = validationResult.request;
        diverFriendRequestDao.deleteModel(friendRequest);
        Diver from = friendRequest.getFrom();
        Diver to = friendRequest.getTo();
        updateSocialVersions(from, to);
        //todo send notification
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/secure/social/removeFriendRequest.html")
    public View removeFriendRequest(@RequestParam("friendRequestId") long friendRequestId) {
        FriendRequestValidationResult<DiverFriendRequest> validationResult = validateFriendRequest(friendRequestId,
                                                                                                   true,
                                                                                                   false);
        if (validationResult.errorCode != null) {
            return gsonViewFactory.createErrorGsonView(validationResult.errorCode);
        }
        DiverFriendRequest friendRequest = validationResult.request;
        diverFriendRequestDao.deleteModel(friendRequest);
        Diver from = friendRequest.getFrom();
        Diver to = friendRequest.getTo();
        updateSocialVersions(from, to);
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
        updateSocialVersions(diver, friend);
        //todo send notification
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/secure/social/acceptLogbookBuddieRequest.html")
    public View acceptLogbookBuddieRequest(@RequestParam("logbookEntryRequestJson") String logbookEntryRequestJson) {
        LogbookEntryRequestFormObject formObject;
        try {
            formObject = gsonViewFactory.getLogbookEntryEditGson().fromJson(logbookEntryRequestJson, LogbookEntryRequestFormObject.class);
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
        FriendRequestValidationResult<LogbookBuddieRequest> validationResult = validateFriendRequest(
                formObject.getRequestId(),
                false,
                true);
        if (validationResult.errorCode != null) {
            return gsonViewFactory.createErrorGsonView(validationResult.errorCode);
        }
        LogbookBuddieRequest request = validationResult.request;
        LogbookBuddieRequest logbookBuddieRequest = validationResult.request;
        LogbookEntry logbookEntry = logbookBuddieRequest.getLogbookEntry();
        Diver to = request.getTo();
        if (logbookBuddieRequest.isToSign()) {
            if (!logbookEntry.getInstructor().equals(to) || logbookEntry.getDigest() != null) {
                logbookBuddieRequestDao.deleteModel(request);
                updateSocialVersions(logbookBuddieRequest.getFrom(), to);
                return gsonViewFactory.createErrorGsonView("validation.logbookEntryChanged");
            }
            try {
                String signature = logbookService.createSignature(logbookEntry);
                if (!signature.equals(logbookService.createSignature(formObject))) {
                    updateSocialVersions(logbookBuddieRequest.getFrom(), to);
                    return gsonViewFactory.createErrorGsonView("validation.logbookEntryChanged");
                }
                logbookEntry.setDigest(signature);
                logbookEntryDao.updateModel(logbookEntry);
            } catch (StaleObjectStateException e) {
                log.error(e.getMessage(), e);
                updateSocialVersions(logbookBuddieRequest.getFrom(), to);
                return gsonViewFactory.createErrorGsonView("validation.logbookEntryChanged");
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                return gsonViewFactory.createErrorGsonView("validation.internal");
            }
        }
        logbookBuddieRequestDao.deleteModel(request);
        updateSocialVersions(logbookBuddieRequest.getFrom(), to);
        return gsonViewFactory.createSuccessGsonView();
    }

    @RequestMapping("/secure/social/rejectLogbookBuddieRequest.html")
    public View rejectLogbookBuddieRequest(@RequestParam("logbookBuddieRequestId") long logbookBuddieRequestId) {
        FriendRequestValidationResult<LogbookBuddieRequest> validationResult = validateFriendRequest(
                logbookBuddieRequestId, false, true);
        if (validationResult.errorCode != null) {
            return gsonViewFactory.createErrorGsonView(validationResult.errorCode);
        }
        LogbookBuddieRequest request = validationResult.request;
        LogbookEntry logbookEntry = request.getLogbookEntry();
        Diver to = request.getTo();
        if (request.isToSign()) {
            if (!logbookEntry.getInstructor().equals(to) || logbookEntry.getDigest() != null) {
                logbookBuddieRequestDao.deleteModel(request);
                updateSocialVersions(request.getFrom(), to);
                return gsonViewFactory.createErrorGsonView("validation.logbookEntryChanged");
            }
            logbookEntry.setInstructor(null);
        } else {
            logbookEntry.getBuddies().remove(to);
        }
        try {
            logbookEntryDao.updateModel(logbookEntry);
        } catch (StaleObjectStateException e) {
            log.error(e.getMessage(), e);
            updateSocialVersions(request.getFrom(), to);
            return gsonViewFactory.createErrorGsonView("validation.logbookEntryChanged");
        }
        logbookBuddieRequestDao.deleteModel(request);
        updateSocialVersions(request.getFrom(), to);
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

    @RequestMapping("/secure/social/setDefaultLogbookVisibility.html")
    public View setDefaultLogbookVisibility(@RequestParam("defaultVisibility") String defaultVisibility) {
        LogbookVisibility visibility;
        try {
            visibility = LogbookVisibility.valueOf(defaultVisibility);
        } catch (Exception ignored) {
            return gsonViewFactory.createErrorGsonView("validation.incorrectField");
        }
        Diver diver = getDiver();
        diver.setDefaultVisibility(visibility);
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

    private static final class FriendRequestValidationResult<T extends Request> {
        private final String errorCode;
        private final T request;

        private FriendRequestValidationResult(String errorCode, T request) {
            this.errorCode = errorCode;
            this.request = request;
        }
    }
}
