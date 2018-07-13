var login_controller = {

    isInit: false,
    validationController: null,

    init: function () {
        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'login';
        this.validationController.fields = [
            {
                id: 'email',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emailEmpty';
                    }
                }
            },
            {
                id: 'password',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.passwordEmpty';
                    }
                }
            }
        ];
        //this.validationController.submitButton = $('#loginSubmit');
        this.validationController.init();
        this.setListeners();
        this.isInit = true;
    },

    setListeners: function () {
        var self = this;
        $('#loginSubmit').click(function () {
            self.sendLogin();
            return false;
        });
        $('#login_password').keydown(function (e) {
            if (e.which == 13) {
                self.sendLogin();
                return false;
            }
            return true;
        });
        $('#forgotPassword').click(function () {
            window.location.href = '/lostPasswordForm.html';
            return false;
        });
    },

    sendLogin: function () {
        if (this.validationController.validateForm()) {
            var rememberMe = '';
            if ($('#rememberLogin').prop("checked")) {
                rememberMe = 'on';
            }
            var self = this;
            login_model.login(
                self.validationController.form.email, self.validationController.form.password, rememberMe
                , function (json) {
                    window.location.href = json.redirectUrl;
                }
                , function (/*json*/) {
                    self.validationController.showErrors({errors: {error: "validation.badCredentials"}});
                }
            );
        }
    }
};
