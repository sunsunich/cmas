var validation_controller = {

    resetFields: function () {
        this.prefix = "";
        this.fields = [];
        this.submitButton = null;
        this.fieldIdsToValidate = [];
        this.fromValidator = null;

        this.form = {};
    },

    init: function () {
        var self = this;
        for (var i = 0; i < this.fields.length; i++) {
            const field = this.fields[i];
            var fieldElem = $('#' + this.prefix + '_' + field.id);
            if (fieldElem.is("select") || fieldElem.hasClass('hasDatepicker')) {
                fieldElem.on("change", function () {
                    self.validateField(field);
                    self.checkForm();
                });
            } else {
                fieldElem.on("input", function () {
                    self.validateField(field);
                    self.checkForm();
                });
            }
        }
        this.checkForm();
    },

    checkForm: function () {
        if (this.submitButton) {
            if (this.validateForm(true)) {
                this.submitButton.prop('disabled', false);
            } else {
                this.submitButton.prop('disabled', true);
            }
        }
    },

    validateField: function (field, isSilent) {
        if (this.fieldIdsToValidate.length > 0 && this.fieldIdsToValidate.indexOf(field.id) == -1) {
            this.form[field.id] = '';
            return true;
        }
        var fieldElem = $('#' + this.prefix + '_' + field.id);
        var value;
        switch (fieldElem.attr('type')) {
            case 'checkbox' :
                value = 'false';
                if (fieldElem.prop("checked")) {
                    value = 'true';
                }
                break;
            default :
                value = fieldElem.val();
        }
        this.form[field.id] = value;
        var errorCode = field.validateField(value);
        if (errorCode) {
            if (!isSilent) {
                this.showFieldError(field.id, errorCode);
            }
            return false;
        } else {
            if (!isSilent) {
                this.clearFieldError(field.id);
            }
            return true;
        }
    },

    validateForm: function (isSilent) {
        if (!isSilent) {
            $('#' + this.prefix + '_error').empty();
        }
        var result = true;
        for (var i = 0; i < this.fields.length; i++) {
            var field = this.fields[i];
            result = this.validateField(field, isSilent) && result;
        }
        if (this.fromValidator) {
            var errorCode = this.fromValidator(this.form);
            if (errorCode) {
                result = false;
                $('#' + this.prefix + '_error').html(error_codes[errorCode]);
            } else {
                $('#' + this.prefix + '_error').empty();
            }
        }
        return result;
    },

    cleanErrors: function () {
        for (var i = 0; i < this.fields.length; i++) {
            var field = this.fields[i];
            this.clearFieldError(field.id);
        }
        $('#' + this.prefix + '_error').empty();
    },

    clearFieldError: function (fieldId) {
        $('#' + this.prefix + '_error_' + fieldId).empty();
        $('#' + this.prefix + '_error_ico_' + fieldId).hide();
        var fieldElem = $('#' + this.prefix + '_' + fieldId);
        if (fieldElem.is("select")) {
            util_controller.removeErrorSelect2()
        } else {
            fieldElem.css('border-color', '');
            if (fieldElem.hasClass('hasDatepicker')) {
                $('#' + this.prefix + '_ico_' + fieldId).show();
            }
        }
    },

    showFieldError: function (fieldId, errorCode) {
        $('#' + this.prefix + '_error_' + fieldId).html(error_codes[errorCode]);
        $('#' + this.prefix + '_error_ico_' + fieldId).show();
        var fieldElem = $('#' + this.prefix + '_' + fieldId);
        if (fieldElem.is("select")) {
            util_controller.setErrorSelect2()
        } else {
            fieldElem.css('border-color', '#f4225b');
            if (fieldElem.hasClass('hasDatepicker')) {
                $('#' + this.prefix + '_ico_' + fieldId).hide();
            }
        }
    },

    showErrors: function (formErrors) {
        if (!formErrors.success) {
            if (formErrors.fieldErrors) {
                for (var fieldError in formErrors.fieldErrors) {
                    if (formErrors.fieldErrors.hasOwnProperty(fieldError)) {
                        this.showFieldError(fieldError, formErrors.fieldErrors[fieldError]);
                    }
                }
            }
            if (jQuery.isEmptyObject(formErrors.fieldErrors) && formErrors.errors) {
                var message = '';
                var isFirstError = true;
                for (var error in formErrors.errors) {
                    if (formErrors.errors.hasOwnProperty(error)) {
                        if (isFirstError) {
                            isFirstError = false;
                        } else {
                            message += ' ';
                        }
                        message += error_codes[formErrors.errors[error]];
                    }
                }
                $('#' + this.prefix + '_error').html(message);
            }
        }
    },

    simpleShowErrors: function (prefix, formErrors) {
        if (!formErrors.success) {
            if (formErrors.fieldErrors) {
                for (var fieldError in formErrors.fieldErrors) {
                    if (formErrors.fieldErrors.hasOwnProperty(fieldError)) {
                        $('#' + prefix + '_error_' + fieldError).html(error_codes[formErrors.fieldErrors[fieldError]]);
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
                $('#' + prefix + '_error').html(message).show();
            }
        }
    }
};
