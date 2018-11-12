var social_settings_controller = {

    timeout: 0,
    listSelection: null,
    findFriendsController: null,
    showFriendsController: null,

    init: function () {
        country_controller.inputId = "findDiverCountry";
        country_controller.drawIcon = false;
        country_controller.init();
        var self = this;
        this.findFriendsController = simpleClone(fast_search_friends_controller);
        this.findFriendsController.setupFoundElem = function (diverId) {
            self.setupFoundDiver(diverId);
        };
        this.findFriendsController.init();

        this.showFriendsController = simpleClone(fast_search_friends_controller);
        this.showFriendsController.fastSearchUrl = "/secure/social/searchFriendsFast.html"
        this.showFriendsController.prefix = "allFriends";
        this.showFriendsController.listElemEjs = "friends";
        this.showFriendsController.setupFoundElem = function (diverId) {
            self.setupFoundFriend(diverId);
        };

        this.start();
        this.setList("findFriends");
        this.setListeners();
    },

    start: function () {
        this.getSocialUpdates();
        var self = this;
        this.timeout = setTimeout(function run() {
            self.getSocialUpdates();
            self.timeout = setTimeout(run, 3000);
        }, 3000);
    },

    stop: function () {
        if (this.timeout) {
            clearTimeout(this.timeout);
            this.timeout = null;
        }
    },

    getSocialUpdates: function () {
        var self = this;
        social_model.getSocialUpdates(
            function (json) {
                var friends = json.friends;
                if (friends.length > 0) {
                    $('#noFriendsText').hide();
                    $('#friendsPanel').html(
                        new EJS({url: '/js/templates/friends.ejs?v=' + webVersion}).render({
                            "divers": friends,
                            "webVersion": webVersion,
                            "imagesData": imagesData
                        })
                    );
                    for (var i = 0; i < friends.length; i++) {
                        $('#' + friends[i].id + '_showFriendDiver').click(function (e) {
                            e.preventDefault();
                            util_controller.showDiver($(this)[0].id);
                        });
                        $('#' + friends[i].id + '_removeFriend').click(function () {
                            self.removeFriend($(this)[0].id);
                        });
                    }
                }
                else {
                    $('#friendsPanel').empty();
                    $('#noFriendsText').show();
                }
                var toRequests = json.toRequests;
                if (toRequests.length > 0) {
                    $('#toRequestsPanel').show();
                    $('#toRequests').html(
                        new EJS({url: '/js/templates/toRequests.ejs?v=' + webVersion}).render({
                            "requests": toRequests,
                            "webVersion": webVersion
                        })
                    );
                    for (i = 0; i < toRequests.length; i++) {
                        $('#' + toRequests[i].from.id + '_showFromDiver').click(function (e) {
                            e.preventDefault();
                            util_controller.showDiver($(this)[0].id);
                        });
                        $('#' + toRequests[i].id + '_acceptRequest').click(function () {
                            self.acceptFriendRequest($(this)[0].id);
                        });
                        $('#' + toRequests[i].id + '_rejectRequest').click(function () {
                            self.rejectFriendRequest($(this)[0].id);
                        });
                    }
                }
                else {
                    $('#toRequestsPanel').hide();
                }
                var fromRequests = json.fromRequests;
                if (fromRequests.length > 0) {
                    $('#fromRequestsPanel').show();
                    $('#fromRequests').html(
                        new EJS({url: '/js/templates/fromRequests.ejs?v=' + webVersion}).render({
                            "requests": fromRequests,
                            "webVersion": webVersion
                        })
                    );
                    for (i = 0; i < fromRequests.length; i++) {
                        $('#' + fromRequests[i].to.id + '_showToDiver').click(function (e) {
                            e.preventDefault();
                            util_controller.showDiver($(this)[0].id);
                        });
                        $('#' + fromRequests[i].id + '_removeFromRequest').click(function () {
                            self.removeFriendRequest($(this)[0].id);
                        });
                    }
                }
                else {
                    $('#fromRequestsPanel').hide();
                }
                var buddieRequests = json.buddieRequests;
                if (buddieRequests.length > 0) {
                    $('#buddieRequestsPanel').show();
                    $('#buddieRequests').html(
                        new EJS({url: '/js/templates/logbookRequests.ejs?v=' + webVersion}).render({
                            "requests": buddieRequests,
                            "webVersion": webVersion
                        })
                    );
                    for (i = 0; i < buddieRequests.length; i++) {
                        social_model.logbookEntriesToProcessMap[buddieRequests[i].id] = buddieRequests[i];
                        $('#' + buddieRequests[i].logbookEntry.id + '_showLogbookRequest').click(function (e) {
                            e.preventDefault();
                            self.showLogbookEntry($(this)[0].id);
                        });
                        $('#' + buddieRequests[i].id + '_acceptLogbookRequest').click(function () {
                            self.acceptLogbookRequest($(this)[0].id);
                        });
                        $('#' + buddieRequests[i].id + '_rejectLogbookRequest').click(function () {
                            self.rejectLogbookRequest($(this)[0].id);
                        });
                    }
                }
                else {
                    $('#buddieRequestsPanel').hide();
                }
            }
            , function () {
            }
        );
    },

    setList: function (listSelection) {
        var self = this;
        this.listSelection = listSelection;
        switch (this.listSelection) {
            case 'allFriends' :
                $('.panel-menu-item').removeClass('panel-menu-item-active');
                $('#allFriends').addClass('panel-menu-item-active');
                social_model.getFriends(
                    function (json) {
                        self.showFriendsController.renderFriends(json);
                    }
                    , function (json) {
                    }
                );
                break;
            case 'friendRequestsTo' :
                break;
        }
    },

    setListeners: function () {
        var self = this;
        $("#friendRequestSuccessDialogOk").click(function () {
            $('#friendRequestSuccessDialog').hide();
        });
        $("#friendRequestSuccessDialogClose").click(function () {
            $('#friendRequestSuccessDialog').hide();
        });
        $('#friendRemoveClose').click(function () {
            $('#friendRemove').hide();
        });
        $('#friendRemoveOk').click(function () {
            social_model.removeFriend(
                function (/*json*/) {
                    self.getSocialUpdates();
                    $('#friendRemove').hide();
                }
                , function (json) {
                    if (json && json.hasOwnProperty("message")) {
                        error_dialog_controller.showErrorDialog(error_codes[json.message]);
                    }
                    else {
                        error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
                    }
                });
        });
        $('#friendRequestRemoveClose').click(function () {
            $('#friendRequestRemove').hide();
        });
        $('#friendRequestRemoveOk').click(function () {
            social_model.removeFriendRequest(
                function (/*json*/) {
                    self.getSocialUpdates();
                    $('#friendRequestRemove').hide();
                }
                , function (json) {
                    if (json && json.hasOwnProperty("message")) {
                        error_dialog_controller.showErrorDialog(error_codes[json.message]);
                    }
                    else {
                        error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
                    }
                });
        });
    },

    setupFoundFriend: function (diverId) {
        var self = this;
        $('#' + diverId + '_showFriendDiver').click(function (e) {
            e.preventDefault();
            util_controller.showDiver($(this)[0].id);
        });

        const removeFriendId = '#' + diverId + '_removeFriend';
        $('#' + diverId + '_foundFriend').hover(
            function () {
                $(removeFriendId).show();
            }, function () {
                $(removeFriendId).hide();
            }
        );
        $(removeFriendId).click(function () {
            self.removeFriend($(this)[0].id);
        });
    },

    setupFoundDiver: function (diverId) {
        $('#' + diverId + '_foundFriend').click(function () {
            self.sendFriendRequest($(this)[0].id);
        });
    },

    sendFriendRequest: function (elemId) {
        var diverId = elemId.split('_')[0];
        var self = this;
        social_model.sendFriendRequest(
            diverId
            , function (json) {
                self.getSocialUpdates();
                var notification;
                if (isStringTrimmedEmpty(json.message)) {
                    notification = labels["cmas.face.friendRequest.success"];
                }
                else {
                    self.getSocialUpdates();
                    notification = error_codes[json.message];
                }
                $('#' + diverId + '_addFriend').hide();
                $('#' + diverId + '_addFriendNotification')
                    .html(notification)
                    .show();
            }
            , function (json) {
                $('#' + diverId + '_addFriendNotification')
                    .html(labels["cmas.face.friendRequest.failure"] + ' ' + error_codes[json.message])
                    .show();
            });
    },

    removeFriend: function (elemId) {
        var diverId = elemId.split('_')[0];
        social_model.removeFriendId = diverId;
        var diverName = $('#' + diverId + '_showFriendDiver').html();
        $('#removeDiverName').html(diverName);
        $('#friendRemove').show();
    },

    removeFriendRequest: function (elemId) {
        social_model.removeFriendRequestId = elemId.split('_')[0];
        $('#friendRequestRemove').show();
    },

    acceptFriendRequest: function (elemId) {
        var requestId = elemId.split('_')[0];
        var self = this;
        social_model.acceptFriendRequest(
            requestId
            , function (/*json*/) {
                self.getSocialUpdates();
            }
            , function (json) {
                if (json && json.hasOwnProperty("message")) {
                    error_dialog_controller.showErrorDialog(error_codes[json.message]);
                }
                else {
                    error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
                }
            });
    },

    rejectFriendRequest: function (elemId) {
        var requestId = elemId.split('_')[0];
        var self = this;
        social_model.rejectFriendRequest(
            requestId
            , function (/*json*/) {
                self.getSocialUpdates();
            }
            , function (json) {
                if (json && json.hasOwnProperty("message")) {
                    error_dialog_controller.showErrorDialog(error_codes[json.message]);
                }
                else {
                    error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
                }
            });
    }
};

$(document).ready(function () {
    social_settings_controller.init();
});
