var profile_management_controller = {

    init: function () {
        set_password_controller.init({
            hasOldPassword: false,
            setPasswordAction: function (form) {
                recovery_model.changePassword(
                    form
                    , function (/*json*/) {
                        registration_flow_controller.toggleMode('setPasswordSuccess');
                    }
                    , function (json) {
                        set_password_controller.validationController.showErrors(json);
                    }
                );
            }
        });
    }
};
$(document).ready(function () {
    profile_management_controller.init();
});
