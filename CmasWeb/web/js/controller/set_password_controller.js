var set_password_controller = {

    validationController: null,

    init: function () {
        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'setPassword';
        this.validationController.fields = [
            {
                id: 'password',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.passwordEmpty';
                    }
                }
            },
            {
                id: 'checkPassword',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.checkPasswordEmpty';
                    }
                }
            }
        ];
        this.validationController.fromValidator = function (form) {
            if (isStringTrimmedEmpty(form.password) || isStringTrimmedEmpty(form.checkPassword)) {
                return null;
            }
            if (form.password != form.checkPassword) {
                return 'validation.passwordMismatch';
            }
            return null;
        };
        this.validationController.submitButton = $('#setPasswordButton');
        this.validationController.form.code = $('#setPassword_code').val();
        this.validationController.init();
        this.setListeners();
        registration_flow_controller.init('setPassword');
    },

    setListeners: function () {
        var self = this;
        $('#setPasswordButton').click(function () {
            self.setPassword();
            return false;
        });
    },

    setPassword: function () {
        if (this.validationController.validateForm()) {
            var form = this.validationController.form;
            window.location = "/setPasswordSubmit.html?code=" + form.code +
                "&password=" + form.password + "&checkPassword=" + form.checkPassword;
        }
    }
};

$(document).ready(function () {
    set_password_controller.init();
});
