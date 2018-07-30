var recovery_model = {

    changePassword: function (form, successHandler, unSuccessHandler) {
        basicClient.sendPostRequestCommonCase(
            "/changePasswd.html",
            form,
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    }
};