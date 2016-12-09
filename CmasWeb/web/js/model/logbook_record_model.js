var logbook_record_model = {

    diverId: "",
    logbookEntryId: "",
    spotId: "",
    buddiesIds: [],
    instructorId: "",
    score: 'ZERO_STAR',
    scoreInt: 0,
    findDiverMode: "NAME",
    isInstructorSearch: false,

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
    },

    searchDivers: function (form, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "GET",
            url: "/secure/social/searchDivers.html",
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
                errorHandler();
            }
        });
    },

    getDivers: function (diverIds, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "GET",
            url: "/secure/getDivers.html",
            dataType: "json",
            data: {"diverIdsJson": arrayToJsonStr(diverIds)},
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
                errorHandler();
            }
        });
    }
};