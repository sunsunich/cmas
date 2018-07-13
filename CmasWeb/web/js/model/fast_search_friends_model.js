var fast_search_friends_model = {

    fastSearchFriends: function (input, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/social/searchFriendsFast.html",
            {"input": input},
            successHandler, unSuccessHandler
        );
    },

    sendFriendRequest: function (diverId, successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/social/sendFriendRequest.html",
            {"diverId": diverId},
            successHandler, unSuccessHandler
        );
    }
};