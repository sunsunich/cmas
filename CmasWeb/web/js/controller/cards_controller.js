var cards_controller = {

    init: function () {
        var self = this;
        $(".card-container img").each(function () {
            if ($(this).attr("src").endsWith("/")) {
                self.loadCard(this.id);
            }
        });
        this.setListeners();
    },

    loadCard: function (id) {
        profile_model.loadCard(
            id
            , function (json) {
                $('#' + id).attr("src", imagesData.cardsPicRoot + json.imageUrl);
            }
            , function () {
            });
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
