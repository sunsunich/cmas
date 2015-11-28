var login_controller = {

    init: function() {
        this.setListeners();
    },

    setListeners: function() {
        var self = this;
        $('#loginForm').click(function() {
            self.sendLogin();
            return false;
        });
        $('#txbPassword').keydown(function (e) {
            if (e.which == 13) {
                self.sendLogin();
                return false;
            }
            return true;
        });
    },

    sendLogin: function () {
        $("#errorMessage1").empty();
        var username = $('#txbLogin').val();
        var password = $('#txbPassword').val();
        var rememberMe = '';
        //if ($('#rememberLogin').prop("checked")) {
        //    rememberMe = 'on';
        //}
        login_model.login(
                username
                , password
                , rememberMe
                , function(json) {
                    window.location = json.redirectUrl;
                }
                , function(message) {
                    $("#errorMessage1").html("Invalid ID or password, login failed!");
                });
        // this.overlay.hide();
    }
};

$(document).ready(function() {
    login_controller.init();
});
