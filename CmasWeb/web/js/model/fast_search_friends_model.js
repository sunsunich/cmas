var fast_search_friends_model = {

    fastSearch: function (url, input, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/social/searchFriendsFast.html",
            {"input": input},
            successHandler, unSuccessHandler
        );
    }
};