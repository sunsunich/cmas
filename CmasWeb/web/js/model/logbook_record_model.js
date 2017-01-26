var logbook_record_model = {

    logbookEntry: {state: 'NEW', diveSpec: {scubaTanks: []}, spot: {}},

    weatherTypes: [],
    surfaceTypes: [],
    currentTypes: [],
    underWaterVisibilityTypes: [],
    waterTypes: [],
    temperatureMeasureUnits: [],

    divePurposeTypes: [],
    entryTypes: [],
    diveSuitTypes: [],

    volumeMeasureUnits: [],
    pressureMeasureUnits: [],
    supplyTypes: [],

    findDiverMode: "NAME",
    isInstructorSearch: false,
    diverId: "",
    buddiesIds: [],
    instructorId: "",
    visibilityTypes: [],

    createDraftRecord: function (successHandler, errorHandler) {
        var self = this;
        loader_controller.startwait();
        $.ajax({
            type: "POST",
            url: "/secure/saveDraftRecord.html",
            dataType: "json",
            data: {"logbookEntryJson": JSON.stringify(self.logbookEntry)},
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
                loader_controller.stopwait();
            }
        });
    },

    createRecord: function (successHandler, errorHandler) {
        var self = this;
        loader_controller.startwait();
        $.ajax({
            type: "POST",
            url: "/secure/createRecord.html",
            dataType: "json",
            data: {"logbookEntryJson": JSON.stringify(self.logbookEntry)},
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