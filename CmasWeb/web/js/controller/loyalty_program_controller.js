var loyalty_program_controller = {

    init: function () {
        registration_flow_controller.init('simple', {backgroundImageId: 'loyaltyProgramImageBackground'});
        this.setListeners();
    },

    onResize: function () {
        var viewPortWidth = $(window).width();
        if (viewPortWidth > 630) {
            $('#cameraTextDescription').hide();
        } else {
            $('#cameraTextDescription').show();
        }
    },

    setListeners: function () {
        var self = this;
        $(window).load(function () {
            self.onResize();
        });
        $(window).resize(function () {
            self.onResize();
        });

        $('#placeOrder').click(function () {
            if (loyalty_program_model.isDiverAllowedLoyaltyProgram) {
                $('#cameraOrderConsent').show();
            } else {
                window.location = '/secure/pay.html';
            }
            return false;
        });
        $('#orderPlaceSuccessClose').click(function () {
            $('#orderPlaceSuccess').hide();
        });
        $('#orderPlaceSuccessOk').click(function () {
            $('#orderPlaceSuccess').hide();
        });
        $('#cameraOrderConsentClose').click(function () {
            $('#cameraOrderConsent').hide();
        });
        $('#cameraOrder_termsAndCondAccepted').on("change", function () {
            if ($('#cameraOrder_termsAndCondAccepted').prop("checked")) {
                $('#cameraOrderConsent').hide();
                self.placeCameraOrder();
            } else {
                $('#cameraOrder_error_termsAndCondAccepted').html(
                    error_codes['validation.cameraOrderTermsAndCondNotAccepted']
                );
            }
        });
    },

    placeCameraOrder: function () {
        loyalty_program_model.placeCameraOrder(
            function () {
                $('#orderPlaceSuccess').show();
                $('#placeOrderContainer').hide();
            }, function (json) {
                error_dialog_controller.showErrorDialog(error_codes[json.message]);
                $('#placeOrderContainer').hide();
            }, function () {
                error_dialog_controller.showErrorDialog(error_codes["error.loyalty.program.camera.orderFailed"]);
            }
        );
    }
};

$(document).ready(function () {
    loyalty_program_controller.init();
});
