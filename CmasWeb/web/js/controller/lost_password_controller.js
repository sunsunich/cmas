var lost_password_controller = {

    isInit: false,
    validationController: null,

    init: function () {
        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'lostPassword';
        this.validationController.fields = [
            {
                id: 'email',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emailEmpty';
                    }
                }
            }
        ];
        this.validationController.submitButton = $('#lostPasswordSubmit');
        this.validationController.init();
        this.setListeners();
        this.isInit = true;
    },

    renderRecaptcha: function (reCaptchaPublicKey) {
        // if (!cookie_controller.isCookieExists('CAPTCHA_COOKIE')) {
            var viewPortWidth = $(window).width();
            var reCaptchaSize;
            if (viewPortWidth > 904) {
                reCaptchaSize = 'normal';
            } else {
                reCaptchaSize = 'compact';
            }
            grecaptcha.render(
                'lostPassword_captcha',
                {
                    'sitekey': reCaptchaPublicKey,
                    'size': reCaptchaSize
                }
            );
        // }
    },

    setListeners: function () {
        var self = this;
        $('#lostPasswordSubmit').click(function () {
            self.submitForm();
            return false;
        });
        $('#lostPassword_email').keydown(function (e) {
            if (e.which == 13) {
                self.submitForm();
                return false;
            }
            return true;
        });
        $('#backToLogin').click(function () {
            window.location.href = '/login-form.html';
            return false;
        });
    },

    submitForm: function () {
        if (this.validationController.validateForm()) {
            $('#lostPasswordForm').submit();
        }
    }
};
