var profile_model = {

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

    changeUserpic: function (form, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "POST",
            url: "/secure/processEditUserpic.html",
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
};