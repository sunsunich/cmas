var loader_controller = {

    init: function () {
    },

    startwait: function () {
        $("#loading").show();
    },

    stopwait: function () {
        $("#loading").hide();
    }

};

$(document).ready(function() {
    loader_controller.init();
});


