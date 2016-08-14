var logbook_feed_model = {

    isMyRecords: false,
    isShowSpec: true,
    containerId: "",
    url: "",
    latestDate: "",
    earliestDate: "",
    limit: 10,
    deleteRecordId: 0,

    setState: function (records) {
        if (records.length > 0) {
            if (compare_big_int(records[0].dateEdit, this.latestDate)) {
                this.latestDate = records[0].dateEdit;
            }
            if (compare_big_int(this.earliestDate, records[records.length - 1].dateEdit)) {
                this.earliestDate = records[records.length - 1].dateEdit;
            }
        }
    },

    getNewRecords: function (successHandler, errorHandler) {
        var form = {
            fromDateTimestamp: this.latestDate, toDateTimestamp: "", limit: this.limit
        };

        this.getRecords(form, successHandler, errorHandler);
    },

    getOldRecords: function (successHandler, errorHandler) {
        var form = {
            fromDateTimestamp: "", toDateTimestamp: this.earliestDate, limit: this.limit
        };

        this.getRecords(form, successHandler, errorHandler);
    },

    getRecords: function (form, successHandler, errorHandler) {
        var self = this;
        $.ajax({
            type: "GET",
            url: self.url,
            dataType: "json",
            data: form,
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    self.setState(json);
                    successHandler({
                        records: json,
                        isMyRecords: self.isMyRecords,
                        containerId: self.containerId,
                        isShowSpec: self.isShowSpec
                    });
                } else {
                    errorHandler(json);
                }
            },
            error: function (e) {
            }
        });
    },

    deletetRecord: function (successHandler, errorHandler) {
        loader_controller.startwait();
        var self = this;
        $.ajax({
            type: "GET",
            url: "/secure/deleteRecord.html",
            dataType: "json",
            data: {logbookEntryId: self.deleteRecordId},
            success: function (json) {
                var success = !json.hasOwnProperty('success') || json.success;
                if (success) {
                    successHandler();
                } else {
                    errorHandler(json);
                }
                loader_controller.stopwait();
            },
            error: function (e) {
                window.location.reload();
            }
        });
    }
};