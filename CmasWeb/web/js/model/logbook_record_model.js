var logbook_record_model = {

    // freeTankId: 1,
    // displayTanks: {},
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

    createDraftRecord: function (successHandler, unSuccessHandler) {
        basicClient.sendPostRequestCommonCase(
            "/secure/saveDraftRecord.html",
            {"logbookEntryJson": JSON.stringify(this.logbookEntry)},
            successHandler, unSuccessHandler
        );
    },

    createRecord: function (successHandler, unSuccessHandler) {
        basicClient.sendPostRequestCommonCase(
            "/secure/createRecord.html",
            {"logbookEntryJson": JSON.stringify(this.logbookEntry)},
            successHandler, unSuccessHandler,
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