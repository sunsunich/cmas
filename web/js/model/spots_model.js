var spots_model = {

    map: {},
    spots: [],
    lastOpenInfoWindow: {},
    spotId: 0,

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
    }
};