var fed_edit_diver_model = {

    freeCardId: 1,
    displayCards: {},
    diver: {},

    getCardPrintName: function (card, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/fed/getCardPrintName.html",
            {"cardJson": JSON.stringify(card)},
            successHandler, unSuccessHandler
        );
    },

    uploadDiver: function (successHandler, unSuccessHandler) {
        basicClient.sendPostRequestCommonCase(
            "/fed/uploadDiver.html",
            {"diverJson": JSON.stringify(this.diver)},
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    }
};