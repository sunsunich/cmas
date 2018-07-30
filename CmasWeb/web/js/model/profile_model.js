var profile_model = {

    isFileUpload: true,

    loadCard: function (cardId, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/profile/getCardImageUrl.html",
            {"cardId": cardId},
            successHandler, unSuccessHandler
        );
    },

    loadUserpic: function (successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/profile/getUserpicUrl.html",
            {},
            successHandler, unSuccessHandler,
            function () {
            }
        );
    },

    uploadFileUserpic: function (form, successHandler, unSuccessHandler) {
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
                    unSuccessHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function () {
                window.location.reload();
            }
        });
    },

    changeUserpic: function (imageBase64Bytes, successHandler, unSuccessHandler) {
        basicClient.sendPostRequestCommonCase(
            "/secure/processEditUserpic.html",
            {imageBase64Bytes: imageBase64Bytes},
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    },

    changeEmail: function (form, successHandler, unSuccessHandler) {
        basicClient.sendPostRequestCommonCase(
            "/secure/processEditEmail.html",
            form,
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    },

    changePassword: function (form, successHandler, unSuccessHandler) {
        basicClient.sendPostRequestCommonCase(
            "/secure/processEditPassword.html",
            form,
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    }

};