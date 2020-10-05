var fed_index_controller = {

    init: function () {
        fileUpload_model.uploadUrl = "/fed/uploadUsers.html";
        fileUpload_model.processingProgressUrl = "/fed/getUploadUsersProgress.html";
        fileUpload_controller.model = fileUpload_model;
        fileUpload_controller.uploadForm = $("#diverUpload");
        fileUpload_controller.inputElem = $("#diverUploadInput");
        fileUpload_controller.textElem = $("#progressText");
        fileUpload_controller.progressOuterElem = $("#progressOuter");
        fileUpload_controller.progressElem = $("#progress");
        fileUpload_controller.errorElem = $("#fileUploadError");
        fileUpload_controller.successAction = function (diversProcessed) {
            $('#diverCnt').html(diversProcessed);
            $('#uploadSuccess').show()
        };

        fileUpload_controller.init();
        this.setListeners();
    },

    setListeners: function () {
        $('#uploadSuccessClose').click(function () {
            window.location.reload();
        });
        $('#uploadSuccessOk').click(function () {
            window.location = "/fed/index.html?sort=dateEdited&dir=true"
        });
    }
};

$(document).ready(function () {
    fed_index_controller.init();
});
