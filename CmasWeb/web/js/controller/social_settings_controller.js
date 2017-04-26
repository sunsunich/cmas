var social_settings_controller = {

    newsCountryController: {},
    timeout: null,

    init: function () {
        this.newsCountryController = simpleClone(country_controller);
        this.newsCountryController.inputId = "countryToNews";
        this.newsCountryController.drawIcon = false;
        this.newsCountryController.init();
        country_controller.inputId = "findDiverCountry";
        country_controller.drawIcon = false;
        country_controller.init();
        this.refreshNewsCountries();
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
                        new EJS({url: '/js/templates/friends.ejs'}).render({"friends": friends})
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
                        new EJS({url: '/js/templates/toRequests.ejs'}).render({"requests": toRequests})
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
                        new EJS({url: '/js/templates/fromRequests.ejs'}).render({"requests": fromRequests})
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
                        new EJS({url: '/js/templates/logbookRequests.ejs'}).render({"requests": buddieRequests})
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

    refreshNewsCountries: function () {
        var self = this;
        social_model.getNewsCountries(
            function (json) {
                if (json.length > 0) {
                    $('#newsCountries').html(
                        new EJS({url: '/js/templates/newsCountries.ejs'}).render({"countries": json})
                    ).show();
                    for (var i = 0; i < json.length; i++) {
                        $('#' + json[i].code + '_removeNewsCountry').click(function () {
                            var code = $(this)[0].id.split('_')[0];
                            social_model.removeCountryFromNews(code,
                                function () {
                                    self.refreshNewsCountries();
                                },
                                function (json) {
                                    if (json && json.hasOwnProperty("message")) {
                                        error_dialog_controller.showErrorDialog(error_codes[json.message]);
                                    }
                                    else {
                                        error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
                                    }
                                }
                            );
                        });
                    }
                }
                else {
                    $('#newsCountries').hide();
                }
            }
            , function () {
                $('#newsCountries').hide();
            }
        );
    },

    setListeners: function () {
        var self = this;

        $('#findDiverButton').click(function () {
            self.cleanFindDiverForm();
            $('#findDiver').show();
        });
        $("#findDiverClose").click(function () {
            $('#findDiver').hide();
        });
        $('#findDiverOk').click(function () {
            self.findDiver();
        });
        $("#noDiversFoundClose").click(function () {
            $('#noDiversFound').hide();
        });
        $('#noDiversFoundOk').click(function () {
            $('#noDiversFound').hide();
            $('#findDiver').show();
        });
        $('#newDiverSearch').click(function () {
            $('#diversList').hide();
            $('#mainContent').show();
            $('#findDiver').show();
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
        $('#showLogbookEntryClose').click(function () {
            $('#showLogbookEntry').hide();
        });
        $('#showLogbookEntryOk').click(function () {
            $('#showLogbookEntry').hide();
        });
        //$('#addTeamToLogbook').click(function () {
        //    var checked = $(this).prop("checked");
        //    social_model.setAddTeamToLogbook(
        //        checked,
        //        function (/*json*/) {
        //        },
        //        function (json) {
        //            if (json && json.hasOwnProperty("message")) {
        //                error_dialog_controller.showErrorDialog(error_codes[json.message]);
        //            }
        //            else {
        //                error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
        //            }
        //        }
        //    );
        //});

        $('#addLocationCountryToNewsFeed').click(function () {
            var checked = $(this).prop("checked");
            social_model.setAddLocationCountryToNewsFeed(
                checked,
                function (/*json*/) {
                },
                function (json) {
                    if (json && json.hasOwnProperty("message")) {
                        error_dialog_controller.showErrorDialog(error_codes[json.message]);
                    }
                    else {
                        error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
                    }
                }
            );
        });
        $('#addCountryClose').click(function () {
            $('#addCountry').hide();
        });
        $('#addCountryOk').click(function () {
            self.addCountryToNews();
        });
        $('#addCountryButton').click(function () {
            $('#addCountry').show();
        });
    },

    addCountryToNews: function () {
        var self = this;
        var countryCode = $('#countryToNews').val();
        $('#addCountryForm_error_countryCode').empty();
        $('#addCountryForm_error').empty().hide();
        var formErrors = {};
        formErrors.fieldErrors = {};
        formErrors.errors = [];
        if (isStringTrimmedEmpty(countryCode)) {
            formErrors.fieldErrors["countryCode"] = 'validation.emptyCountry';
        }
        formErrors.success = jQuery.isEmptyObject(formErrors.fieldErrors) && jQuery.isEmptyObject(formErrors.errors);
        if (formErrors.success) {
            social_model.addCountryToNews(
                countryCode,
                function (/*json*/) {
                    self.refreshNewsCountries();
                    $('#addCountry').hide();
                }
                , function (json) {
                    if (json) {
                        validation_controller.showErrors('addCountryForm', json);
                    }
                    else {
                        $('#addCountryForm_error').html(error_codes["validation.internal"]);
                    }
                }
            );
        }
        else {
            validation_controller.showErrors('addCountryForm', formErrors);
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
                    validation_controller.showErrors('findDiver', json);
                });
        }
        else {
            validation_controller.showErrors('findDiver', formErrors);
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
            result.fieldErrors["country"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(form.name)) {
            result.fieldErrors["name"] = 'validation.emptyField';
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
        var self = this;
        var logbookEntryId = elemId.split('_')[0];
        social_model.getLogbookEntry(
            logbookEntryId
            , function (json) {
                var record = json[0];
                $('#showLogbookEntryContent').html(
                    new EJS({url: '/js/templates/logbookEntryDialog.ejs'}).render({"record": record})
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
                new EJS({url: '/js/templates/diversList.ejs'}).render({"divers": divers})
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
    social_settings_controller.init();
});
