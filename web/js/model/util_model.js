var util_model = {
    getDiver: function (diverId, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "Get",
            url: "/secure/getDiver.html",
            dataType: "json",
            data: {"diverId": diverId},
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
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
    }
};
