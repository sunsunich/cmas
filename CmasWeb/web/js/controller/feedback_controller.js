var feedback_controller = {

    validationController: null,

    init: function () {
        registration_flow_controller.init('simple',
            {
                backgroundImageId: 'feedbackImageBackground'
                , visibleInputElemId: "feedback_text"
            });
        multiple_fileUpload_controller.addImageElem = $('#addImage');
        multiple_fileUpload_controller.photoListContainer = $('#photoListContainer');
        multiple_fileUpload_controller.errorElem = $('#feedback_error_photo');
        multiple_fileUpload_controller.addImageCallback = function () {
            registration_flow_controller.onResize();
        };
        multiple_fileUpload_controller.init();

        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'feedback';
        this.validationController.fields = [
            {
                id: 'text',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emailEmpty';
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
            if (viewPortWidth > 904) {
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
        $('#feedback_text').keydown(function (e) {
            if (e.which == 13) {
                self.submitForm();
                return false;
            }
            return true;
        });
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
