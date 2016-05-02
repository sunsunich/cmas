var cards_controller = {

    init: function () {
        this.setListeners();
        // this.loadCard(cmas_primaryCardId);
        for (var i = 0; i < cmas_secondaryCardIds.length; i++) {
            if (cmas_secondaryCardIds[i] != cmas_primaryCardId) {
                this.loadCard(cmas_secondaryCardIds[i]);
            }
        }
    },

    setListeners: function () {
    },

    changeCardWidth: function (cardImg) {
        var width = cardImg.width();
        if (width != '640') {
            cardImg.width('640');
        }
        else {
            cardImg.width('100%');
        }
    },

    loadCard: function (cardId) {
        var self = this;
        profile_model.loadCard(
            cardId
            , function (json) {
                $('#content').append('<img class="content-card" id="' + cardId + '" src = "data:image/png;base64,' + json.base64 + '"/>');
                $('#' + cardId).click(function () {
                    self.changeCardWidth($(this));
                });

            }
            , function () {
            });
    }
};

$(document).ready(function () {
    cards_controller.init();
});
