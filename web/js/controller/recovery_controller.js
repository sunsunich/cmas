var recovery_controller = {

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $('#changePasswordButton').click(function () {
            self.changePassword();
            return false;
        });
        $("#changePasswordOk").click(function () {
            window.location = "/";
            return false;
        });
        $("#changePasswordClose").click(function () {
            window.location = "/";
            return false;
        });
    },

    changePassword: function () {
        var changePasswordForm = {
            password: $('#password').val(),
            checkPassword: $('#checkPassword').val(),
            code : $('#code').val()
        };
        this.cleanChangePasswordErrors();
        var formErrors = this.validateChangePasswordForm(changePasswordForm);
        if (formErrors.success) {
            var self = this;
            recovery_model.changePassword(
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
        $('#error_password').empty();
        $('#error_checkPassword').empty();
    },

    changePasswordOk: function () {
        $('#password').val('');
        $('#checkPassword').val('');
        $("#changePassword").show();
    }
};

$(document).ready(function () {
    recovery_controller.init();
});
