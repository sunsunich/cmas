var fileUpload_model = {
    uploadUrl: "",
    processingProgressUrl: "",

    uploadFile: function (formData, progressListener, successHandler, unSuccessHandler) {
        var self = this;
        $.ajax({
            type: "Post",
            enctype: 'multipart/form-data',
            url: self.uploadUrl,
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            xhr: function () {
                var myXhr = $.ajaxSettings.xhr();
                if (myXhr.upload) {
                    myXhr.upload.addEventListener('progress', progressListener, false);
                }
                return myXhr;
            },
            success: function (jsonStr) {
                var json = JSON.parse(jsonStr);
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    unSuccessHandler(json);
                }
            },
            error: function () {
                unSuccessHandler();
            }
        });
    },

    getFileProcessingProgress: function (successHandler, unSuccessHandler) {
        basicClient.sendGetRequest(
            false,
            this.processingProgressUrl,
            {},
            successHandler, unSuccessHandler
        );
    }
};
