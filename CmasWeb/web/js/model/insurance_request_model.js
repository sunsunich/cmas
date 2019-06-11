var insurance_request_model = {

    url: "/secure/createInsuranceRequest.html",

    genders: [],

    insuranceRequest: {
        diver: { id : ""},
        address: {
            country : { code : ""}
        },
        gender : ""
    },

    sendInsuranceRequest: function (successHandler, unSuccessHandler) {
        var self = this;
        basicClient.sendPostRequestCommonCase(
            this.url,
            {"insuranceRequestJson": JSON.stringify(this.insuranceRequest)},
            function (json) {
                successHandler(json);
            },
            unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    }
};