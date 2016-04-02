var login_controller = {

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $('#loginSubmit').click(function () {
            self.sendLogin();
            return false;
        });
        $('#passField').keydown(function (e) {
            if (e.which == 13) {
                self.sendLogin();
                return false;
            }
            return true;
        });
        $('#regLink').click(function () {
            window.location.href = "/diver-registration.html";
            return false;
        });
    },

    sendLogin: function () {
        $("#error").hide();
        var username = $('#loginField').val();
        var password = $('#passField').val();
        var rememberMe = '';
        if ($('#rememberLogin').prop("checked")) {
            rememberMe = 'on';
        }
        login_model.login(
            username
            , password
            , rememberMe
            , function (json) {
                window.location.href = json.redirectUrl;
            }
            , function (message) {
                $("#error").show();
            });
        // this.overlay.hide();
    }
};

$(document).ready(function () {
    login_controller.init();
});
