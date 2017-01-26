var profile_model = {

    isFileUpload: true,

    loadCard: function (cardId, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/profile/getCardImage.html",
            dataType: "json",
            data: {
                cardId: cardId
            },
            success: function (json) {
                if (json.success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                loader_controller.stopwait();
                errorHandler();
            }
        });
    },

    changePassword: function (form, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "POST",
            url: "/secure/processEditPasswd.html",
            dataType: "json",
            data: form,
            success: function (json) {
                if (json.success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                window.location.reload();
            }
        });
    },

    changeEmail: function (form, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "POST",
            url: "/secure/processEditEmail.html",
            dataType: "json",
            data: form,
            success: function (json) {
                if (json.success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                window.location.reload();
            }
        });
    },

    loadUserpic: function (successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "GET",
            url: "/secure/profile/getUserpic.html",
            dataType: "json",
            data: {},
            success: function (json) {
                if (json.success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function (e) {
                loader_controller.stopwait();
            }
        });
    },

    uploadFileUserpic: function (form, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "POST",
            url: "/secure/uploadFileUserpic.html",
            cache: false,
            contentType: false,
            enctype: 'multipart/form-data',
            processData: false,
            data: form,
            success: function (jsonStr) {
                var json = JSON.parse(jsonStr);
                if (json.success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                window.location.reload();
            }
        });
    },

    changeUserpic: function (imageBase64Bytes, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "POST",
            url: "/secure/processEditUserpic.html",
            dataType: "json",
            data: {
                imageBase64Bytes: imageBase64Bytes
            },
            success: function (json) {
                if (json.success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                window.location.reload();
            }
        });
    }
};