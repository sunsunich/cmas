var userpic_controller = {

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        if (!(Modernizr.canvas &&
            Modernizr.video &&
            Modernizr.todataurlpng &&
            Modernizr.getusermedia
        )) {
            $("#cameraSelect").hide();
        }

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
        $("#userpicFileInput").change(function () {
            profile_model.isFileUpload = true;
            self.loadFileToPreview(this);
        });
        $("#selectUserpicOk").click(function () {
            if (profile_model.isFileUpload) {
                self.uploadFile();
            } else {
                self.uploadCameraImage();
            }
        });
        $("#cameraSelect").click(function () {
            self.resetUserPicChooser();
            self.showCameraPreview();
        });
        $("#cameraPreviewClose").click(function () {
            self.hideCameraPreview();
            $('#cameraPreview').hide();
            $('#selectUserpic').show();
        });
        $('#takePicture').click(function () {
            self.takePicture();
        });
        $('#takePictureAgain').click(function () {
            self.showCameraPreview();
        });
        $("#cameraPreviewOk").click(function () {
            self.loadImageToPreview();
            self.hideCameraPreview();
            $('#cameraPreview').hide();
            $('#selectUserpic').show();
        });

        self.loadUserpic();
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

    stream: null,

    showCameraPreview: function () {
        var self = this;
        navigator.getUserMedia = navigator.getUserMedia ||
            navigator.webkitGetUserMedia ||
            navigator.mozGetUserMedia ||
            navigator.msGetUserMedia;

        var constraints = {
            video: {
                mandatory: {
                    maxWidth: 300,
                    maxHeight: 150
                }
            }
        };
        var video = document.querySelector('video');
        navigator.getUserMedia(constraints,
            function (stream) {
                self.stream = stream;
                video.src = window.URL.createObjectURL(stream);
            },
            function (e) {
                console.log('getUserMedia() error', e);
            });

        $('#selectUserpic').hide();
        $('#cameraPreviewOk').hide();
        $('#takePictureAgain').hide();
        $('#cameraPreviewImg').hide();
        $('#takePicture').show();
        $("video").show();
        $('#cameraPreview').show();
    },

    hideCameraPreview: function () {
        if (this.stream) {
            this.stream.getTracks()[0].stop();
            this.stream = null;
            var video = document.querySelector('video');
            video.src = "";
            $("video").hide();
        }
    },

    takePicture: function () {
        var video = document.querySelector('video');
        var canvas = document.querySelector('canvas');
        var ctx = canvas.getContext('2d');
        ctx.drawImage(video, 0, 0);
        $('#cameraPreviewImg').attr('src', canvas.toDataURL('image/png'));

        $('#takePicture').hide();
        $('#takePictureAgain').show();
        $('#cameraPreviewImg').show()
        $('#cameraPreviewOk').show();
        this.hideCameraPreview();
    },

    loadImageToPreview: function () {
        $('#userpicPreview').attr('src', $('#cameraPreviewImg').attr("src"));
        profile_model.isFileUpload = false;
    },

    uploadCameraImage: function () {
        var self = this;
        var imageData = $('#userpicPreview').attr("src");
        profile_model.changeUserpic(imageData.substring(imageData.indexOf(',') + 1),
            function (/*json*/) {
                self.loadUserpic();
                $('#selectUserpic').hide();
            },
            function (json) {
                $('#selectUserpic_error_file').html(error_codes[json.message]);
            }
        );
    },

    selectUserpicFromDrive: function () {
        $('#userpicFileInput').trigger('click');
    },

    loadFileToPreview: function (input) {
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
        profile_model.uploadFileUserpic(formData,
            function (/*json*/) {
                self.loadUserpic();
                $('#selectUserpic').hide();
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
