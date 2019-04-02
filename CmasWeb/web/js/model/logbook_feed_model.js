var logbook_feed_model = {

    resetFields: function () {
        this.isFirstLoad = true;
        this.isMyRecords = false;
        this.containerId = "";
        this.url = "";
        this.templateName = "logbookFeed";
        this.latestDate = "0";
        this.earliestDate = "";
        this.lastElemId = null;
        this.limit = 10;
        this.deleteRecordId = 0;

        this.fromDate = "";
        this.toDate = "";
        this.country = "";
        this.fromMeters = "";
        this.toMeters = "";
        this.fromMinutes = "";
        this.toMinutes = "";
    },

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

    resetFeed: function () {
        this.latestDate = "0";
        this.earliestDate = "";
        this.lastElemId = null;
    },

    getNewRecords: function (successHandler, unSuccessHandler) {
        var self = this;
        var form = {
            fromDateTimestamp: self.latestDate, toDateTimestamp: "", limit: self.limit
        };

        this.getRecords(form, successHandler, unSuccessHandler);
    },

    getOldRecords: function (successHandler, unSuccessHandler) {
        var self = this;
        var form = {
            fromDateTimestamp: "", toDateTimestamp: self.earliestDate, limit: self.limit
        };

        this.getRecords(form, successHandler, unSuccessHandler);
    },

    searchForRecords: function (successHandler, unSuccessHandler) {
        var self = this;
        var form = {
            country: self.country,
            fromDate: self.fromDate,
            toDate: self.toDate,
            fromMeters: self.fromMeters,
            toMeters: self.toMeters,
            fromMinutes: self.fromMinutes,
            toMinutes: self.toMinutes,
            limit: this.limit
        };

        this.getRecords(form, successHandler, unSuccessHandler);
    },

    getRecords: function (form, successHandler, unSuccessHandler) {
        var self = this;
        basicClient.sendGetRequest(this.isFirstLoad, self.url, form, function (json) {
                self.setState(json);
                successHandler({
                    records: json,
                    isMyRecords: self.isMyRecords,
                    containerId: self.containerId
                });
                if (self.isFirstLoad) {
                    self.isFirstLoad = false;
                }
            },
            function (json) {
                if (self.isFirstLoad) {
                    self.isFirstLoad = false;
                }
                unSuccessHandler(json);
            },
            function () {
                if (self.isFirstLoad) {
                    self.isFirstLoad = false;
                }
            }
        );
    },

    deleteRecord: function (successHandler, unSuccessHandler) {
        basicClient.sendGetRequestCommonCase(
            "/secure/deleteRecord.html",
            {logbookEntryId: this.deleteRecordId},
            successHandler,
            unSuccessHandler,
            function () {
                window.location.reload();
            }
        );
    }
};