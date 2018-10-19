var fast_search_divers_model = {

    searchDiversFast: function (input, diverType, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/social/searchDiversFast.html",
            {"input": input, "diverType" : diverType},
            successHandler, unSuccessHandler
        );
    }
};