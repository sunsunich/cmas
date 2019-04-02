var notifications_controller = {

    timeout: 0,
    listSelection: null,

    init: function () {
        country_controller.inputId = "findDiverCountry";
        country_controller.drawIcon = false;
        country_controller.init();
        this.start();
        this.setList("allFriends");
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
                            "friends": friends,
                            "webVersion": webVersion
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
                        self.renderFriends(json);
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
        $('#showLogbookEntryClose').click(function () {
            $('#showLogbookEntry').hide();
        });
        $('#showLogbookEntryOk').click(function () {
            $('#showLogbookEntry').hide();
        });
    },

    renderFriends: function (divers) {
        var self = this;
        $('#foundFriendList').show();
        if (divers && divers.length > 0) {
            $('#noDiversFoundMessage').hide();
            $('#foundFriendListContent').html(
                new EJS({url: '/js/templates/foundFriendList.ejs?v=' + webVersion}).render({
                    "divers": divers,
                    "webVersion": webVersion,
                    "imagesData" : imagesData
                })
            ).show();
            var i;
            for (i = 0; i < divers.length; i++) {
                $('#' + divers[i].id + '_foundFriend').click(function () {
                    self.sendFriendRequest($(this)[0].id);
                });
            }
        } else {
            $('#foundFriendListContent').empty();
            $('#noDiversFoundMessage').show();
        }
    },

    findDiver: function () {
        var self = this;
        var findDiverForm = {
            diverType: $("input[name=findDiverType]:checked").val(),
            country: $('#findDiverCountry').val(),
            name: $('#findDiverName').val()
        };

        this.cleanFindDiverErrors();
        var formErrors = this.validateFindDiverForm(findDiverForm);
        if (formErrors.success) {
            social_model.searchNewFriends(
                findDiverForm
                , function (json) {
                    self.showFoundDivers(json);
                }
                , function (json) {
                    validation_controller.simpleShowErrors('findDiver', json);
                });
        }
        else {
            validation_controller.simpleShowErrors('findDiver', formErrors);
        }
    },

    validateFindDiverForm: function (form) {
        var result = {};
        result.fieldErrors = {};
        result.errors = [];
        if (isStringTrimmedEmpty(form.diverType)) {
            result.fieldErrors["diverType"] = 'validation.diverTypeEmpty';
        }
        if (isStringTrimmedEmpty(form.country)) {
            result.fieldErrors["country"] = 'validation.emptyCountry';
        }
        if (isStringTrimmedEmpty(form.name)) {
            result.fieldErrors["name"] = 'validation.emptyName';
        }
        else if (form.name.length < 3) {
            result.fieldErrors["name"] = 'validation.searchNameTooShort';
        }

        result.success = jQuery.isEmptyObject(result.fieldErrors) && jQuery.isEmptyObject(result.errors);
        return result;
    },

    cleanFindDiverForm: function () {
        $("input[name=findDiverType]:checked").attr('checked', false);
        $('#findDiverCountry').val('').trigger("change");
        $('#findDiverName').val('');
        this.cleanFindDiverErrors();
    },

    cleanFindDiverErrors: function () {
        $("#findDiver_error").empty().hide();
        $('#findDiver_error_diverType').empty();
        $('#findDiver_error_country').empty();
        $('#findDiver_error_name').empty();
    },

    showLogbookEntry: function (elemId) {
        var logbookEntryId = elemId.split('_')[0];
        social_model.getLogbookEntry(
            logbookEntryId
            , function (json) {
                var record = json[0];
                $('#showLogbookEntryContent').html(
                    new EJS({url: '/js/templates/logbookEntryDialog.ejs?v=' + webVersion}).render({"record": record})
                );
                $('#' + record.diver.id + '_logbookRecordDialog' + '_' + record.id + '_showDiver').click(function (e) {
                    e.preventDefault();
                    util_controller.showDiver($(this)[0].id);
                });
                if (record.instructor) {
                    $('#' + record.instructor.id + '_logbookRecordDialog' + '_' + record.id + '_showDiver').click(function (e) {
                        e.preventDefault();
                        util_controller.showDiver($(this)[0].id);
                    });
                }
                if (record.buddies) {
                    for (var i = 0; i < record.buddies.length; i++) {
                        $('#' + record.buddies[i].id + '_logbookRecordDialog' + '_' + record.id + '_showDiver').click(function (e) {
                            e.preventDefault();
                            util_controller.showDiver($(this)[0].id);
                        });
                    }
                }
                $('#showLogbookEntry').show();
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

    showFoundDivers: function (divers) {
        var self = this;
        $('#findDiver').hide();
        if (divers.length > 0) {
            $('#diversListContent').html(
                new EJS({url: '/js/templates/diversList.ejs?v=' + webVersion}).render({
                    "divers": divers,
                    "webVersion": webVersion,
                    "imagesData": imagesData
                })
            );
            var i;
            for (i = 0; i < divers.length; i++) {
                $('#' + divers[i].id + '_addFriend').click(function () {
                    self.sendFriendRequest($(this)[0].id);
                });
            }
            $('#mainContent').hide();
            $('#diversList').show();
        } else {
            $('#noDiversFound').show();
        }
    },

    sendFriendRequest: function (elemId) {
        var diverId = elemId.split('_')[0];
        var self = this;
        fast_search_friends_model.sendFriendRequest(
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
    },

    acceptLogbookRequest: function (elemId) {
        var requestId = elemId.split('_')[0];
        var logbookEntry = social_model.logbookEntriesToProcessMap[requestId].logbookEntry;
        logbookEntry.requestId = requestId;
        var self = this;
        social_model.acceptLogbookEntryRequest(
            logbookEntry
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

    rejectLogbookRequest: function (elemId) {
        var requestId = elemId.split('_')[0];
        var self = this;
        social_model.rejectLogbookEntryRequest(
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
    notifications_controller.init();
});
