var logbook_feed_controller = {

    model: null,
    timeout: null,

    start: function () {
        var self = this;
        this.refreshFeed();
        this.timeout = setTimeout(function run() {
            self.refreshFeed();
            self.timeout = setTimeout(run, 3000);
        }, 3000);
    },

    stop: function () {
        if (this.timeout) {
            clearTimeout(this.timeout);
            this.timeout = null;
        }
    },

    refreshFeed: function () {
        var self = this;
        this.model.getNewRecords(
            function (recordsInfo) {
                var records = recordsInfo.records;
                for (var i = 0; i < records.length; i++) {
                    $('#' + self.model.containerId + 'logbookRecord_' + records[i].id).remove();
                }
                $('#' + self.model.containerId).prepend(
                    new EJS({url: '/js/templates/logbookFeed.ejs'}).render({"recordsInfo": recordsInfo})
                );
                for (i = 0; i < records.length; i++) {
                    $('#' + self.model.containerId + 'more_' + records[i].id).click(function (event) {
                        event.preventDefault();
                        self.openMoreOnRecord($(this)[0].id);
                    });
                    $('#' + self.model.containerId + 'less_' + records[i].id).click(function (event) {
                        event.preventDefault();
                        self.closeMoreOnRecord($(this)[0].id);
                    });
                    $('#' + self.model.containerId + 'edit_' + records[i].id).click(function (event) {
                        event.preventDefault();
                        var elemId = $(this)[0].id;
                        window.location = "/secure/createRecordForm.html?recordId=" + elemId.split('_')[1];
                    });
                }
            },
            function (json) {
                if (json && json.hasOwnProperty("message")) {
                    error_dialog_controller.showErrorDialog(error_codes[json.message]);
                }
                else {
                    error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
                }
            }
        )
    },

    openMoreOnRecord: function (elemId) {
        var recordId = elemId.split('_')[1];
        $('#' + this.model.containerId + 'more_' + recordId).hide();
        $('#' + this.model.containerId + 'invisibleNote_' + recordId).show();
        $('#' + this.model.containerId + 'less_' + recordId).show();
    },

    closeMoreOnRecord: function (elemId) {
        var recordId = elemId.split('_')[1];
        $('#' + this.model.containerId + 'more_' + recordId).show();
        $('#' + this.model.containerId + 'invisibleNote_' + recordId).hide();
        $('#' + this.model.containerId + 'less_' + recordId).hide();
    }
};