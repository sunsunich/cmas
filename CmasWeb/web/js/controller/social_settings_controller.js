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
        this.showFriendsController.fastSearchUrl = "/secure/social/searchInFriendsFast.html";
        this.showFriendsController.prefix = "allFriends";
        this.showFriendsController.listElemEjs = "friends";
        this.showFriendsController.setupFoundElem = function (diverId) {
            self.setupFoundFriend(diverId);
        };
        this.showFriendsController.emptyInputCallback = function () {
            social_model.getFriends(
                function (json) {
                    self.showFriendsController.renderFriends(json);
                }
                , function (json) {
                }
            );
        };
        this.showFriendsController.init();
        this.start();
        this.setListeners();
    },

    start: function () {
        this.getSocialUpdates(true);
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

    getSocialUpdates: function (firstCall) {
        var self = this;
        social_model.getSocialUpdates(
            function (json) {
                var friends = json.friends;
                if (firstCall) {
                    if (friends && friends.length > 0) {
                        self.setList("allFriends");
                        self.showFriendsController.renderFriends(friends);
                    } else {
                        self.setList("findFriends");
                    }
                } else {
                    if (!$('#allFriends_input').val()) {
                        self.showFriendsController.renderFriends(friends);
                    }
                }
                var toRequests = json.toRequests;
                if (toRequests.length > 0) {
                    $('#friendRequestsTo_notFound').hide();
                    $('#friendRequestsTo_list').html(
                        new EJS({url: '/js/templates/toRequests.ejs?v=' + webVersion}).render({
                            "requests": toRequests,
                            "webVersion": webVersion,
                            "suffix": ""
                        })
                    );
                    for (var i = 0; i < toRequests.length; i++) {
                        self.setupFriendRequestTo(toRequests[i]);
                    }
                    $('#friendRequestOne').show();
                    $('#friendRequestsOne_list').html(
                        new EJS({url: '/js/templates/toRequests.ejs?v=' + webVersion}).render({
                            "requests": [toRequests[0]],
                            "webVersion": webVersion,
                            "suffix": "_one"
                        })
                    );
                    self.setupFriendRequestTo(toRequests[0], "_one");
                    $('#friendRequestsTo_one').click(function () {
                        self.setList('friendRequestsTo');
                    });
                }
                else {
                    $('#friendRequestOne').hide();
                    $('#friendRequestsTo_list').empty();
                    $('#friendRequestsTo_notFound').show();
                }
                var fromRequests = json.fromRequests;
                if (fromRequests.length > 0) {
                    $('#friendRequestsFrom_notFound').hide();
                    $('#friendRequestsFrom_list').html(
                        new EJS({url: '/js/templates/fromRequests.ejs?v=' + webVersion}).render({
                            "requests": fromRequests,
                            "webVersion": webVersion
                        })
                    );
                    for (i = 0; i < fromRequests.length; i++) {
                        self.setupFriendRequestFrom(fromRequests[i]);
                    }
                }
                else {
                    $('#friendRequestsFrom_list').empty();
                    $('#friendRequestsFrom_notFound').show();
                }
            }
            , function () {
            }
        );
    },

    setList: function (listSelection) {
        this.listSelection = listSelection;
        switch (this.listSelection) {
            case 'allFriends' :
                this.hideAllBlocks();
                $('#allFriendsBlock').show();
                $('.panel-menu-item').removeClass('panel-menu-item-active');
                $('#allFriends').addClass('panel-menu-item-active');
                break;
            case 'findFriends' :
                this.hideAllBlocks();
                $('#findFriendsBlock').show();
                $('.panel-menu-item').removeClass('panel-menu-item-active');
                $('#findFriends').addClass('panel-menu-item-active');
                break;
            case 'friendRequestsTo' :
                this.hideAllBlocks();
                $('#friendRequestsToBlock').show();
                $('.panel-menu-item').removeClass('panel-menu-item-active');
                $('#friendRequestsTo').addClass('panel-menu-item-active');
                break;
            case 'friendRequestsFrom':
                this.hideAllBlocks();
                $('#friendRequestsFromBlock').show();
                $('.panel-menu-item').removeClass('panel-menu-item-active');
                $('#friendRequestsFrom').addClass('panel-menu-item-active');
                break;
        }
    },

    hideAllBlocks: function () {
        $('#allFriendsBlock').hide();
        $('#findFriendsBlock').hide();
        $('#friendRequestsToBlock').hide();
        $('#friendRequestsFromBlock').hide();
    },

    setListeners: function () {
        var self = this;
        $("#findFriends").click(function () {
            self.setList('findFriends');
        });
        $("#allFriends").click(function () {
            self.setList('allFriends');
        });
        $("#friendRequestsTo").click(function () {
            self.setList('friendRequestsTo');
        });
        $("#friendRequestsFrom").click(function () {
            self.setList('friendRequestsFrom');
        });

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
        $('#' + diverId + '_foundFriend').hover(
            function () {
                var elemId = $(this)[0].id;
                var diverId = elemId.split('_')[0];
                $('#' + diverId + '_removeFriend').show();
            }, function () {
                var elemId = $(this)[0].id;
                var diverId = elemId.split('_')[0];
                $('#' + diverId + '_removeFriend').hide();
            }
        );
        $('#' + diverId + '_removeFriend').click(function () {
            self.removeFriend($(this)[0].id);
        });
    },

    setupFoundDiver: function (diverId) {
        var self = this;
        $('#' + diverId + '_showFoundDiver').click(function (e) {
            e.preventDefault();
            util_controller.showDiver($(this)[0].id);
        });
        $('#' + diverId + '_addFriend').click(function () {
            self.sendFriendRequest($(this)[0].id);
        });
    },

    setupFriendRequestTo: function (toRequest, suffix) {
        if (!suffix) {
            suffix = "";
        }
        var self = this;
        $('#' + toRequest.from.id + '_showRequestToDiver' + suffix).click(function (e) {
            e.preventDefault();
            util_controller.showDiver($(this)[0].id);
        });
        $('#' + toRequest.id + '_acceptRequest' + suffix).click(function () {
            self.acceptFriendRequest($(this)[0].id);
        });
        $('#' + toRequest.id + '_rejectRequest' + suffix).click(function () {
            self.rejectFriendRequest($(this)[0].id);
        });
    },

    setupFriendRequestFrom: function (fromRequest) {
        var self = this;
        $('#' + fromRequest.to.id + '_showRequestFromDiver').click(function (e) {
            e.preventDefault();
            util_controller.showDiver($(this)[0].id);
        });
        $('#' + fromRequest.id + '_removeFromRequest').click(function () {
            self.removeFriendRequest($(this)[0].id);
        });
        $('#' + fromRequest.id + '_requestFrom').hover(
            function () {
                var elemId = $(this)[0].id;
                var fromRequestId = elemId.split('_')[0];
                $('#' + fromRequestId + '_removeFromRequest').show();
            }, function () {
                var elemId = $(this)[0].id;
                var fromRequestId = elemId.split('_')[0];
                $('#' + fromRequestId + '_removeFromRequest').hide();
            }
        );
    },

    sendFriendRequest: function (elemId) {
        var diverId = elemId.split('_')[0];
        var self = this;
        social_model.sendFriendRequest(
            diverId
            , function (json) {
                self.getSocialUpdates();
                var notification;
                var textClass;
                if (isStringTrimmedEmpty(json.message)) {
                    notification = labels["cmas.face.friendRequest.success"];
                    textClass = 'friendList-floating-elem-notification-positive';
                }
                else {
                    $('#' + diverId + '_addFriendNotificationImg')
                        .prop('src', '/i/button_clean_click.png?v=' + webVersion);
                    textClass = 'friendList-floating-elem-notification-negative';
                    notification = error_codes[json.message];
                }
                $('#' + diverId + '_addFriend').hide();
                $('#' + diverId + '_addFriendNotification').addClass(textClass).html(notification);
                $('#' + diverId + '_addFriendNotificationContainer').show();
            }
            , function (json) {
                if (json.message == "validation.friendRequestAlreadyExists") {
                    $('#' + diverId + '_addFriend').hide();
                }
                $('#' + diverId + '_addFriendNotificationImg')
                    .prop('src', '/i/button_clean_click.png?v=' + webVersion);
                $('#' + diverId + '_addFriendNotification')
                    .addClass('friendList-floating-elem-notification-negative')
                    .html(error_codes[json.message]);
                $('#' + diverId + '_addFriendNotificationContainer').show();
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
