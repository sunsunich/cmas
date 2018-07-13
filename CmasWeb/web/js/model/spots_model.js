var spots_model = {

    logbookEntryId: "",
    map: {},
    spotMap: {},
    currentEditSpot: {},
    currentSpotIdToDelete: "",
    lastOpenInfoWindow: null,
    currentBounds: null,
    delta: 0.0005, // 50 meters

    getSpots: function (bounds, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/getSpots.html",
            bounds,
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    },

    getSpotByCoords: function (latitude, longitude, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/getSpotByCoords.html",
            {"latitude": latitude, "longitude": longitude},
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    },

    createSpot: function (editSpotForm, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/createSpot.html",
            editSpotForm,
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    },

    deleteSpot: function (successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/deleteSpot.html",
            {"spotId": this.currentSpotIdToDelete},
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    }
};