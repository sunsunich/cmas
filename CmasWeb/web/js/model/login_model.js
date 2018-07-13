var login_model = {

    login: function (username, password, remember_me, successHandler, unSuccessHandler) {
        basicClient.sendPostRequestCommonCase(
            "/j_spring_security_check",
            {
                j_username: username
                , j_password: password
                , _spring_security_remember_me: remember_me
            },
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    }
};