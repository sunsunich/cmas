var feedback_controller = {

    validationController: null,

    init: function () {
        multiple_fileUpload_controller.addImageElem = $('#addImage');
        multiple_fileUpload_controller.photoListContainer = $('#photoListContainer');
        multiple_fileUpload_controller.errorElem = $('#feedback_error_photo');
        multiple_fileUpload_controller.init();

        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'feedback';
        this.validationController.fields = [
            {
                id: 'text',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyField';
                    }
                    if (value.length > 2048) {
                        return 'validation.maxLength';
                    }
                }
            }
        ];
        this.validationController.submitButton = $('#feedbackSubmit');
        this.validationController.init();
        this.setListeners();
    },

    renderRecaptcha: function (reCaptchaPublicKey) {
        if (!cookie_controller.isCookieExists('CAPTCHA_COOKIE')) {
            var viewPortWidth = $(window).width();
            var reCaptchaSize;
            if (viewPortWidth > 450) {
                reCaptchaSize = 'normal';
            } else {
                reCaptchaSize = 'compact';
            }
            grecaptcha.render(
                'feedback_captcha',
                {
                    'sitekey': reCaptchaPublicKey,
                    'size': reCaptchaSize
                }
            );
        }
    },

    setListeners: function () {
        var self = this;
        $('#feedbackSubmit').click(function () {
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

    onResize: function () {
        var relativeViewPortWidth = $(window).width()

        adjustCssProperty("padding-left", ".panel", relativeViewPortWidth, 1.9, 16, 56);
        adjustCssProperty("padding-right", ".panel", relativeViewPortWidth, 1.9, 16, 56);

        adjustCssProperty("width", ".form-row input, .form-row textarea, .select2-container--bootstrap", relativeViewPortWidth, 0.15, 257, 866);

    },

    submitForm: function () {
        if (this.validationController.validateForm()) {
            $('#feedbackForm').submit();
        }
    }
};
$(document).ready(function () {
    feedback_controller.init();
});
