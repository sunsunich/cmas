var profile_controller = {

    myFriendsFeedController: null,

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;

        var tabName = cookie_controller.readCookie("PROFILE_TAB");
        if (tabName) {
            self.showTab(tabName);
        }
        else {
            self.showTab('PRIVATE');
        }
        $('#privateTab').click(function () {
            self.showTab('PRIVATE');
        });
        $('#socialTab').click(function () {
            self.showTab('SOCIAL');
        });

        $('.userpic-selection-right a').click(function (event) {
            event.preventDefault();
        });

        $('#changePasswordButton').click(function () {
            $('#changePasswordSuccessMessage').hide();
            $("#changePasswordForm").show();
            $("#changePasswordOk").show();
            $('#changePassword').show();
        });
        $("#changePasswordOk").click(function () {
            self.changePassword();
        });
        $("#changePasswordClose").click(function () {
            $('#changePassword').hide();
        });
        $("#changePasswordFinishedOk").click(function () {
            $('#changePassword').hide();
        });

        $('#changeEmailButton').click(function () {
            $('#changeEmailSuccessMessage').hide();
            $("#changeEmailForm").show();
            $("#changeEmailOk").show();
            $('#changeEmail').show();
        });
        $("#changeEmailOk").click(function () {
            self.changeEmail();
        });
        $("#changeEmailClose").click(function () {
            $('#changeEmail').hide();
        });
        $("#changeEmailFinishedOk").click(function () {
            $('#changeEmail').hide();
        });

        $("#cardReload").click(function () {
            self.loadPrimaryCard();
        });
        self.loadPrimaryCard();

        var my_friends_logbook_feed_model = simpleClone(logbook_feed_model);
        my_friends_logbook_feed_model.isMyRecords = false;
        my_friends_logbook_feed_model.url = "/secure/getMyFriendsLogbookFeed.html";
        my_friends_logbook_feed_model.containerId = 'accountFeed';
        this.myFriendsFeedController = simpleClone(logbook_feed_controller);
        this.myFriendsFeedController.model = my_friends_logbook_feed_model;
        this.myFriendsFeedController.start();
    },

    showTab: function (tabName) {
        if (tabName == 'PRIVATE') {
            $('#socialTab').addClass('inactive');
            $('#privateTab').removeClass('inactive');
            $('#socialSettings').hide();
            $('#privateSettings').show();
        } else {
            $('#privateTab').addClass('inactive');
            $('#socialTab').removeClass('inactive');
            $('#privateSettings').hide();
            $('#socialSettings').show();
        }
        cookie_controller.createCookie("PROFILE_TAB", tabName, 0);
    },

    loadPrimaryCard: function () {
        profile_model.loadCard(
            cmas_primaryCardId
            , function (json) {
                $('#noCard').hide();
                $('#cardImg').attr("src", "data:image/png;base64," + json.base64);
                $('#card').show();
            }
            , function () {
                $('#noCard').show();
                $('#card').hide();
            });
    },

    changePassword: function () {
        var changePasswordForm = {
            oldPassword: $('#oldPassword').val(),
            password: $('#password').val(),
            checkPassword: $('#checkPassword').val()
        };

        this.cleanChangePasswordErrors();
        var formErrors = this.validateChangePasswordForm(changePasswordForm);
        if (formErrors.success) {
            var self = this;
            profile_model.changePassword(
                changePasswordForm
                , function (/*json*/) {
                    self.changePasswordOk();
                }
                , function (json) {
                    validation_controller.showErrors('changePassword', json);
                });
        }
        else {
            validation_controller.showErrors('changePassword', formErrors);
        }
    },

    validateChangePasswordForm: function (form) {
        var result = {};
        result.fieldErrors = {};
        result.errors = [];
        if (isStringTrimmedEmpty(form.oldPassword)) {
            result.fieldErrors["oldPassword"] = 'validation.passwordEmpty';
        }
        if (isStringTrimmedEmpty(form.password)) {
            result.fieldErrors["password"] = 'validation.passwordEmpty';
        }
        if (isStringTrimmedEmpty(form.checkPassword)) {
            result.fieldErrors["checkPassword"] = 'validation.checkPasswordEmpty';
        }
        if (jQuery.isEmptyObject(result.fieldErrors)) {
            if (form.password != form.checkPassword) {
                result.errors[0] = 'validation.passwordMismatch';
            }
        }

        result.success = jQuery.isEmptyObject(result.fieldErrors) && jQuery.isEmptyObject(result.errors);

        return result;
    },

    cleanChangePasswordErrors: function () {
        $("#changePassword_error").empty().hide();
        $('#changePassword_error_oldPassword').empty();
        $('#changePassword_error_password').empty();
        $('#changePassword_error_checkPassword').empty();
    },

    changePasswordOk: function () {
        $('#oldPassword').val('');
        $('#password').val('');
        $('#checkPassword').val('');
        $("#changePasswordForm").hide();
        $("#changePasswordOk").hide();
        $("#changePasswordSuccessMessage").show();
    },

    changeEmail: function () {
        var changeEmailForm = {
            password: $('#changeEmailPassword').val(),
            email: $('#email').val()
        };

        this.cleanChangeEmailErrors();
        var formErrors = this.validateChangeEmailForm(changeEmailForm);
        if (formErrors.success) {
            var self = this;
            profile_model.changeEmail(
                changeEmailForm
                , function (/*json*/) {
                    self.changeEmailOk();
                }
                , function (json) {
                    validation_controller.showErrors('changeEmail', json);
                });
        }
        else {
            validation_controller.showErrors('changeEmail', formErrors);
        }
    },

    validateChangeEmailForm: function (form) {
        var result = {};
        result.fieldErrors = {};
        result.errors = [];
        if (isStringTrimmedEmpty(form.password)) {
            result.fieldErrors["password"] = 'validation.passwordEmpty';
        }
        if (isStringTrimmedEmpty(form.email)) {
            result.fieldErrors["email"] = 'validation.emailEmpty';
        }
        result.success = jQuery.isEmptyObject(result.fieldErrors) && jQuery.isEmptyObject(result.errors);

        return result;
    },

    cleanChangeEmailErrors: function () {
        $("#changeEmail_error").empty().hide();
        $('#changeEmail_error_password').empty();
        $('#changeEmail_error_email').empty();
    },

    changeEmailOk: function () {
        $('#changeEmailPassword').val('');
        $('#email').val('');
        $("#changeEmailForm").hide();
        $("#changeEmailOk").hide();
        $("#changeEmailSuccessMessage").show();
    }
};

$(document).ready(function () {
    profile_controller.init();
});
