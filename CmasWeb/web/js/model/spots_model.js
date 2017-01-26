var spots_model = {

    logbookEntryId: "",
    map: {},
    spotMap: {},
    currentEditSpot: {},
    lastOpenInfoWindow: null,
    currentBounds: null,
    delta: 0.0005, // 50 meters

    getSpots: function (bounds, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "GET",
            url: "/secure/getSpots.html",
            dataType: "json",
            data: bounds,
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

    getSpotByCoords: function (latitude, longitude, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "GET",
            url: "/secure/getSpotByCoords.html",
            dataType: "json",
            data: {"latitude": latitude, "longitude": longitude},
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

    createSpot: function (editSpotForm, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "GET",
            url: "/secure/createSpot.html",
            dataType: "json",
            data: editSpotForm,
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