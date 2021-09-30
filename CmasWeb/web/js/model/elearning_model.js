var elearning_model = {

    tokenStr: null,
    tokenStatus: "ASSIGNED",
    tokeErrorCode: null,

    getElearningToken: function (successHandler, unSuccessHandler) {
        var self = this;
        basicClient.sendGetRequestCommonCase(
            "/secure/getElearningToken.html",
            {},
            function (json) {
                self.tokenStr = json.token;
                self.tokenStatus = json.status;
                successHandler(json)
            }, function (json) {
                self.tokeErrorCode = json.message;
                unSuccessHandler(json)
            }
        );
    },

    elearningRegister: function (successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/elearningRegister.html",
            {},
            successHandler, unSuccessHandler
        );
    }
};