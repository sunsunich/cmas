var logbook_feed_controller = {

    resetFields: function () {
        this.model = null;
        this.reloadInterval = null;
        this.scrollHandler = null;
    },

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
        this.refreshFeed();
        $(window).scroll(this.scrollHandler);
    },

    stop: function () {
        if (this.reloadInterval) {
            clearTimeout(this.reloadInterval);
            this.reloadInterval = null;
        }
        $(window).off('scroll', this.scrollHandler);
    },

    resetFeed: function () {
        this.stop();
        this.model.resetFeed();
        this.start();
    },

    refreshFeed: function () {
        var self = this;
        this.model.getNewRecords(
            function (recordsInfo) {
                self.renderFeed(recordsInfo, true, false);
                self.resetReloadInterval();
            },
            function (json) {
                if (json && json.hasOwnProperty("message")) {
                    error_dialog_controller.showErrorDialog(error_codes[json.message]);
                }
                else {
                    error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
                }
                self.resetReloadInterval();
            }
        )
    },

    resetReloadInterval: function () {
        var self = this;
        this.reloadInterval = setTimeout(function run() {
            self.refreshFeed();
        }, 3000);
    },

    getOldRecords: function () {
        var self = this;
        this.model.getOldRecords(
            function (recordsInfo) {
                self.renderFeed(recordsInfo, false, false);
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

    renderFeed: function (recordsInfo, isNew, isReplace) {
        var self = this;
        var records = recordsInfo.records;
        if (isReplace) {
            $('#' + self.model.containerId).html(
                new EJS({url: '/js/templates/' + self.model.templateName + '.ejs?v=' + webVersion}).render({
                        "recordsInfo": recordsInfo,
                        "webVersion": webVersion,
                        "imagesData": imagesData
                    }
                )
            )
        } else {
            for (var i = 0; i < records.length; i++) {
                var record = records[i];
                $('#' + self.model.containerId + 'logbookRecord_' + record.id).remove();
            }
            if (isNew) {
                $('#' + self.model.containerId).prepend(
                    new EJS({url: '/js/templates/' + self.model.templateName + '.ejs?v=' + webVersion}).render({
                        "recordsInfo": recordsInfo,
                        "webVersion": webVersion,
                        "imagesData": imagesData
                    })
                );
            }
            else {
                $('#' + self.model.containerId).append(
                    new EJS({url: '/js/templates/' + self.model.templateName + '.ejs?v=' + webVersion}).render({
                        "recordsInfo": recordsInfo,
                        "webVersion": webVersion,
                        "imagesData": imagesData
                    })
                );
            }
        }
        for (i = 0; i < records.length; i++) {
            record = records[i];
            if (self.model.isMyRecords) {
                $('#' + self.model.containerId + 'logbookRecord_' + record.id).hover(
                    function () {
                        var elemId = $(this)[0].id;
                        $('#' + self.model.containerId + 'logbookRecordButtons_' + elemId.split('_')[1]).show();
                    }, function () {
                        var elemId = $(this)[0].id;
                        $('#' + self.model.containerId + 'logbookRecordButtons_' + elemId.split('_')[1]).hide();
                    }
                );
                $('#' + self.model.containerId + 'edit_' + record.id).click(function (event) {
                    event.preventDefault();
                    var elemId = $(this)[0].id;
                    window.location = "/secure/editLogbookRecordForm.html?logbookEntryId=" + elemId.split('_')[1];
                });
                $('#' + self.model.containerId + 'delete_' + record.id).click(function (event) {
                    event.preventDefault();
                    var elemId = $(this)[0].id;
                    self.showRecordDeleteDialog(elemId);
                });
                $('#' + self.model.containerId + 'specMore_' + record.id).click(function (event) {
                    event.preventDefault();
                    self.openFullSpecOnRecord($(this)[0].id);
                });
                $('#' + self.model.containerId + 'specLess_' + record.id).click(function (event) {
                    event.preventDefault();
                    self.closeFullSpecOnRecord($(this)[0].id);
                });
                $('#' + self.model.containerId + 'tanksMore_' + record.id).click(function (event) {
                    event.preventDefault();
                    self.openTanksOnRecord($(this)[0].id);
                });
                $('#' + self.model.containerId + 'tanksLess_' + record.id).click(function (event) {
                    event.preventDefault();
                    self.closeTanksOnRecord($(this)[0].id);
                });
            } else {
                $('#' + self.model.containerId + 'SpecOpen_' + record.id).click(function (event) {
                    event.preventDefault();
                    self.openSpecOnRecord($(this)[0].id);
                });
                $('#' + self.model.containerId + 'SpecClose_' + record.id).click(function (event) {
                    event.preventDefault();
                    self.closeSpecOnRecord($(this)[0].id);
                });
            }
            $('#' + self.model.containerId + 'more_' + record.id).click(function (event) {
                event.preventDefault();
                self.openMoreOnRecord($(this)[0].id);
            });
            $('#' + self.model.containerId + 'less_' + record.id).click(function (event) {
                event.preventDefault();
                self.closeMoreOnRecord($(this)[0].id);
            });
            if (record.instructor) {
                $('#' + record.instructor.id + '_' + self.model.containerId + '_' + record.id + '_showDiver').click(function (e) {
                    e.preventDefault();
                    util_controller.showDiver($(this)[0].id);
                });
            }
            if (record.buddies) {
                for (var j = 0; j < record.buddies.length; j++) {
                    var buddy = record.buddies[j];
                    $('#' + buddy.id + '_' + self.model.containerId + '_' + record.id + '_showDiver').click(function (e) {
                        e.preventDefault();
                        util_controller.showDiver($(this)[0].id);
                    });
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
        this.model.deleteRecord(
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

    openFullSpecOnRecord: function (elemId) {
        var recordId = elemId.split('_')[1];
        $('#' + this.model.containerId + 'specMore_' + recordId).hide();
        $('#' + this.model.containerId + 'invisibleSpec_' + recordId).show();
        $('#' + this.model.containerId + 'specLess_' + recordId).show();
    },

    closeFullSpecOnRecord: function (elemId) {
        var recordId = elemId.split('_')[1];
        $('#' + this.model.containerId + 'specMore_' + recordId).show();
        $('#' + this.model.containerId + 'invisibleSpec_' + recordId).hide();
        $('#' + this.model.containerId + 'specLess_' + recordId).hide();
    },

    openTanksOnRecord: function (elemId) {
        var recordId = elemId.split('_')[1];
        $('#' + this.model.containerId + 'tanksMore_' + recordId).hide();
        $('#' + this.model.containerId + 'invisibleTanks_' + recordId).show();
        $('#' + this.model.containerId + 'tanksLess_' + recordId).show();
    },

    closeTanksOnRecord: function (elemId) {
        var recordId = elemId.split('_')[1];
        $('#' + this.model.containerId + 'tanksMore_' + recordId).show();
        $('#' + this.model.containerId + 'invisibleTanks_' + recordId).hide();
        $('#' + this.model.containerId + 'tanksLess_' + recordId).hide();
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