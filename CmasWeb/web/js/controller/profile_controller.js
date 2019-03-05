var profile_controller = {

    myFriendsFeedController: null,

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;

        if (isCMAS) {
            $("#cardReload").click(function () {
                self.loadPrimaryCard();
            });
            self.loadPrimaryCard();
        }

        var my_friends_logbook_feed_model = simpleClone(logbook_feed_model);
        my_friends_logbook_feed_model.isMyRecords = true;
       // my_friends_logbook_feed_model.templateName = "logbookFeedFull";
        my_friends_logbook_feed_model.url = "/secure/getMyPublicLogbookFeed.html";
        my_friends_logbook_feed_model.containerId = 'accountFeed';
        this.myFriendsFeedController = simpleClone(logbook_feed_controller);
        this.myFriendsFeedController.model = my_friends_logbook_feed_model;
        this.myFriendsFeedController.init();
        this.myFriendsFeedController.start();
    },

    loadPrimaryCard: function () {
        profile_model.loadCard(
            cmas_primaryCardId
            , function (json) {
                $('#noCard').hide();
                $('#cardImg').attr("src", imagesData.cardsPicRoot + json.imageUrl);
                $('#card').show();
            }
            , function () {
                $('#noCard').show();
                $('#card').hide();
            });
    },

    resetFeed: function () {
        this.myFriendsFeedController.resetFeed();
    }
};

$(document).ready(function () {
    profile_controller.init();
});
