var registration_controller = {

    init: function () {
        country_controller.init();
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $('#regSubmit').click(function () {
            self.register();
            return false;
        });
        $("#dobField").datepicker(
            {
                changeYear: true,
                changeMonth: true,
                yearRange: '-100:-10',
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

    register: function () {
        var termsAndCondAccepted = 'false';
        if ($('#termsAndCondAccepted').prop("checked")) {
            termsAndCondAccepted = 'true';
        }
        var regForm = {
            country: $('#country').val(),
            firstName: $('#firstNameField').val(),
            lastName: $('#lastNameField').val(),
            dob: $('#dobField').val(),
            termsAndCondAccepted: termsAndCondAccepted
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
            result.fieldErrors["country"] = 'validation.emptyCountry';
        }
        if (isStringTrimmedEmpty(regForm.firstName)) {
            result.fieldErrors["firstName"] = 'validation.emptyFirstName';
        }
        if (isStringTrimmedEmpty(regForm.lastName)) {
            result.fieldErrors["lastName"] = 'validation.emptyLastName';
        }
        if (isStringTrimmedEmpty(regForm.dob)) {
            result.fieldErrors["dob"] = 'validation.emptyDob';
        }
        if (regForm.termsAndCondAccepted !== 'true') {
            result.fieldErrors["termsAndCondAccepted"] = 'validation.termsAndCondNotAccepted';
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
        $('#reg_error_termsAndCondAccepted').empty();
    },

    showRegistrationOk: function () {
        $("#dialog").show();
    },

    hideRegistrationOk: function () {
        $('#country').val('').trigger("change");
        $('#firstNameField').val('');
        $('#lastNameField').val('');
        $('#dobField').val('');
        $('#termsAndCondAccepted').prop('checked', false)
        $("#dialog").hide();
        setTimeout(function () {
            window.location = "/";
        }, 2000);
    }
};

$(document).ready(function () {
    registration_controller.init();
});
