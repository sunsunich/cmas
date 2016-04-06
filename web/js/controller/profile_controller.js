var profile_controller = {

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $('#regSubmit').click(function () {
            self.register();
            return false;
        });
        $("#dialogClose").click(function () {
            self.hideRegistrationOk();
        });
        $("#dialogOk").click(function () {
            self.hideRegistrationOk();
        });
    },

    formatCountry: function (state) {
        if (!state.id) {
            return state.text;
        }
        return $(
            '<span><img class="country-input-ico">' + state.text + '</span>'
        );
    },

    register: function () {
        var regForm = {
            country: $('#oplCountries').val(),
            firstName: $('#firstNameField').val(),
            lastName: $('#lastNameField').val(),
            dob: $('#dobField').val()
        };

        this.cleanErrors();
        var formErrors = this.validateRegForm(regForm);
        if (formErrors.success) {
            var self = this;
            registration_model.register(
                regForm
                , function (/*json*/) {
                    self.showRegistrationOk(regForm.email);
                }
                , function (json) {
                    self.showErrors(json);
                });
        }
        else {
            this.showErrors(formErrors);
        }
    },

    validateRegForm: function (regForm) {
        var result = {};
        result.fieldErrors = {};
        result.errors = {};
        if (isStringTrimmedEmpty(regForm.country)) {
            result.fieldErrors["country"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(regForm.firstName)) {
            result.fieldErrors["firstName"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(regForm.lastName)) {
            result.fieldErrors["lastName"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(regForm.dob)) {
            result.fieldErrors["dob"] = 'validation.emptyField';
        }

        result.success = jQuery.isEmptyObject(result.fieldErrors) && jQuery.isEmptyObject(result.errors);

        return result;
    },

    showErrors: function (formErrors) {
        if (!formErrors.success) {
            if (formErrors.fieldErrors) {
                for (var fieldError in formErrors.fieldErrors) {
                    if (formErrors.fieldErrors.hasOwnProperty(fieldError)) {
                        $('#error_' + fieldError).html(error_codes[formErrors.fieldErrors[fieldError]]);
                    }
                }
            }
            if ((formErrors.fieldErrors == null
                || formErrors.fieldErrors.length == null
                || formErrors.fieldErrors.length == 0)
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

    cleanErrors: function () {
        $("#error").empty().hide();
        $('#error_country').empty();
        $('#error_firstName').empty();
        $('#error_lastName').empty();
        $('#error_dob').empty();
    },

    showRegistrationOk: function () {
        $("#dialog").show();
    },

    hideRegistrationOk: function () {
        $('#oplCountries').select2("val", "");
        $('#firstNameField').val('');
        $('#lastNameField').val('');
        $('#dobField').val('');
        $("#dialog").hide();
        setTimeout(function () {
            window.location = "/";
        }, 2000);
    }
};

$(document).ready(function () {
    profile_controller.init();
});
