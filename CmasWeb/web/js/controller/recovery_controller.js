var recovery_controller = {

    isInit: false,
    validationController: null,

    init: function () {
        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'changePassword';
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
        this.validationController.submitButton = $('#changePasswordButton');
        this.validationController.form.code = $('#changePassword_code').val();
        this.validationController.init();
        this.setListeners();
        this.isInit = true;
    },

    setListeners: function () {
        var self = this;
        $('#changePasswordButton').click(function () {
            self.changePassword();
            return false;
        });
        $('#backToLoginButton').click(function () {
            window.location.href = '/login-form.html';
            return false;
        });
    },

    changePassword: function () {
        var self = this;
        if (this.validationController.validateForm()) {
            recovery_model.changePassword(
                self.validationController.form
                , function (/*json*/) {
                    registration_flow_controller.toggleMode('changePasswordSuccess');
                }
                , function (json) {
                    self.validationController.showErrors(json);
                }
            );
        }
    }
};
