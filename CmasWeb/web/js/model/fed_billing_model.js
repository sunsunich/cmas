var fed_billing_model = {

    getDiverPaymentList: function (successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/fed/getDiverPaymentList.html",
            null,
            successHandler, unSuccessHandler
        );
    },

    addDiverToPaymentList: function (diverId, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/fed/addDiverToPaymentList.html?diverId=" + diverId,
            null,
            successHandler, unSuccessHandler
        );
    },

    removeDiverFromPaymentList: function (diverId, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/fed/removeDiverFromPaymentList.html?diverId=" + diverId,
            null,
            successHandler, unSuccessHandler
        );
    }
};