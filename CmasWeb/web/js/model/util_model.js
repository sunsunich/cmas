var util_model = {
    getDiver: function (diverId, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/getDiver.html",
            {"diverId": diverId},
            successHandler, unSuccessHandler
        );
    }
};
