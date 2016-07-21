var logbook_controller = {

    myFeedController: null,
    myFriendsFeedController: null,

    init: function () {
        var my_logbook_feed_model = simpleClone(logbook_feed_model);
        my_logbook_feed_model.isMyRecords = true;
        my_logbook_feed_model.url = "/secure/getMyLogbookFeed.html";
        my_logbook_feed_model.containerId = 'myLogbookFeed';
        this.myFeedController = simpleClone(logbook_feed_controller);
        this.myFeedController.model = my_logbook_feed_model;
        this.myFeedController.init();

        var my_friends_logbook_feed_model = simpleClone(logbook_feed_model);
        my_friends_logbook_feed_model.isMyRecords = false;
        my_friends_logbook_feed_model.url = "/secure/getMyFriendsLogbookFeed.html";
        my_friends_logbook_feed_model.containerId = 'friendsLogbookFeed';
        this.myFriendsFeedController = simpleClone(logbook_feed_controller);
        this.myFriendsFeedController.model = my_friends_logbook_feed_model;

        this.setListeners();
    },

    setListeners: function () {
        var self = this;

        var tabName = cookie_controller.readCookie("LOGBOOK_TAB");
        if (tabName) {
            self.showTab(tabName);
        } else {
            self.showTab('MY');
        }
        $('#myTab').click(function () {
            self.showTab('MY');
        });
        $('#friendsTab').click(function () {
            self.showTab('FRIENDS');
        });
        $('#createLogbookEntryButton').click(function () {
            window.location = "/secure/showSpots.html";
        });
    },

    showTab: function (tabName) {
        this.myFeedController.stop();
        this.myFriendsFeedController.stop();
        if (tabName == 'MY') {
            this.myFeedController.start();
            $('#friendsTab').addClass('inactive');
            $('#myTab').removeClass('inactive');
            $('#friendsLogbookFeed').hide();
            $('#myLogbook').show();
        } else {
            this.myFriendsFeedController.start();
            $('#myTab').addClass('inactive');
            $('#friendsTab').removeClass('inactive');
            $('#myLogbook').hide();
            $('#friendsLogbookFeed').show();
        }
        cookie_controller.createCookie("LOGBOOK_TAB", tabName, 0);
    }
};

$(document).ready(function () {
    logbook_controller.init();
});
