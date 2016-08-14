var logbook_feed_controller = {

    model: null,
    timeout: null,
    scrollHandler: null,

    init: function () {
        var self = this;
        if (this.model.isMyRecords) {
            $('#recordDeleteDialogClose').click(function () {
                $('#recordDeleteDialog').hide();
            });
            $('#recordDeleteDialogOk').click(function () {
                self.doRecordDelete();
            });
        }
        this.scrollHandler = function () {
            self.handleScroll();
        }
    },

    start: function () {
        var self = this;
        this.refreshFeed();
        this.timeout = setTimeout(function run() {
            self.refreshFeed();
            self.timeout = setTimeout(run, 3000);
        }, 3000);
        $(window).scroll(this.scrollHandler);
    },

    stop: function () {
        if (this.timeout) {
            clearTimeout(this.timeout);
            this.timeout = null;
        }
        $(window).off('scroll', this.scrollHandler);
    },

    refreshFeed: function () {
        var self = this;
        this.model.getNewRecords(
            function (recordsInfo) {
                self.renderFeed(recordsInfo, true);
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

    getOldRecords: function () {
        var self = this;
        this.model.getOldRecords(
            function (recordsInfo) {
                self.renderFeed(recordsInfo, false);
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

    renderFeed: function (recordsInfo, isNew) {
        var self = this;
        var records = recordsInfo.records;
        for (var i = 0; i < records.length; i++) {
            $('#' + self.model.containerId + 'logbookRecord_' + records[i].id).remove();
        }
        if (isNew) {
            $('#' + self.model.containerId).prepend(
                new EJS({url: '/js/templates/logbookFeed.ejs'}).render({"recordsInfo": recordsInfo})
            );
        }
        else {
            $('#' + self.model.containerId).append(
                new EJS({url: '/js/templates/logbookFeed.ejs'}).render({"recordsInfo": recordsInfo})
            );
        }
        for (i = 0; i < records.length; i++) {
            $('#' + self.model.containerId + 'more_' + records[i].id).click(function (event) {
                event.preventDefault();
                self.openMoreOnRecord($(this)[0].id);
            });
            $('#' + self.model.containerId + 'less_' + records[i].id).click(function (event) {
                event.preventDefault();
                self.closeMoreOnRecord($(this)[0].id);
            });
            $('#' + self.model.containerId + 'feedItemSettings_' + records[i].id).click(function (event) {
                event.preventDefault();
                self.toggleRecordMenu($(this)[0].id);
            });
            $('#' + self.model.containerId + 'edit_' + records[i].id).click(function (event) {
                event.preventDefault();
                var elemId = $(this)[0].id;
                window.location = "/secure/editLogbookRecordForm.html?logbookEntryId=" + elemId.split('_')[1];
            });
            $('#' + self.model.containerId + 'delete_' + records[i].id).click(function (event) {
                event.preventDefault();
                var elemId = $(this)[0].id;
                self.showRecordDeleteDialog(elemId);
            });
            if (self.model.isShowSpec) {
                $('#' + self.model.containerId + 'SpecOpen_' + records[i].id).click(function (event) {
                    event.preventDefault();
                    self.openSpecOnRecord($(this)[0].id);
                });
                $('#' + self.model.containerId + 'SpecClose_' + records[i].id).click(function (event) {
                    event.preventDefault();
                    self.closeSpecOnRecord($(this)[0].id);
                });
                if (records[i].instructor) {
                    $('#' + records[i].instructor.id + '_' + self.model.containerId + '_showDiver').click(function (e) {
                        e.preventDefault();
                        util_controller.showDiver($(this)[0].id);
                    });
                }
                if (records[i].buddies) {
                    for (var j = 0; j < records[i].buddies.length; j++) {
                        $('#' + records[i].buddies[j].id + '_' + self.model.containerId + '_showDiver').click(function (e) {
                            e.preventDefault();
                            util_controller.showDiver($(this)[0].id);
                        });
                    }
                }
            }
        }
    },

    handleScroll: function () {
        var self = this;
        if (self.model.lastElemId) {
            var elem = $('#' + self.model.containerId + 'logbookRecord_' + self.model.lastElemId);
            var elemTop = elem.offset().top;
            var elemHeight = elem.outerHeight();
            var winHeight = $(window).height();
            var winSize = $(window).scrollTop();
            if (winSize > (elemTop + elemHeight - winHeight)) {
                self.getOldRecords();
            }
        }
    },

    doRecordDelete: function () {
        $('#recordDeleteDialog').hide();
        var self = this;
        this.model.deletetRecord(
            function () {
                $('#' + self.model.containerId + 'logbookRecord_' + self.model.deleteRecordId).remove();
                self.model.deleteRecordId = 0;
            },
            function (json) {
                if (json && json.hasOwnProperty("message")) {
                    error_dialog_controller.showErrorDialog(error_codes[json.message]);
                }
                else {
                    error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
                }
            }
        );
    },

    showRecordDeleteDialog: function (elemId) {
        this.model.deleteRecordId = elemId.split('_')[1];
        $('#recordDeleteDialog').show();
    },

    toggleRecordMenu: function (elemId) {
        var recordId = elemId.split('_')[1];
        var wasVisible = $('#' + this.model.containerId + 'menuItemSettings_' + recordId).is(":visible");
        $('#' + this.model.containerId + 'menuItemSettings_' + recordId).toggle();
        if (wasVisible) {
            $('#' + this.model.containerId + 'feedItemSettings_' + recordId).attr('src', "/i/arrow_white_ico.png");
        } else {
            $('#' + this.model.containerId + 'feedItemSettings_' + recordId).attr('src', "/i/arrow_up_white_ico.png");
        }
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
    },

    openSpecOnRecord: function (elemId) {
        var recordId = elemId.split('_')[1];
        $('#' + this.model.containerId + 'SpecOpen_' + recordId).hide();
        $('#' + this.model.containerId + 'Spec_' + recordId).show();
        $('#' + this.model.containerId + 'SpecClose_' + recordId).show();
    },

    closeSpecOnRecord: function (elemId) {
        var recordId = elemId.split('_')[1];
        $('#' + this.model.containerId + 'SpecOpen_' + recordId).show();
        $('#' + this.model.containerId + 'Spec_' + recordId).hide();
        $('#' + this.model.containerId + 'SpecClose_' + recordId).hide();
    }
};