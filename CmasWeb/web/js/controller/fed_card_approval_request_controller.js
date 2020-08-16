var fed_card_approval_request_controller = {

    cardValidationController: null,

    init: function () {
        this.cardValidationController = simpleClone(validation_controller);
        this.cardValidationController.prefix = 'card';
        this.cardValidationController.fields = [
            {
                id: 'diverType',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyField';
                    }
                }
            },
            {
                id: 'cardType',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyField';
                    }
                }
            },
            {
                id: 'diverLevel',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyField';
                    }
                }
            },
            {
                id: 'federationCardNumber',
                validateField: function (value) {
                }
            },
            {
                id: 'requestId',
                validateField: function (value) {
                }
            },
            {
                id: 'validUntil',
                validateField: function (value) {
                }
            },
            {
                id: 'requestId',
                validateField: function (value) {
                }
            }
        ];
        this.cardValidationController.init();

        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $(window).resize(function () {
            self.onResize();
        });
        $('#card_validUntil').datepicker(
            {
                changeYear: true,
                changeMonth: true,
                yearRange: '0:+10',
                defaultDate: "1d",
                dateFormat: 'dd/mm/yy'
            }
        ).val(validUntil);
        if (fed_card_approval_request_model.cardType == null
            || fed_card_approval_request_model.cardType == '') {
            $("#card_cardType").val('');
        }
        var backText = "Back to certificate requests";
        if (fed_card_approval_request_model.requestStatus == 'APPROVED') {
            $('#submit_addCard').hide();
            $('#decline_addCard').html(backText).click(function () {
                window.location = "/fed/cardApprovalRequests.html";
                return false;
            });
            $('#card_diverType').prop("disabled", true);
            $('#card_cardType').prop("disabled", true);
            $('#card_diverLevel').prop("disabled", true);
            $('#card_federationCardNumber').prop("disabled", true);
            $('#card_validUntil').prop("disabled", true);
        } else if (fed_card_approval_request_model.requestStatus == 'DECLINED') {
            $('#submit_addCard').click(function () {
                self.approveCardApprovalRequest();
                return false;
            });
            $('#decline_addCard').html(backText).click(function () {
                window.location = "/fed/cardApprovalRequests.html";
                return false;
            });
        } else {
            $('#submit_addCard').click(function () {
                self.approveCardApprovalRequest();
                return false;
            });
            $('#decline_addCard').click(function () {
                window.location = "/fed/declineCardApprovalRequest.html?requestId="
                    + fed_card_approval_request_model.requestId;
                return false;
            });
        }
        $('#cardApprovalSuccess').css('position', 'relative');
        $('#cardApprovalSuccess').css('left', '0%');
        $('#cardApprovalSuccessClose').click(function () {
            window.location = "/fed/cardApprovalRequests.html";
            return false;
        });
        $('#cardApprovalSuccessOk').click(function () {
            window.location = "/fed/cardApprovalRequests.html";
            return false;
        });
        this.onResize();
    },

    approveCardApprovalRequest: function () {
        var self = this;
        if (this.cardValidationController.validateForm()) {
            fed_card_approval_request_model.approveCardApprovalRequest(
                this.cardValidationController.form,
                function (/*json*/) {
                    $("#cardApprovalSuccess").show();
                    $('#submit_addCard').prop("disabled", true);
                    $('#decline_addCard').prop("disabled", true);
                }
                , function (json) {
                    self.cardValidationController.showErrors(json);
                });
        }
    },

    onResize: function () {
        var viewPortWidth = $(window).width();
        $('.cardImage').css('max-width', viewPortWidth / 2);
    }
};

$(document).ready(function () {
    fed_card_approval_request_controller.init();
});
