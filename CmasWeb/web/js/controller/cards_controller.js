var cards_controller = {

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        for (var i = 0; i < cmas_cardIds.length; i++) {
            var cardId = cmas_cardIds[i];
            $('#' + cardId).click(function () {
                self.changeCardWidth($(this));
            });
        }
    },

    changeCardWidth: function (cardImg) {
        var width = cardImg.width();
        if (width != '546') {
            cardImg.width('546');
        }
        else {
            cardImg.css("width", "");
        }
    }
};

$(document).ready(function () {
    cards_controller.init();
});
