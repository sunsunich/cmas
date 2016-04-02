var registration_controller = {

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $('#regForm').click(function () {
            self.register(false);
            return false;
        });
        $("#oplCountries").select2({
            placeholder: "Country"
        });
    },

    register: function (isSkipFederationCheck) {
        var regForm = {
            email: $('#txbEmail').val(),
            password: $('#txbRegPassword').val(),
            passwordRepeat: $('#txbRegPasswordRepeat').val(),
            country: $('#oplCountries').val(),
            firstName: $('#txbFirstName').val(),
            lastName: $('#txbLastName').val(),
            role: $('#oplRoles').val(),
            isSkipFederationCheck: isSkipFederationCheck
        };

        this.cleanErrors();
        var formErrors = this.validateRegForm(regForm);
        if (formErrors.success) {
            var self = this;
            registration_model.register(
                regForm
                , function (/*json*/) {
                    self.ShowRegistrationOk(regForm.email);
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
                        message += error_codes[formErrors.errors[error]] + '\n';
                    }
                }
                message += "Please contact your sports federation\n";
                message += "You can get approval from your sports federation later from your personal CMAS page\n";
                message += "Register without sports federation approval?";
                var doRegistration = confirm(message);
                if (doRegistration) {
                    this.register(true);
                }
            }
        }
    },

    cleanErrors: function () {
        $('#error_email').empty();
        $('#error_password').empty();
        $('#error_passwordRepeat').empty();
        $('#error_country').empty();
        $('#error_role').empty();
    },

    ShowRegistrationOk: function (argEmail) {
        var objRP = document.getElementById('registrationPanel');
        var objROP = document.getElementById('registrationOkPanel');
        var objFPP = document.getElementById('forgetPasswordPanel');
        var objLP = document.getElementById('loginPanel');

        SetFieldsToDisabled(false);

        if (objRP !== null) objRP.style.display = 'none';
        if (objROP !== null) objROP.style.display = 'block';
        if (objFPP !== null) objFPP.style.display = 'none';
        if (objLP !== null) objLP.style.display = 'block';

        document.f.txbLogin.value = argEmail;
        document.f.txbPassword.focus();
    }
};

$(document).ready(function () {
    registration_controller.init();
});
