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

    diverId: "",
    buddiesIds: [],
    instructorId: "",
    visibilityTypes: [],

    additionalFieldIds: [
        "prevDiveDate", "prevDiveTime", "avgDepthMeters", "note", "weather", "surface", "waterType", "current",
        "underWaterVisibility", "airTemp", "waterTemp", "temperatureMeasureUnit", "divePurpose", "entryType",
        "additionalWeightKg", "diveSuit", "decoStepsComments", "cnsToxicity"
    ],

    createDraftRecord: function (successHandler, unSuccessHandler) {
        var self = this;
        basicClient.sendPostRequestCommonCase(
            "/secure/saveDraftRecord.html",
            {"logbookEntryJson": JSON.stringify(this.logbookEntry)},
            function (json) {
                self.logbookEntry.id = json.id;
                self.addPhotoToRecord(successHandler, unSuccessHandler);
            },
            unSuccessHandler
        );
    },

    addPhotoToRecord: function (successHandler, unSuccessHandler) {
        var logbookEntryPhotoSrc = $('#logbookEntryPhoto').attr('src');
        if (!logbookEntryPhotoSrc || logbookEntryPhotoSrc.indexOf('base64') == -1) {
            successHandler();
            return;
        }
        var input = $("#photoFileInput")[0];
        var file = input.files[0];
        var form = new FormData();
        form.append('file', file);
        form.append('logbookEntryId', this.logbookEntry.id);
        $.ajax({
            type: "POST",
            url: "/secure/addPhotoToRecord.html",
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
            },
            error: function () {
                window.location.reload();
            }
        });
    },

    createRecord: function (successHandler, unSuccessHandler) {
        var self = this;
        basicClient.sendPostRequestCommonCase(
            "/secure/createRecord.html",
            {"logbookEntryJson": JSON.stringify(this.logbookEntry)},
            function (json) {
                self.logbookEntry.id = json.id;
                self.addPhotoToRecord(successHandler, function () {
                    window.location.reload();
                });
            },
            unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    },

    searchDivers: function (form, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/social/searchDivers.html",
            form,
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    },

    getDivers: function (diverIds, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/getDivers.html",
            {"diverIdsJson": arrayToJsonStr(diverIds)},
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    }
};