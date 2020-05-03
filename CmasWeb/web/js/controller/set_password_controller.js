var set_password_controller = {

    validationController: null,
    setPasswordAction: null,

    init: function (config) {
        this.setPasswordAction = config.setPasswordAction;
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
        util_controller.tweakPasswordInput('setPassword', 'password');
        util_controller.tweakPasswordInput('setPassword', 'checkPassword');
        if (config.hasOldPassword) {
            this.validationController.fields.push(
                {
                    id: 'oldPassword',
                    validateField: function (value) {
                        if (isStringTrimmedEmpty(value)) {
                            return 'validation.passwordEmpty';
                        }
                    }
                }
            );
            util_controller.tweakPasswordInput('setPassword', 'oldPassword');
        } else {
            this.validationController.form.code = $('#setPassword_code').val();
        }
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
        $('#backToLoginButton').click(function () {
            window.location.href = '/login-form.html';
            return false;
        });
    },

    setPassword: function () {
        if (this.validationController.validateForm()) {
            this.setPasswordAction(this.validationController.form);
        }
    }
};
