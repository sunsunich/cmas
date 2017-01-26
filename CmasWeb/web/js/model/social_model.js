var social_model = {

    removeFriendId: 0,

    removeFriendRequestId: 0,

    socialUpdatesVersion: -1,

    logbookEntriesToProcessMap: {},

    simpleGetRequest: function (url, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: url,
            dataType: "json",
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                loader_controller.stopwait();
                errorHandler();
            }
        });
    },

    getFriends: function (successHandler, errorHandler) {
        this.simpleGetRequest("/secure/social/getFriends.html", successHandler, errorHandler);
    },

    getFromRequests: function (successHandler, errorHandler) {
        this.simpleGetRequest("/secure/social/getFromRequests.html", successHandler, errorHandler);
    },

    getToRequests: function (successHandler, errorHandler) {
        this.simpleGetRequest("/secure/social/getToRequests.html", successHandler, errorHandler);
    },

    getNewsCountries: function (successHandler, errorHandler) {
        this.simpleGetRequest("/secure/social/getNewsCountries.html", successHandler, errorHandler);
    },

    getSocialUpdates: function (successHandler, errorHandler) {
        var self = this;
        $.ajax({
            type: "Get",
            url: "/secure/social/getSocialUpdates.html",
            dataType: "json",
            data: {"version": self.socialUpdatesVersion},
            success: function (json) {
                if (self.socialUpdatesVersion < json.socialUpdatesVersion) {
                    successHandler(json);
                }
                self.socialUpdatesVersion = json.socialUpdatesVersion;
            },
            error: function () {
                errorHandler();
            }
        });
    },

    getLogbookEntry: function (logbookEntryId, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/getRecord.html",
            dataType: "json",
            data: {"logbookEntryId": logbookEntryId},
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                loader_controller.stopwait();
                errorHandler();
            }
        });
    },

    searchNewFriends: function (form, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/social/searchNewFriends.html",
            dataType: "json",
            data: form,
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                window.location.reload();
            }
        });
    },

    sendFriendRequest: function (diverId, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/social/sendFriendRequest.html",
            dataType: "json",
            data: {"diverId": diverId},
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                loader_controller.stopwait();
                errorHandler({"message": "validation.internal"});
            }
        });
    },

    acceptFriendRequest: function (friendRequestId, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/social/acceptFriendRequest.html",
            dataType: "json",
            data: {"friendRequestId": friendRequestId},
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                loader_controller.stopwait();
                errorHandler();
            }
        });
    },

    rejectFriendRequest: function (friendRequestId, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/social/rejectFriendRequest.html",
            dataType: "json",
            data: {"friendRequestId": friendRequestId},
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                loader_controller.stopwait();
                errorHandler();
            }
        });
    },

    acceptLogbookEntryRequest: function (formObject, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "POST",
            url: "/secure/social/acceptLogbookBuddieRequest.html",
            dataType: "json",
            data: {"logbookEntryRequestJson": JSON.stringify(formObject)},
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                loader_controller.stopwait();
                errorHandler();
            }
        });
    },

    rejectLogbookEntryRequest: function (requestId, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/social/rejectLogbookBuddieRequest.html",
            dataType: "json",
            data: {"logbookBuddieRequestId": requestId},
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                loader_controller.stopwait();
                errorHandler();
            }
        });
    },

    removeFriend: function (successHandler, errorHandler) {
        var self = this;
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/social/removeFriend.html",
            dataType: "json",
            data: {"diverId": self.removeFriendId},
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                loader_controller.stopwait();
                errorHandler();
            }
        });
    },

    removeFriendRequest: function (successHandler, errorHandler) {
        var self = this;
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/social/removeFriendRequest.html",
            dataType: "json",
            data: {"friendRequestId": self.removeFriendRequestId},
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                loader_controller.stopwait();
                errorHandler();
            }
        });
    },

    setDefaultLogbookVisibility: function (defaultVisibility, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/social/setDefaultLogbookVisibility.html",
            dataType: "json",
            data: {"defaultVisibility": defaultVisibility},
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                loader_controller.stopwait();
                errorHandler();
            }
        });
    },

    addCountryToNews: function (countryCode, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/social/addCountryToNews.html",
            dataType: "json",
            data: {"countryCode": countryCode},
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                loader_controller.stopwait();
                errorHandler();
            }
        });
    },

    removeCountryFromNews: function (countryCode, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/social/removeCountryFromNews.html",
            dataType: "json",
            data: {"countryCode": countryCode},
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                loader_controller.stopwait();
                errorHandler();
            }
        });
    },

    setAddTeamToLogbook: function (addTeamToLogbook, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/social/setAddTeamToLogbook.html",
            dataType: "json",
            data: {"addTeamToLogbook": addTeamToLogbook},
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                loader_controller.stopwait();
                errorHandler();
            }
        });
    },

    setAddLocationCountryToNewsFeed: function (addLocationCountryToNewsFeed, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/social/setAddLocationCountryToNewsFeed.html",
            dataType: "json",
            data: {"addLocationCountryToNewsFeed": addLocationCountryToNewsFeed},
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                loader_controller.stopwait();
                errorHandler();
            }
        });
    }
};