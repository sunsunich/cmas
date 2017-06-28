var fileUpload_model = {
    uploadUrl: "",
    processingProgressUrl: "",

    uploadFile: function (formData, progressListener, successHandler, errorHandler) {
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
                    errorHandler(json);
                }
            },
            error: function () {
                errorHandler();
            }
        });
    },

    getFileProcessingProgress: function (successHandler, errorHandler) {
        var self = this;
        $.ajax({
            type: "Get",
            url: self.processingProgressUrl,
            dataType: "json",
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
            },
            error: function () {
                errorHandler();
            }
        });
    }
};
