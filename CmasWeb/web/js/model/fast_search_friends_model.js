var fast_search_friends_model = {

    fastSearch: function (url, input, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            url,
            {"input": input},
            successHandler, unSuccessHandler
        );
    }
};