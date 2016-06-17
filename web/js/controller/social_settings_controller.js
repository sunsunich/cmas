var social_settings_controller = {

    newsCountryController: {},

    init: function () {
        this.newsCountryController = simpleClone(country_controller);
        this.newsCountryController.inputId = "countryToNews";
        this.newsCountryController.drawIcon = false;
        this.newsCountryController.init();
        country_controller.inputId = "findDiverCountry";
        country_controller.drawIcon = false;
        country_controller.init();
        this.refreshFriends();
        this.refreshFriendRequests();

        this.setListeners();
    },

    refreshFriends: function () {
        var self = this;
        social_model.getFriends(
            function (json) {
                if (json.length > 0) {
                    $('#noFriendsText').hide();
                    $('#friendsPanel').html(
                        new EJS({url: '/js/templates/friends.ejs'}).render({"friends": json})
                    );
                    var i;
                    for (i = 0; i < json.length; i++) {
                        $('#' + json[i].id + '_removeFriend').click(function () {
                            self.removeFriend($(this)[0].id);
                        });
                    }
                }
                else {
                    $('#noFriendsText').show();
                }
            }
            , function () {
                $('#noFriendsText').show();
            }
        );
    },

    refreshFriendRequests: function () {
        var self = this;
        social_model.getToRequests(
            function (json) {
                if (json.length > 0) {
                    $('#toRequestsPanel').show();
                    $('#toRequests').html(
                        new EJS({url: '/js/templates/toRequests.ejs'}).render({"requests": json})
                    );
                    //var i;
                    //for (i = 0; i < json.length; i++) {
                    //    $('#' + json[i].id + '_removeFriend').click(function () {
                    //        self.removeFriend($(this)[0].id);
                    //    });
                    //}
                }
                else {
                    $('#toRequestsPanel').hide();
                }
            }
            , function () {
                $('#toRequestsPanel').hide();
            }
        );
        social_model.getFromRequests(
            function (json) {
                if (json.length > 0) {
                    $('#fromRequestsPanel').show();
                    $('#fromRequests').html(
                        new EJS({url: '/js/templates/fromRequests.ejs'}).render({"requests": json})
                    );
                }
                else {
                    $('#fromRequestsPanel').hide();
                }
            }
            , function () {
                $('#fromRequestsPanel').hide();
            }
        );
    },

    setListeners: function () {
        var self = this;
        $("#visibilityType").select2({
            escapeMarkup: function (m) {
                return m;
            }
        });
        $('#visibilityType').select2("val", labels[logbookVisibility]);

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
            $('#friendRequestResult').hide();
        });
    },

    findDiver: function () {
        var findDiverForm = {
            diverType: $("input[name=findDiverType]:checked").val(),
            country: $('#findDiverCountry').val(),
            name: $('#findDiverName').val()
        };

        this.cleanFindDiverErrors();
        var formErrors = this.validateFindDiverForm(findDiverForm);
        if (formErrors.success) {
            var self = this;
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
        $('#findDiverCountry').select2("val", '');
        $('#findDiverName').val('');
        this.cleanFindDiverErrors();
    },

    cleanFindDiverErrors: function () {
        $("#findDiver_error").empty().hide();
        $('#findDiver_error_diverType').empty();
        $('#findDiver_error_country').empty();
        $('#findDiver_error_name').empty();
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
                self.refreshFriendRequests();
                var notification;
                if (isStringTrimmedEmpty(json.message)) {
                    notification = labels["cmas.face.friendRequest.success"];
                }
                else {
                    self.refreshFriends();
                    notification = error_codes[json.message];
                }
                $('#' + diverId + '_addFriend').hide();
                $('#' + diverId + '_addFriendNotification')
                    .html(notification)
                    .show();
            }
            , function (json) {
                $('#' + diverId + '_addFriendNotification')
                    .html(labels["cmas.face.friendRequest.success"] + ' ' + error_codes[json.message])
                    .show();
            });
    },

    removeFriend: function (elemId) {
        var diverId = elemId.split('_')[0];
        var self = this;
        social_model.removeFriend(
            diverId
            , function (/*json*/) {
                self.refreshFriends();
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
