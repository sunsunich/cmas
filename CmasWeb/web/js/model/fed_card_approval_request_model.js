var fed_card_approval_request_model = {

    requestId : null,
    requestStatus : null,
    cardType : null,

    approveCardApprovalRequest: function (form, successHandler, unSuccessHandler) {
        basicClient.sendPostRequestCommonCase(
            "/fed/approveCardApprovalRequest.html",
            form,
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    }
};