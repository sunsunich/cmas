var basicClient = {
    sendRequest: function (showLoader, url, requestType, data, successHandler, unSuccessHandler, errorHandler) {
        if (showLoader) {
            loader_controller.startwait();
        }
        $.ajax({
            type: requestType,
            url: url,
            dataType: "json",
            data: data,
            success: function (json) {
                if (json.hasOwnProperty('success')) {
                    if (json.success) {
                        successHandler(json);
                    } else {
                        if (json.redirectUrl) {
                            window.location = json.redirectUrl;
                        } else {
                            unSuccessHandler(json);
                        }
                    }
                } else {
                    successHandler(json);
                }
                if (showLoader) {
                    loader_controller.stopwait();
                }
            },
            error: function (e) {
                if (errorHandler) {
                    errorHandler(e);
                } else if (unSuccessHandler) {
                    unSuccessHandler({"message": "validation.internal"});
                } else {
                    window.location.reload();
                }
                if (showLoader) {
                    loader_controller.stopwait();
                }
            }
        });
    },

    sendGetRequest: function (showLoader, url, data, successHandler, unSuccessHandler, errorHandler) {
        this.sendRequest(showLoader, url, "GET", data, successHandler, unSuccessHandler, errorHandler)
    },

    sendPostRequest: function (showLoader, url, data, successHandler, unSuccessHandler, errorHandler) {
        this.sendRequest(showLoader, url, "POST", data, successHandler, unSuccessHandler, errorHandler)
    },

    sendGetRequestCommonCase: function (url, data, successHandler, unSuccessHandler, errorHandler) {
        this.sendGetRequest(true, url, data, successHandler, unSuccessHandler, errorHandler);
    },

    sendPostRequestCommonCase: function (url, data, successHandler, unSuccessHandler, errorHandler) {
        this.sendPostRequest(true, url, data, successHandler, unSuccessHandler, errorHandler);
    }
};
