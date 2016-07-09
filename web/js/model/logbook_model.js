var logbook_model = {

    spotId: "",
    buddiesIds: [],
    instructorId: "",
    score: 'ZERO_STAR',
    scoreInt: 0,

    createRecord: function (form, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "POST",
            url: "/secure/createRecord.html",
            dataType: "json",
            data: form,
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
                window.location.reload();
            }
        });
    }
};