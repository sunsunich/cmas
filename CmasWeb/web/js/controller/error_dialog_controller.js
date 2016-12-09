var error_dialog_controller = {

    init: function () {
        $("#errorDialogClose").click(function () {
            $('#errorDialog').hide();
        });
        $('#errorDialogOk').click(function () {
            $('#errorDialog').hide();
        });
    },

    showErrorDialog: function (errorText) {
        $('#errorDialogText').html(errorText);
        $('#errorDialog').show();
    }
};

$(document).ready(function () {
    error_dialog_controller.init();
});
