var diver_verification_controller = {

    isInit: false,
    validationController: null,
    divers_cards: [],
    cards_controllers: {},

    init: function (country, divers_cards) {
        this.divers_cards = divers_cards;
        for (var i = 0; i < divers_cards.length; i++) {
            var diverIdAndCards = divers_cards[i];
            var diverId = diverIdAndCards.diverId;
            this.cards_controllers[diverId] = simpleClone(cards_controller);
            this.cards_controllers[diverId].init(diverIdAndCards.cardIds);

            $('#' + diverId + '_showAllCertificates').click(function () {
                var diverId = $(this)[0].id.split('_')[0];
                $('#' + diverId + '_cards').show();
            });

            $('#' + diverId + '_cardsOk').click(function () {
                var diverId = $(this)[0].id.split('_')[0];
                $('#' + diverId + '_cards').hide();
            });
            $('#' + diverId + '_cardsClose').click(function () {
                var diverId = $(this)[0].id.split('_')[0];
                $('#' + diverId + '_cards').hide();
            });
        }

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

        adjustCssProperty("padding-left", ".dialog-content", viewPortWidth, 3, 8, 48);
        adjustCssProperty("padding-right", ".dialog-content", viewPortWidth, 3, 8, 48);

        for (var i = 0; i < this.divers_cards.length; i++) {
            var diverIdAndCards = this.divers_cards[i];
            var diverId = diverIdAndCards.diverId;
            var cardsInView = diverIdAndCards.cardIds.length < 3 ? diverIdAndCards.cardIds.length : 3;
            var width = cardsInView * 344;
            width += parseFloat($('.dialog-content').css("padding-left")) * 2; // 2 paddings
            width += viewPortWidth /*$('#' + diverId + '_cardsContent').width()*/ * cardsInView * 0.02; // margin per card
            width += 16; //delta+scrolling
            $('#' + diverId + '_cards').css("width", width + "px");
        }
    }
};
