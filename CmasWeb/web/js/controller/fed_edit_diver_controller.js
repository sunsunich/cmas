var fed_edit_diver_controller = {

    init: function () {
        if (cards == null) {
            cards = [];
        }
        for (var i = 0; i < cards.length; i++) {
            this.addCardToModel(cards[i]);
        }
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $('#diver_dob').datepicker(
            {
                changeYear: true,
                changeMonth: true,
                yearRange: '-100:-10',
                defaultDate: "-20y",
                dateFormat: 'dd/mm/yy'
            }
        ).val(dob);

        $('#addCard').click(function () {
            self.addCard();
        });

        $('#submit_addDiver').click(function () {
            self.uploadDiver();
        });

        $('#diverSaveSuccessClose').click(function () {
            if (fed_edit_diver_model.diver.id > 0) {
                window.location.reload();
            } else {
                window.location = "/fed/index.html"
            }
        });
        $('#diverSaveSuccessOk').click(function () {
            if (fed_edit_diver_model.diver.id > 0) {
                window.location.reload();
            } else {
                window.location = "/fed/index.html"
            }
        });
    },

    addCardToModel: function (card) {
        card.uiId = fed_edit_diver_model.freeCardId;
        fed_edit_diver_model.freeCardId++;
        fed_edit_diver_model.displayCards[card.uiId] = card;
        $('#cardsContainer').append(
            new EJS({url: '/js/templates/editDiverCard.ejs'}).render({"card": card})
        );
        $('#' + card.uiId + '_deleteCard').click(function () {
            var uiId = $(this)[0].id.split('_')[0];
            delete fed_edit_diver_model.displayCards[uiId];
            $('#' + uiId).remove();
        });
    },

    addCard: function () {
        const card = {
            number: $('#card_number').val(),
            diverType: $('#card_diverType').val(),
            diverLevel: $('#card_diverLevel').val(),
            cardType: $('#card_cardType').val()
        };

        this.cleanCardErrors();
        var formErrors = this.validateCardForm(card);
        if (formErrors.success) {
            var self = this;
            fed_edit_diver_model.getCardPrintName(card
                , function (json) {
                    card.printName = json.message;
                    self.addCardToModel(card);
                }
                , function (json) {
                });
        }
        else {
            validation_controller.showErrors('card', formErrors);
        }
    },

    validateCardForm: function (card) {
        var result = {};
        result.fieldErrors = {};
        result.errors = {};
        if (isStringTrimmedEmpty(card.diverType)) {
            result.fieldErrors["diverType"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(card.cardType)) {
            result.fieldErrors["cardType"] = 'validation.emptyField';
        }
        result.success = jQuery.isEmptyObject(result.fieldErrors) && jQuery.isEmptyObject(result.errors);
        return result;
    },

    cleanCardErrors: function () {
        $('#card_error_diverType').empty();
        $('#card_error_cardType').empty();
    },


    uploadDiver: function () {
        fed_edit_diver_model.diver.id = $('#diver_id').val();
        fed_edit_diver_model.diver.email = $('#diver_email').val();
        fed_edit_diver_model.diver.firstName = $('#diver_firstName').val();
        fed_edit_diver_model.diver.lastName = $('#diver_lastName').val();
        fed_edit_diver_model.diver.dob = $('#diver_dob').val();
        fed_edit_diver_model.diver.diverLevel = $('#diver_diverLevel').val();
        fed_edit_diver_model.diver.diverType = $('#diver_diverType').val();
        fed_edit_diver_model.diver.cards = [];
        for (var attr in fed_edit_diver_model.displayCards) {
            if (fed_edit_diver_model.displayCards.hasOwnProperty(attr)) {
                fed_edit_diver_model.diver.cards.push(fed_edit_diver_model.displayCards[attr]);
            }
        }

        var natFedCardNumber = $('#diver_natFedCardNumber').val();
        var natFedInstructorCardNumber = $('#diver_natFedInstructorCardNumber').val();

        if (!isStringTrimmedEmpty(natFedCardNumber)) {
            const card = {
                number: natFedCardNumber,
                diverType: fed_edit_diver_model.diver.diverType,
                diverLevel: fed_edit_diver_model.diver.diverLevel,
                cardType: 'NATIONAL'
            };
            fed_edit_diver_model.diver.cards.push(card);
        }
        if (!isStringTrimmedEmpty(natFedInstructorCardNumber)) {
            const card = {
                number: natFedInstructorCardNumber
            };
            fed_edit_diver_model.diver.instructor = {};
            fed_edit_diver_model.diver.instructor.cards = [];
            fed_edit_diver_model.diver.instructor.cards.push(card);
        }

        this.cleanDiverErrors();
        var formErrors = this.validateDiverForm(fed_edit_diver_model.diver);
        if (formErrors.success) {
            var self = this;
            fed_edit_diver_model.uploadDiver(
                function (json) {
                    $("#diverSaveSuccess").show();
                }
                , function (json) {
                    validation_controller.showErrors('diver', json);
                });
        }
        else {
            validation_controller.showErrors('diver', formErrors);
        }
    },

    validateDiverForm: function (diver) {
        var result = {};
        result.fieldErrors = {};
        result.errors = {};
        if (isStringTrimmedEmpty(diver.email)) {
            result.fieldErrors["email"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(diver.firstName)) {
            result.fieldErrors["firstName"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(diver.lastName)) {
            result.fieldErrors["lastName"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(diver.dob)) {
            result.fieldErrors["dob"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(diver.diverType)) {
            result.fieldErrors["diverType"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(diver.diverLevel)) {
            result.fieldErrors["diverLevel"] = 'validation.emptyField';
        }
        result.success = jQuery.isEmptyObject(result.fieldErrors) && jQuery.isEmptyObject(result.errors);
        return result;
    },

    cleanDiverErrors: function () {
        $("#diver_error_email").empty();
        $("#diver_error_firstName").empty();
        $("#diver_error_lastName").empty();
        $("#diver_error_dob").empty();
        $("#diver_error_diverType").empty();
        $("#diver_error_diverLevel").empty();
    }
};

$(document).ready(function () {
    fed_edit_diver_controller.init();
});
