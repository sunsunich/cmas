var cards_controller = {

    resetFields: function () {
        cmas_cardIds = [];
    },

    init: function (cmas_cardIds) {
        this.cmas_cardIds = cmas_cardIds;
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
        for (var i = 0; i < this.cmas_cardIds.length; i++) {
            var cardId = this.cmas_cardIds[i];
            $('#' + cardId).click(function () {
                self.changeCardWidth($(this));
            });
        }
    },

    changeCardWidth: function (cardImg) {
        var width = cardImg.width();
        if (width != '546') {
            cardImg.width('546');
            cardImg.addClass('clearfix');
        } else {
            cardImg.css("width", "");
            cardImg.removeClass('clearfix');
        }
    }
};
