var profile_controller = {

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
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
        $("#cardReload").click(function () {
            self.loadPrimaryCard();
        });

        self.loadPrimaryCard();
    },

    loadPrimaryCard: function () {
        var self = this;
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
                    self.showChangePasswordErrors(json);
                });
        }
        else {
            this.showChangePasswordErrors(formErrors);
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

    showChangePasswordErrors: function (formErrors) {
        if (!formErrors.success) {
            if (formErrors.fieldErrors) {
                for (var fieldError in formErrors.fieldErrors) {
                    if (formErrors.fieldErrors.hasOwnProperty(fieldError)) {
                        $('#error_' + fieldError).html(error_codes[formErrors.fieldErrors[fieldError]]);
                    }
                }
            }
            if (jQuery.isEmptyObject(formErrors.fieldErrors)
                && formErrors.errors && formErrors.errors.length > 0) {
                var message = '';
                for (var error in formErrors.errors) {
                    if (formErrors.errors.hasOwnProperty(error)) {
                        message += error_codes[formErrors.errors[error]];
                    }
                }
                $('#error').html(message).show();
            }
        }
    },


    cleanChangePasswordErrors: function () {
        $("#error").empty().hide();
        $('#error_oldPassword').empty();
        $('#error_password').empty();
        $('#error_checkPassword').empty();
    },

    changePasswordOk: function () {
        $('#oldPassword').val('');
        $('#password').val('');
        $('#checkPassword').val('');
        $("#changePasswordForm").hide();
        $("#changePasswordOk").hide();
        $("#changePasswordSuccessMessage").show();
    }
};

$(document).ready(function () {
    profile_controller.init();
});
