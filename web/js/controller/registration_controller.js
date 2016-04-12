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
                    validation_controller.showErrors('reg', json);
                });
        }
        else {
            validation_controller.showErrors('reg', formErrors);
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

    cleanErrors: function () {
        $("#reg_error").empty().hide();
        $('#reg_error_country').empty();
        $('#reg_error_firstName').empty();
        $('#reg_error_lastName').empty();
        $('#reg_error_dob').empty();
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
    registration_controller.init();
});
