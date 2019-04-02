var fed_edit_diver_controller = {

    validationController: null,
    cardValidationController: null,

    init: function () {
        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'diver';
        this.validationController.fields = [
            {
                id: 'email',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emailEmpty';
                    }
                }
            },
            {
                id: 'firstName',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyField';
                    }
                }
            },
            {
                id: 'lastName',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyField';
                    }
                }
            },
            {
                id: 'dob',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyField';
                    }
                }
            },
            {
                id: 'diverType',
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
            }
        ];
        this.validationController.init();

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
            }
        ];
        this.cardValidationController.init();

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
            new EJS({url: '/js/templates/editDiverCard.ejs?v=' + webVersion}).render({"card": card})
        );
        $('#' + card.uiId + '_deleteCard').click(function () {
            var uiId = $(this)[0].id.split('_')[0];
            delete fed_edit_diver_model.displayCards[uiId];
            $('#' + uiId).remove();
        });
    },

    addCard: function () {
        if (this.cardValidationController.validateForm()) {
            const card = this.cardValidationController.form;
            card.number = $('#card_number').val();
            card.diverLevel = $('#card_diverLevel').val();

            var self = this;
            fed_edit_diver_model.getCardPrintName(card
                , function (json) {
                    card.printName = json.message;
                    self.addCardToModel(card);
                }
                , function (json) {
                }
            );
        }
    },

    uploadDiver: function () {
        if (this.validationController.validateForm()) {
            fed_edit_diver_model.diver = this.validationController.form;
            fed_edit_diver_model.diver.id = $('#diver_id').val();
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

            var self = this;
            fed_edit_diver_model.uploadDiver(
                function (/*json*/) {
                    $("#diverSaveSuccess").show();
                }
                , function (json) {
                    self.validationController.showErrors(json);
                });
        }
    }
};

$(document).ready(function () {
    fed_edit_diver_controller.init();
});
