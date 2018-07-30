var lost_password_controller = {

    validationController: null,

    init: function () {
        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'changeEmail';
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
                id: 'email',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emailEmpty';
                    }
                }
            }
        ];
        this.validationController.submitButton = $('#changeEmailButton');
        this.validationController.init();
        this.setListeners();
        registration_flow_controller.init('simple',
            {backgroundImageId: "changeEmailImageBackground", visibleInputElemId: "changeEmail_password"}
        );
    },

    setListeners: function () {
        var self = this;
        $('#changeEmailButton').click(function () {
            self.submitForm();
            return false;
        });
        $('#backToMyAccountButton').click(function () {
            window.location = "/secure/profile/getUser.html";
            return false;
        });
    },

    submitForm: function () {
        var self = this;
        if (this.validationController.validateForm()) {
            profile_model.changeEmail(
                this.validationController.form
                , function (/*json*/) {
                    $('#changeEmailBlock').hide();
                    $('#changeEmailSuccessBlock').show();
                    registration_flow_controller.toggleMode('simple',
                        {backgroundImageId: "changeEmailSuccessImageBackground"}
                    );
                }
                , function (json) {
                    self.validationController.showErrors(json);
                }
            );
        }
    }
};
$(document).ready(function () {
    lost_password_controller.init();
});
