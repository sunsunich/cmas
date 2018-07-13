var social_model = {

    removeFriendId: 0,

    removeFriendRequestId: 0,

    socialUpdatesVersion: -1,

    logbookEntriesToProcessMap: {},

    getFriends: function (successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase("/secure/social/getFriends.html", {}, successHandler, unSuccessHandler);
    },

    getFromRequests: function (successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase("/secure/social/getFromRequests.html", {}, successHandler, unSuccessHandler);
    },

    getToRequests: function (successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase("/secure/social/getToRequests.html", {}, successHandler, unSuccessHandler);
    },

    getSocialUpdates: function (successHandler, unSuccessHandler) {
        var self = this;
        basicClient.sendGetRequest(
            false,
            "/secure/social/getSocialUpdates.html",
            {"version": self.socialUpdatesVersion},
            function (json) {
                if (self.socialUpdatesVersion < json.socialUpdatesVersion) {
                    successHandler(json);
                }
                self.socialUpdatesVersion = json.socialUpdatesVersion;
            },
            unSuccessHandler
        );
    },

    getLogbookEntry: function (logbookEntryId, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/getRecord.html",
            {"logbookEntryId": logbookEntryId},
            successHandler, unSuccessHandler
        );
    },

    searchNewFriends: function (form, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/social/searchNewFriends.html",
            form,
            successHandler, unSuccessHandler, function () {
                window.location.reload();
            }
        );
    },

    acceptFriendRequest: function (friendRequestId, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/social/acceptFriendRequest.html",
            {"friendRequestId": friendRequestId},
            successHandler, unSuccessHandler
        );
    },

    rejectFriendRequest: function (friendRequestId, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/social/rejectFriendRequest.html",
            {"friendRequestId": friendRequestId},
            successHandler, unSuccessHandler
        );
    },

    acceptLogbookEntryRequest: function (formObject, successHandler, unSuccessHandler) {
        basicClient.sendPostRequestCommonCase(
            "/secure/social/acceptLogbookBuddieRequest.html",
            {"logbookEntryRequestJson": JSON.stringify(formObject)},
            successHandler, unSuccessHandler
        );
    },

    rejectLogbookEntryRequest: function (requestId, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/social/rejectLogbookBuddieRequest.html",
            {"logbookBuddieRequestId": requestId},
            successHandler, unSuccessHandler
        );
    },

    removeFriend: function (successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/social/removeFriend.html",
            {"diverId": this.removeFriendId},
            successHandler, unSuccessHandler
        );
    },

    removeFriendRequest: function (successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/social/removeFriendRequest.html",
            {"friendRequestId": this.removeFriendRequestId},
            successHandler, unSuccessHandler
        );
    },

    setDefaultLogbookVisibility: function (defaultVisibility, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/social/setDefaultLogbookVisibility.html",
            {"defaultVisibility": defaultVisibility},
            successHandler, unSuccessHandler
        );
    },

    addCountryToNews: function (countryCode, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/social/addCountryToNews.html",
            {"countryCode": countryCode},
            successHandler, unSuccessHandler
        );
    },

    removeCountryFromNews: function (countryCode, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/social/removeCountryFromNews.html",
            {"countryCode": countryCode},
            successHandler, unSuccessHandler
        );
    },

    setAddLocationCountryToNewsFeed: function (addLocationCountryToNewsFeed, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/social/setAddLocationCountryToNewsFeed.html",
            {"addLocationCountryToNewsFeed": addLocationCountryToNewsFeed},
            successHandler, unSuccessHandler
        );
    }
};