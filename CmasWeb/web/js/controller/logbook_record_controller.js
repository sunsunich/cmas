var logbook_record_controller = {

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        var tabName = logbook_record_model.logbookEntry.state;
        if (!tabName) {
            tabName = 'NEW';
        }

        if (tabName == 'NEW') {
            $('#tabs').hide();
        }
        else {
            $('#tabs').show();
        }

        self.showTab(tabName);
        $('#diveProfileTab').click(function () {
            self.showTab('NEW_SAVED');
        });
        $('#publishTab').click(function () {
            self.showTab('SAVED');
        });

        $('#chooseSpot').click(function (e) {
            e.preventDefault();
            self.buildLogbookEntryForm();
            logbook_record_diveProfile_controller.maskInvalidValues();
            logbook_record_model.createDraftRecord(
                function (json) {
                    window.location = "/secure/showSpots.html?logbookEntryId=" + json.id;
                }, function () {
                    error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
                }
            );
        });

        $('#saveDraftSuccessClose').click(function () {
            window.location = "/secure/editLogbookRecordForm.html?logbookEntryId=" + logbook_record_model.logbookEntry.id;
        });
        $('#saveDraftSuccessOk').click(function () {
            window.location = "/secure/editLogbookRecordForm.html?logbookEntryId=" + logbook_record_model.logbookEntry.id;
        });

        $('#submitSuccessClose').click(function () {
            window.location = "/secure/editLogbookRecordForm.html?logbookEntryId=" + logbook_record_model.logbookEntry.id;
        });
        $('#submitSuccessOk').click(function () {
            window.location = "/secure/editLogbookRecordForm.html?logbookEntryId=" + logbook_record_model.logbookEntry.id;
        });

        $('#saveDraftLogbookEntryButton').click(function () {
            self.saveDraft();
        });
        $('#saveLogbookEntryButton').click(function () {
            self.saveLogbookEntry('SAVED');
        });
        $('#publishLogbookEntryButton').click(function () {
            self.saveLogbookEntry('PUBLISHED');
        });
    },

    showTab: function (tabName) {
        if (tabName == 'NEW') {
            $('#publish').hide();
            $('#diveProfile').show();
        } else if (tabName == 'NEW_SAVED') {
            $('#publishTab').addClass('inactive');
            $('#diveProfileTab').removeClass('inactive');
            $('#publish').hide();
            $('#diveProfile').show();
        } else {
            $('#diveProfileTab').addClass('inactive');
            $('#publishTab').removeClass('inactive');
            $('#diveProfile').hide();
            $('#publish').show();
        }
    },

    buildLogbookEntryForm: function () {
        logbook_record_diveProfile_controller.buildLogbookEntryForm();
        logbook_record_publish_controller.buildLogbookEntryForm();
    },

    saveDraft: function () {
        this.buildLogbookEntryForm();
        logbook_record_diveProfile_controller.maskInvalidValues();
        logbook_record_model.createDraftRecord(
            function (json) {
                logbook_record_model.logbookEntry.id = json.id;
                $('#saveDraftSuccess').show();
            }, function () {
                error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
            }
        );
    },

    saveLogbookEntry: function (futureState) {
        logbook_record_diveProfile_controller.buildLogbookEntryForm();
        this.cleanCreateFormErrors();
        var diveProfileFormErrors = logbook_record_diveProfile_controller.validateCreateForm();
        if (!diveProfileFormErrors.success) {
            validation_controller.showErrors('create', diveProfileFormErrors);
            if (logbook_record_model.logbookEntry.state == 'NEW') {
                this.showTab('NEW');
            } else {
                this.showTab('NEW_SAVED');
            }
        }
        var publishFormErrors = {"success": true};
        if (logbook_record_model.logbookEntry.state != 'NEW') {
            logbook_record_publish_controller.buildLogbookEntryForm();
            publishFormErrors = logbook_record_publish_controller.validateCreateForm();
            if (!publishFormErrors.success) {
                validation_controller.showErrors('create', publishFormErrors);
                if (diveProfileFormErrors.success) {
                    this.showTab(logbook_record_model.logbookEntry.state);
                }
            }
        }

        if (diveProfileFormErrors.success && publishFormErrors.success) {
            logbook_record_diveProfile_controller.maskInvalidValues();
            const oldState = logbook_record_model.logbookEntry.state;
            logbook_record_model.logbookEntry.state = futureState;
            logbook_record_model.createRecord(
                function (json) {
                    if (logbook_record_model.logbookEntry.state == "PUBLISHED") {
                        window.location = "/secure/showLogbook.html";
                    } else {
                        logbook_record_model.logbookEntry.id = json.id;
                        $('#submitSuccess').show();
                    }
                }
                , function (json) {
                    logbook_record_model.logbookEntry.state = oldState;
                    validation_controller.showErrors('create', json);
                });
        }
    },

    cleanCreateFormErrors: function () {
        $("#create_error").empty().hide();
        logbook_record_diveProfile_controller.cleanCreateFormErrors();
        logbook_record_publish_controller.cleanCreateFormErrors();
    }
};

$(document).ready(function () {
    logbook_record_controller.init();
});
