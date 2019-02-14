var loyalty_program_model = {

    isDiverAllowedLoyaltyProgram: false,

    placeCameraOrder: function (successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/createCameraOrder.html",
            {},
            successHandler, unSuccessHandler
        );
    }
};