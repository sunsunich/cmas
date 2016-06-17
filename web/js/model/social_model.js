var social_model = {

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
                loader_controller.stopwait();
                errorHandler();
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

    removeFriend: function (diverId, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/social/removeFriend.html",
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