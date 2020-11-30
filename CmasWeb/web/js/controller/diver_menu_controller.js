var diver_menu_controller = {

    unsubscribed: false,

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $(window).load(function () {
            self.onResize();
        });
        $(window).resize(function () {
            self.onResize();
        });
        $('#menuButton').click(function (e) {
            $('#menuItems').show();
        });
        $('#userpicMenuButton').click(function (e) {
            e.preventDefault();
            if ($(this).hasClass('userpic-button-off')) {
                $(this).removeClass('userpic-button-off').addClass('userpic-button-on');
                $('#userMenu').show();
            } else {
                $(this).removeClass('userpic-button-on').addClass('userpic-button-off');
                $('#userMenu').hide();
            }
        });
        $("#subscribeDialogClose").click(function () {
            $('#subscribeDialog').hide();
        });
        $('#subscribeDialogOk').click(function () {
            $('#subscribeDialog').hide();
        });
        $('#menuSubscribeItem').click(function () {
            if (diver_menu_controller.unsubscribed) {
                self.subscribe();
            } else {
                self.unsubscribe();
            }
        });
        if (diver_menu_controller.unsubscribed) {
            $('#menuSubscribeText').html(labels["cmas.face.client.menu.subscribe"]);
        } else {
            $('#menuSubscribeText').html(labels["cmas.face.client.menu.unsubscribe"]);
        }
    },

    subscribe: function () {
        basicClient.sendGetRequestCommonCase(
            "/secure/subscribe.html",
            {},
            function () {
                diver_menu_controller.unsubscribed = false;
                $('#menuSubscribeText').html(labels["cmas.face.client.menu.unsubscribe"]);
                $('#submitDialogText').html(labels["cmas.face.subscribe.success.text"]);
                $('#subscribeDialog').show();
            },
            function () {
                error_dialog_controller.showErrorDialog(labels["cmas.face.error.message.again"])
            },
            function () {
                window.location.reload();
            }
        );
        return false;
    },

    unsubscribe: function () {
        basicClient.sendGetRequestCommonCase(
            "/secure/unsubscribe.html",
            {},
            function () {
                diver_menu_controller.unsubscribed = true;
                $('#menuSubscribeText').html(labels["cmas.face.client.menu.subscribe"]);
                $('#submitDialogText').html(labels["cmas.face.unsubscribe.success.text"]);
                $('#subscribeDialog').show();
            },
            function () {
                error_dialog_controller.showErrorDialog(labels["cmas.face.error.message.again"])
            },
            function () {
                window.location.reload();
            }
        );
        return false;
    },

    onResize: function () {
    }
};
