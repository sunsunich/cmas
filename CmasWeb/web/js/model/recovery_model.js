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
    },

    changeEmail: function (form, successHandler, unSuccessHandler) {
        basicClient.sendPostRequestCommonCase(
            "/secure/processEditEmail.html",
            form,
            successHandler, unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    }
};