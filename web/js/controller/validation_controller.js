var validation_controller = {

    init: function () {

    },

    showErrors: function (prefix, formErrors) {
        if (!formErrors.success) {
            if (formErrors.fieldErrors) {
                for (var fieldError in formErrors.fieldErrors) {
                    if (formErrors.fieldErrors.hasOwnProperty(fieldError)) {
                        $('#'+ prefix + '_error_' + fieldError).html(error_codes[formErrors.fieldErrors[fieldError]]);
                    }
                }
            }
            if (jQuery.isEmptyObject(formErrors.fieldErrors)
                && formErrors.errors && formErrors.errors.length > 0) {
                var message = '';
                for (var error in formErrors.errors) {
                    if (formErrors.errors.hasOwnProperty(error)) {
                        message += error_codes[formErrors.errors[error]];
                    }
                }
                $('#'+ prefix + '_error').html(message).show();
            }
        }
    }
};

$(document).ready(function () {
    validation_controller.init();
});
