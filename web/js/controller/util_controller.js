var util_controller = {

    init: function () {
        $('#showDiverClose').click(function () {
            $('#showDiver').hide();
        });
        $('#showDiverOk').click(function () {
            $('#showDiver').hide();
        });
    },

    showDiver: function (elemId) {
        var diverId = elemId.split('_')[0];
        util_model.getDiver(
            diverId
            , function (json) {
                $('#showDiverContent').html(
                    new EJS({url: '/js/templates/diverDialog.ejs'}).render({"diver": json})
                );
                $('#showDiver').show();
            }
            , function (json) {
                if (json && json.hasOwnProperty("message")) {
                    error_dialog_controller.showErrorDialog(error_codes[json.message]);
                }
                else {
                    error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
                }
            });
    }
};

$(document).ready(function () {
    util_controller.init();
});
