var userpic_controller = {

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $('#userpicSelectButton').click(function () {
            self.selectUserpicFromDrive()
        });
        $("#userpicFileInput").change(function () {
            self.validateAndUploadFile(this);
        });
    },

    userpicUpdated: function () {
        window.location.reload();
    },

    selectUserpicFromDrive: function () {
        $('#userpicFileInput').trigger('click');
    },

    validateAndUploadFile: function (input) {
        if (this.validateFile(input)) {
            this.uploadFile();
        }
    },

    validateFile: function (input) {
        $('#selectUserpic_error_file').html('');
        if (!(input.files && input.files[0])) {
            $('#selectUserpic_error_file').html(error_codes["validation.noFile"]);
            return false;
        }
        var file = input.files[0];
        if (file.name.length < 1) {
            $('#selectUserpic_error_file').html(error_codes["validation.noFile"]);
            return false;
        }
        else if (file.size > 10 * 1024 * 1024) {
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
        profile_model.uploadFileUserpic(formData,
            function (/*json*/) {
                self.userpicUpdated();
            },
            function (json) {
                $('#selectUserpic_error_file').html(error_codes[json.message]);
            }
        );
    }
};

$(document).ready(function () {
    userpic_controller.init();
});
