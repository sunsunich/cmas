var logbook_controller = {

    myFeedController: null,
    myFriendsFeedController: null,

    init: function () {
        var my_logbook_feed_model = simpleClone(logbook_feed_model);
        my_logbook_feed_model.isMyRecords = true;
        my_logbook_feed_model.templateName = "logbookFeedFull";
        my_logbook_feed_model.url = "/secure/getMyLogbookFeed.html";
        my_logbook_feed_model.containerId = 'myLogbookRecordsBlock';
        this.myFeedController = simpleClone(logbook_feed_controller);
        this.myFeedController.model = my_logbook_feed_model;
        this.myFeedController.init();

        var my_friends_logbook_feed_model = simpleClone(logbook_feed_model);
        my_friends_logbook_feed_model.url = "/secure/getFriendsOnlyLogbookFeed.html";
        my_friends_logbook_feed_model.containerId = 'myFriendsLogbookRecordsBlock';
        this.myFriendsFeedController = simpleClone(logbook_feed_controller);
        this.myFriendsFeedController.model = my_friends_logbook_feed_model;
        this.myFriendsFeedController.init();

        var all_logbook_feed_model = simpleClone(logbook_feed_model);
        all_logbook_feed_model.url = "/secure/getMyPublicLogbookFeed.html";
        all_logbook_feed_model.containerId = 'allLogbookRecordsBlock';
        this.allFeedController = simpleClone(logbook_feed_controller);
        this.allFeedController.model = all_logbook_feed_model;
        this.allFeedController.init();

        this.setListeners();

        this.setList('myLogbookRecords');
    },

    setList: function (listSelection) {
        this.listSelection = listSelection;
        var currentLogbookController;
        switch (this.listSelection) {
            case 'myLogbookRecords' :
                this.hideAllBlocks();
                $('#myLogbookRecordsBlock').show();
                $('#myLogbookRecords').addClass('panel-menu-item-active');
                currentLogbookController = this.myFeedController;
                break;
            case 'myFriendsLogbookRecords' :
                this.hideAllBlocks();
                $('#myFriendsLogbookRecordsBlock').show();
                $('#myFriendsLogbookRecords').addClass('panel-menu-item-active');
                currentLogbookController = this.myFriendsFeedController;
                break;
            case 'allLogbookRecords' :
                this.hideAllBlocks();
                $('#allLogbookRecordsBlock').show();
                $('#allLogbookRecords').addClass('panel-menu-item-active');
                currentLogbookController = this.allFeedController;
                break;
        }
        logbook_search_controller.init(currentLogbookController);
        currentLogbookController.start();
    },

    hideAllBlocks: function () {
        $('.panel-menu-item').removeClass('panel-menu-item-active');
        this.myFeedController.stop();
        this.myFriendsFeedController.stop();
        this.allFeedController.stop();
        $('#myLogbookRecordsBlock').hide();
        $('#myFriendsLogbookRecordsBlock').hide();
        $('#allLogbookRecordsBlock').hide();
    },

    setListeners: function () {
        var self = this;
        $('#myLogbookRecords').click(function () {
            self.setList('myLogbookRecords');
        });
        $('#myFriendsLogbookRecords').click(function () {
            self.setList('myFriendsLogbookRecords');
        });
        $('#allLogbookRecords').click(function () {
            self.setList('allLogbookRecords');
        });

        $('#createLogbookEntryButton').click(function () {
            window.location = "/secure/createLogbookRecordForm.html";
        });
    }
};

$(document).ready(function () {
    logbook_controller.init();
});
