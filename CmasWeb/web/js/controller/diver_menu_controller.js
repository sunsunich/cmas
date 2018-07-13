var diver_menu_controller = {

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
    },

    onResize: function () {

    }
};
$(document).ready(function () {
    diver_menu_controller.init();
});

