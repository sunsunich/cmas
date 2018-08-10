var registration_flow_controller = {

    modeStack: [],
    mode: "registration",
    visibleInputElem: null,
    backgroundImageId: 'regImageBackground',

    init: function (mode, config) {
        this.toggleMode(mode, config);
        this.setListeners();
        $("#Wrapper-content").removeClass("clearfix");
    },

    setListeners: function () {
        var self = this;
        $(window).load(function () {
            self.onResize();
        });
        $(window).resize(function () {
            self.onResize();
        });
        $('#regTabLogin').click(function () {
            self.toggleMode('login');
        });
        $('#regTabRegister').click(function () {
            self.toggleMode('registration');
        });
    },

    toggleMode: function (mode, config) {
        if ($('#' + this.mode + 'Block')[0]) {
            $('#' + this.mode + 'Block').hide();
        }
        this.mode = mode;
        switch (mode) {
            case "login" :
                this.toggleLogin();
                break;
            case "registration" :
                this.toggleRegistration();
                break;
            case "lostPassword" :
                this.toggleLostPassword();
                break;
            case "setPassword" :
                this.toggleSetPassword();
                break;
            case "setPasswordSuccess" :
                this.toggleSetPasswordSuccess();
                break;
            case "chooseDivers" :
                this.toggleChooseDivers(config);
                break;
            case "emailConfirmation":
                this.toggleEmailConfirmation(config);
                break;
            case "simple" :
                this.toggleSimple(config);
                break;
            case "message" :
                this.toggleMessage(config);
                break;
        }
        this.onResize();
    },

    toggleLogin: function () {
        if (!login_controller.isInit) {
            login_controller.init();
        }

        $('#headerLoginButton').hide();
        $('#headerAdvertButton').show();
        $('#headerAdvertText').show();

        $('#regTabLogin').addClass('reg-tab-selected').removeClass('reg-tab');
        $('#regTabRegister').addClass('reg-tab').removeClass('reg-tab-selected');
        $('#loginBlock').show();

        this.visibleInputElem = $('#login_email');
        this.backgroundImageId = 'loginImageBackground';
    },

    toggleRegistration: function () {
        if (!registration_controller.isInit) {
            registration_controller.init();
        }
        $('#headerLoginButton').show();
        $('#headerAdvertButton').hide();
        $('#headerAdvertText').hide();

        $('#regTabRegister').addClass('reg-tab-selected').removeClass('reg-tab');
        $('#regTabLogin').addClass('reg-tab').removeClass('reg-tab-selected');
        if (this.modeStack.length > 0) {
            this.toggleMode(this.modeStack[this.modeStack.length - 1]);
            return;
        }
        registration_model.reset();
        $('#registrationBlock').show();

        this.visibleInputElem = $('#reg_firstName');
        this.backgroundImageId = 'regImageBackground';
    },

    toggleLostPassword: function () {
        if (!lost_password_controller.isInit) {
            lost_password_controller.init();
        }

        this.visibleInputElem = $('#lostPassword_email');
        this.backgroundImageId = 'lostPasswordImageBackground';
    },

    toggleSetPassword: function () {
        $('#headerLoginButton').show();
        $('#headerAdvertButton').hide();
        $('#headerAdvertText').hide();

        this.visibleInputElem = $('#setPassword_password');
        this.backgroundImageId = 'setPasswordImageBackground';
    },

    toggleSetPasswordSuccess: function () {
        $('#setPasswordBlock').hide();
        $('#setPasswordSuccessBlock').show();

        this.visibleInputElem = null;
        this.backgroundImageId = 'setPasswordSuccessImageBackground';
    },

    toggleSimple: function (config) {
        if (config.visibleInputElemId) {
            this.visibleInputElem = $('#' + config.visibleInputElemId);
        } else {
            this.visibleInputElem = null;
        }
        this.backgroundImageId = config.backgroundImageId;
    },

    toggleChooseDivers: function (config) {
        $('#chooseDiversBlock').show();
        if (config && config.saveRegState) {
            this.modeStack.push('chooseDivers');
        }
        this.visibleInputElem = null;
    },

    toggleEmailConfirmation: function (config) {
        $('#emailConfirmationBlock').show();
        if (config && config.saveRegState) {
            this.modeStack.push('emailConfirmation');
        }
        this.visibleInputElem = null;
    },

    toggleMessage: function (config) {
        $('#messageBlock').show();
        if (config) {
            if (config.header) {
                $('#messageHeader').html(config.header).show();
            } else {
                $('#messageHeader').empty().hide();
            }
            if (config.text) {
                $('#messageText').html(config.text).show();
            } else {
                $('#messageText').empty().hide();
            }
            var messageButtonElem = $('#messageButton');
            if (config.button) {
                if (config.button.onlick) {
                    messageButtonElem.click(function () {
                        config.button.onlick();
                        return false;
                    });
                } else {
                    var self = this;
                    messageButtonElem.click(function () {
                        if (config.saveRegState) {
                            self.modeStack.pop();
                        }
                        self.toggleMode('registration');
                        return false;
                    });
                }
                if (config.button.cssClass) {
                    messageButtonElem.removeClass('form-button-smaller').addClass(config.button.cssClass);
                } else {
                    messageButtonElem.removeAttr('class')
                        .addClass('positive-button')
                        .addClass('form-item-right')
                        .addClass('form-button-smaller');
                }
                messageButtonElem.html(config.button.text).show();
            } else {
                messageButtonElem.empty().hide();
            }
            if (config.saveRegState) {
                this.modeStack.push('message');
            }
        }
        this.visibleInputElem = null;
    },

    onResize: function () {
        var viewPortWidth = $(window).width();
        if (viewPortWidth > 580 &&
            this.mode != 'registration' &&
            this.mode != 'chooseDivers' &&
            this.mode != 'emailConfirmation' &&
            this.mode != 'setPassword' &&
            this.mode != 'setPasswordSuccess' &&
            this.mode != 'message') {
            $('#headerAdvertText').show();
        } else {
            $('#headerAdvertText').hide();
        }

        var isDisplayImage = viewPortWidth > 630;
        var relativeViewPortWidth = isDisplayImage ? viewPortWidth / 2 : viewPortWidth;
        adjustCssProperty("padding-top", "#Wrapper-content", viewPortWidth, 3, 16, 40);
        adjustCssProperty("padding-bottom", "#Wrapper-content", viewPortWidth, 3, 16, 40);
        adjustCssProperty("padding-left", ".formWrapper", relativeViewPortWidth, 1.9, 16, 56);
        adjustCssProperty("padding-right", ".formWrapper", relativeViewPortWidth, 1.9, 16, 56);

        adjustCssProperty("width", ".form-row input, .select2-container--bootstrap", relativeViewPortWidth, 0.15, 257, 433);

        var hasVisibleInputElem = this.visibleInputElem != null;
        var fullInputWidth;
        if (hasVisibleInputElem) {
            var inputWidth = this.visibleInputElem.width();
            $('.error-input-ico, .date-form-calendar-input-ico').css('left', inputWidth - 16);
            fullInputWidth = inputWidth + 2 /*border*/
                + parseFloat(this.visibleInputElem.css('padding-left'))
                + parseFloat(this.visibleInputElem.css('padding-right'));
        } else {
            fullInputWidth = Math.max(
                Math.min(relativeViewPortWidth / (10 * /*0.2*/0.13), 450), 256);
            var formHeaderElem = $('#formHeader');
            if (formHeaderElem[0]) {
                formHeaderElem.css('width', fullInputWidth);
            }
        }
        adjustCssProperty("padding-left", ".diver-to-choose-list-item", relativeViewPortWidth, 3, 8, 16);
        adjustCssProperty("padding-right", ".diver-to-choose-list-item", relativeViewPortWidth, 3, 8, 32);

        $('.form-checkbox-label').css('width', fullInputWidth - 28 /*label padding left*/);
        $('.form-feature-description').css('width', fullInputWidth - 28 /*label padding left*/);

        $('.form-description').css('width', fullInputWidth);
        $('.purchased-feature-list').css('width', fullInputWidth);

        adjustCssProperty("padding-left", ".form-button-single", relativeViewPortWidth, 0.5, 16, 80);
        adjustCssProperty("padding-right", ".form-button-single", relativeViewPortWidth, 0.5, 16, 80);
        adjustCssProperty("padding-left", ".form-button-smaller", relativeViewPortWidth, 1, 8, 50);
        adjustCssProperty("padding-right", ".form-button-smaller", relativeViewPortWidth, 1, 8, 50);
        adjustCssProperty("padding-left", ".form-button-bigger", relativeViewPortWidth, 2, 8, 50);
        adjustCssProperty("padding-right", ".form-button-bigger", relativeViewPortWidth, 2, 8, 50);

        var formWrapperElem = $('#formWrapper');
        var formWidth = fullInputWidth
            + parseFloat(formWrapperElem.css('padding-left'))
            + parseFloat(formWrapperElem.css('padding-right'));

        var wrapperContentElem = $("#Wrapper-content");
        var wrapperContentPaddings = (viewPortWidth - (isDisplayImage ? 2 : 1) * formWidth) / 2 - 1;
        wrapperContentElem.css('padding-left', Math.max(8, wrapperContentPaddings))
            .css('padding-right', Math.max(8, wrapperContentPaddings));
        if (isDisplayImage) {
            wrapperContentElem.css("backgroundImage", '').removeClass("formBackgroundContainer");
            replaceBackground('formImage', this.backgroundImageId);
            var formImageHeight = $('#' + this.backgroundImageId).height();
            var formImageWidth = $('#' + this.backgroundImageId).width();
            var imagePaddings = 90 - parseFloat(wrapperContentElem.css('padding-top'));
            $('#formImage').css('width', formImageWidth).css('height', formImageHeight)
                .addClass("formBackgroundContainer")
                .css('margin-top', imagePaddings).css('margin-bottom', imagePaddings).show();
            adjustCssProperty("padding-left", "#formImage", relativeViewPortWidth, 2, 16, 56);
            adjustCssProperty("width", ".advert-list", relativeViewPortWidth, 0.18, 270, 350);
            adjustCssProperty("width", ".advert-header", relativeViewPortWidth, 0.18, 270, 350);
            formWrapperElem.addClass('form-absolute');
            var wrapperContentPaddingLeft = parseFloat(wrapperContentElem.css('padding-left'));
            adjustCssProperty("left", "#formWrapper", relativeViewPortWidth,
                0.1,
                formImageWidth * 60 / 100 + wrapperContentPaddingLeft,
                formImageWidth * 89 / 100 + wrapperContentPaddingLeft
            );
            if (formWrapperElem.height() +
                parseFloat(formWrapperElem.css('padding-top')) +
                parseFloat(formWrapperElem.css('padding-bottom')) < formImageHeight) {
                formWrapperElem.css("top", 40 + 90 + $('#header').height());
            } else {
                formWrapperElem.css("top", '');
            }
        } else {
            $('#formImage').hide();
            formWrapperElem.removeClass('form-absolute');
            wrapperContentElem.addClass("formBackgroundContainer");
            replaceBackground('Wrapper-content', this.backgroundImageId);
        }
    }
};
