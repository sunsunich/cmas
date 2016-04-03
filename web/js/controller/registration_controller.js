var registration_controller = {

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $('#regSubmit').click(function () {
            self.register();
            return false;
        });
        $("#oplCountries").select2({
            placeholder: '<img class="country-input-ico">' + labels["cmas.face.registration.form.label.country"],
            escapeMarkup: function (m) {
                return m;
            },
            templateSelection: self.formatCountry
        });
        $("#dobField").datepicker(
            {
                changeYear: true,
                changeMonth: true,
                yearRange: '-70:-10',
                defaultDate: "-20y",
                dateFormat: 'dd/mm/yy'
            }
        );
        $("#dialog").dialog({
            draggable: false,
            resizable: false,
            autoOpen: false,

            title: labels["cmas.face.registration.success.title"],
            close: function (event, ui) {
                $('#oplCountries').select2("val", "");
                $('#firstNameField').val('');
                $('#lastNameField').val('');
                $('#dobField').val('');
            },

            buttons: [
                {
                    text: "OK",
                    click: function () {
                        $(this).dialog("close");
                    }
                }
            ]
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
        $('#dialogContent').html(labels["cmas.face.registration.success.text"]);
        $("#dialog").dialog("open");
    }
};

$(document).ready(function () {
    registration_controller.init();
});
