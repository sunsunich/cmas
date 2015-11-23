var registration_controller = {

    init: function() {
        this.setListeners();
    },

    setListeners: function() {
        var self = this;
        $('#loginForm').submit(function() {
            self.sendLogin();
            return false;
        });
    },

    sendLogin: function () {
        var username = $('#loginField').val();
        var password = $('#passField').val();
        var rememberMe;
        if ($('#rememberLogin').prop("checked")) {
            rememberMe = 'on';
        } else {
            rememberMe = '';
        }
        login_model.login(
                username
                , password
                , rememberMe
                , function(json) {
                    window.location = json.redirectUrl;
                }
                , function(message) {
                    $("#error").fadeOut("300");
                    setTimeout(function() {
                        $("#error").fadeIn("slow");
                    }, 300);
                });
        // this.overlay.hide();
    }
};

$(document).ready(function() {
    registration_controller.init();
});
