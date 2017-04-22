var fed_edit_diver_model = {

    freeCardId: 1,
    displayCards: {},
    diver: {},

    getCardPrintName: function (card, successHandler, errorHandler) {
        loader_controller.startwait();
        $.ajax({
            type: "GET",
            url: "/fed/getCardPrintName.html",
            dataType: "json",
            data: {"cardJson": JSON.stringify(card)},
            success: function (json) {
                if (json.success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function (e) {
                loader_controller.stopwait();
            }
        });
    },

    uploadDiver: function (successHandler, errorHandler) {
        var self = this;
        loader_controller.startwait();
        $.ajax({
            type: "POST",
            url: "/fed/uploadDiver.html",
            dataType: "json",
            data: {"diverJson": JSON.stringify(self.diver)},
            success: function (json) {
                if (json.success) {
                    successHandler(json);
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function (e) {
                window.location.reload();
            }
        });
    }
};