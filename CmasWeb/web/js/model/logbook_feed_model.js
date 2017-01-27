var logbook_feed_model = {

    isFirstLoad: true,
    isMyRecords: false,
    isShowSpec: true,
    containerId: "",
    url: "",
    latestDate: "0",
    earliestDate: "",
    lastElemId: null,
    limit: 10,
    deleteRecordId: 0,

    setState: function (records) {
        if (records.length > 0) {
            if (compare_big_int(records[0].dateEdit, this.latestDate)) {
                this.latestDate = records[0].dateEdit;
            }
            var lastRecord = records[records.length - 1];
            if (this.earliestDate == "" || compare_big_int(this.earliestDate, lastRecord.dateEdit)) {
                this.earliestDate = lastRecord.dateEdit;
                this.lastElemId = lastRecord.id;
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
        if (this.isFirstLoad) {
            loader_controller.startwait();
        }
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

                    if (self.isFirstLoad) {
                        loader_controller.stopwait();
                        self.isFirstLoad = false;
                    }
                } else {
                    if (self.isFirstLoad) {
                        loader_controller.stopwait();
                        self.isFirstLoad = false;
                    }
                    errorHandler(json);
                }
            },
            error: function (e) {
                if (self.isFirstLoad) {
                    loader_controller.stopwait();
                    self.isFirstLoad = false;
                }
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