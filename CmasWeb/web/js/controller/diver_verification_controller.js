var diver_verification_controller = {

    isInit: false,
    validationController: null,

    init: function (country) {
        //todo move to more general controller
        $('#Wrapper-content').addClass("clearfix");
        $('.convert-with-labels').each(function () {
            var value = $(this).html();
            $(this).html(labels[value]);
        });

        country_controller.inputId = "diverVerification_country";
        country_controller.defaultValue = country;
        country_controller.init();
        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'diverVerification';
        this.validationController.fields = [
            {
                id: 'name',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyName';
                    }
                }
            },
            {
                id: 'country',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyCountry';
                    }
                }
            }
        ];
        this.validationController.submitButton = $('#diverVerificationSubmit');
        this.validationController.init();
        this.setListeners();
        this.isInit = true;
    },

    renderRecaptcha: function (reCaptchaPublicKey) {
        if (!cookie_controller.isCookieExists('CAPTCHA_COOKIE')) {
            var viewPortWidth = $(window).width();
            var reCaptchaSize;
            if (viewPortWidth > 400) {
                reCaptchaSize = 'normal';
            } else {
                reCaptchaSize = 'compact';
            }
            grecaptcha.render(
                'diverVerification_captcha',
                {
                    'sitekey': reCaptchaPublicKey,
                    'size': reCaptchaSize
                }
            );
        }
    },

    setListeners: function () {
        var self = this;
        $('#diverVerificationSubmit').click(function () {
            self.submitForm();
            return false;
        });
        $(window).load(function () {
            self.onResize();
        });
        $(window).resize(function () {
            self.onResize();
        });
    },

    submitForm: function () {
        if (this.validationController.validateForm()) {
            $('#diverVerificationForm').submit();
        }
    },

    onResize: function () {
        var viewPortWidth = $(window).width();
        if (viewPortWidth > 490) {
            $('.form-elem-large').css('width', '').css('margin-right', '');
        } else {
            $('.form-elem-large').css('width', '100%').css('margin-right', '0');
        }
    }
};
