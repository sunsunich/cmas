var profile_controller = {

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $('#userpicSelectButton').click(function () {
            self.resetUserPicChooser();
            $('#selectUserpic').show();
        });
        $("#selectUserpicClose").click(function () {
            $('#selectUserpic').hide();
        });
        $("#fileFromDiscSelect").click(function () {
            self.resetUserPicChooser();
            self.selectUserpicFromDrive();
        });
        $("#cameraSelect").click(function () {
            self.resetUserPicChooser();
            self.selectUserpicCamera();
        });
        $("#userpicFileInput").change(function () {
            self.readURL(this);
        });
        $("#selectUserpicOk").click(function () {
            self.uploadFile();
        });

        $('#changePasswordButton').click(function () {
            $('#changePasswordSuccessMessage').hide();
            $("#changePasswordForm").show();
            $("#changePasswordOk").show();
            $('#changePassword').show();
        });
        $("#changePasswordOk").click(function () {
            self.changePassword();
        });
        $("#changePasswordClose").click(function () {
            $('#changePassword').hide();
        });
        $("#changePasswordFinishedOk").click(function () {
            $('#changePassword').hide();
        });

        $('#changeEmailButton').click(function () {
            $('#changeEmailSuccessMessage').hide();
            $("#changeEmailForm").show();
            $("#changeEmailOk").show();
            $('#changeEmail').show();
        });
        $("#changeEmailOk").click(function () {
            self.changeEmail();
        });
        $("#changeEmailClose").click(function () {
            $('#changeEmail').hide();
        });
        $("#changeEmailFinishedOk").click(function () {
            $('#changeEmail').hide();
        });

        $("#cardReload").click(function () {
            self.loadPrimaryCard();
        });
        self.loadPrimaryCard();
        self.loadUserpic();
    },

    loadPrimaryCard: function () {
        profile_model.loadCard(
            cmas_primaryCardId
            , function (json) {
                $('#noCard').hide();
                $('#cardImg').attr("src", "data:image/png;base64," + json.base64);
                $('#card').show();
            }
            , function () {
                $('#noCard').show();
                $('#card').hide();
            });
    },

    loadUserpic: function () {
        profile_model.loadUserpic(
            function (json) {
                $('#userpic').attr("src", "data:image/png;base64," + json.base64);
            }
            , function () {
                $('#userpic').attr("src", '/i/no_img.png');
            });
    },

    resetUserPicChooser: function () {
        $('#userpicPreview').attr('src', $('#userpic').attr("src")).show();
        $('#userpicFileInput').hide();
    },

    selectUserpicFromDrive: function () {
        $('#userpicFileInput').trigger('click');
    },

    readURL: function (input) {
        try {
            if (!this.validateFile(input)) {
                return;
            }
            var file = input.files[0];
            var reader = new FileReader();
            reader.onload = function (e) {
                $('#userpicPreview').attr('src', e.target.result);
            };
            reader.readAsDataURL(file);
        }
        catch (err) {
            $('#userpicPreview').hide();
            $('#userpicFileInput').show();
        }
    },

    validateFile: function (input) {
        $('#selectUserpic_error_file').html('');
        if (!(input.files && input.files[0])) {
            $('#selectUserpic_error_file').html(error_codes["validation.emptyField"]);
            return false;
        }
        var file = input.files[0];
        if (file.name.length < 1) {
            $('#selectUserpic_error_file').html(error_codes["validation.emptyField"]);
            return false;
        }
        else if (file.size > 100000) {
            $('#selectUserpic_error_file').html(error_codes["validation.imageSize"]);
            return false;
        }
        else if (file.type != 'image/png' && file.type != 'image/jpg' && file.type != 'image/gif' && file.type != 'image/jpeg') {
            $('#selectUserpic_error_file').html(error_codes["validation.imageFormat"]);
            return false;
        }
        return true;
    },

    uploadFile: function () {
        var input = $("#userpicFileInput")[0];
        if (!this.validateFile(input)) {
            return;
        }
        var file = input.files[0];
        var formData = new FormData();
        formData.append('file', file);
        var self = this;
        profile_model.changeUserpic(formData,
            function (/*json*/) {
                self.loadUserpic();
                $('#selectUserpic').hide();
            },
            function (json) {
                $('#selectUserpic_error_file').html(error_codes[json.message]);
            }
        );
    },

    selectUserpicCamera: function () {

    },

    changePassword: function () {
        var changePasswordForm = {
            oldPassword: $('#oldPassword').val(),
            password: $('#password').val(),
            checkPassword: $('#checkPassword').val()
        };

        this.cleanChangePasswordErrors();
        var formErrors = this.validateChangePasswordForm(changePasswordForm);
        if (formErrors.success) {
            var self = this;
            profile_model.changePassword(
                changePasswordForm
                , function (/*json*/) {
                    self.changePasswordOk();
                }
                , function (json) {
                    validation_controller.showErrors('changePassword', json);
                });
        }
        else {
            validation_controller.showErrors('changePassword', formErrors);
        }
    },

    validateChangePasswordForm: function (form) {
        var result = {};
        result.fieldErrors = {};
        result.errors = [];
        if (isStringTrimmedEmpty(form.oldPassword)) {
            result.fieldErrors["oldPassword"] = 'validation.passwordEmpty';
        }
        if (isStringTrimmedEmpty(form.password)) {
            result.fieldErrors["password"] = 'validation.passwordEmpty';
        }
        if (isStringTrimmedEmpty(form.checkPassword)) {
            result.fieldErrors["checkPassword"] = 'validation.checkPasswordEmpty';
        }
        if (jQuery.isEmptyObject(result.fieldErrors)) {
            if (form.password != form.checkPassword) {
                result.errors[0] = 'validation.passwordMismatch';
            }
        }

        result.success = jQuery.isEmptyObject(result.fieldErrors) && jQuery.isEmptyObject(result.errors);

        return result;
    },

    cleanChangePasswordErrors: function () {
        $("#changePassword_error").empty().hide();
        $('#changePassword_error_oldPassword').empty();
        $('#changePassword_error_password').empty();
        $('#changePassword_error_checkPassword').empty();
    },

    changePasswordOk: function () {
        $('#oldPassword').val('');
        $('#password').val('');
        $('#checkPassword').val('');
        $("#changePasswordForm").hide();
        $("#changePasswordOk").hide();
        $("#changePasswordSuccessMessage").show();
    },

    changeEmail: function () {
        var changeEmailForm = {
            password: $('#changeEmailPassword').val(),
            email: $('#email').val()
        };

        this.cleanChangeEmailErrors();
        var formErrors = this.validateChangeEmailForm(changeEmailForm);
        if (formErrors.success) {
            var self = this;
            profile_model.changeEmail(
                changeEmailForm
                , function (/*json*/) {
                    self.changeEmailOk();
                }
                , function (json) {
                    validation_controller.showErrors('changeEmail', json);
                });
        }
        else {
            validation_controller.showErrors('changeEmail', formErrors);
        }
    },

    validateChangeEmailForm: function (form) {
        var result = {};
        result.fieldErrors = {};
        result.errors = [];
        if (isStringTrimmedEmpty(form.password)) {
            result.fieldErrors["password"] = 'validation.passwordEmpty';
        }
        if (isStringTrimmedEmpty(form.email)) {
            result.fieldErrors["email"] = 'validation.emailEmpty';
        }
        result.success = jQuery.isEmptyObject(result.fieldErrors) && jQuery.isEmptyObject(result.errors);

        return result;
    },

    cleanChangeEmailErrors: function () {
        $("#changeEmail_error").empty().hide();
        $('#changeEmail_error_password').empty();
        $('#changeEmail_error_email').empty();
    },

    changeEmailOk: function () {
        $('#changeEmailPassword').val('');
        $('#email').val('');
        $("#changeEmailForm").hide();
        $("#changeEmailOk").hide();
        $("#changeEmailSuccessMessage").show();
    }
};

$(document).ready(function () {
    profile_controller.init();
});
