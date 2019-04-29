var loyalty_program_model = {

    itemId : "",
    isDiverAllowedLoyaltyProgram: false,

    placeCameraOrder: function (successHandler, unSuccessHandler, errorHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/createCameraOrder.html",
            {"itemId": this.itemId},
            successHandler, unSuccessHandler, errorHandler
        );
    }
};