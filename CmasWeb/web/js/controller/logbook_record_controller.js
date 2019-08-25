var logbook_record_controller = {

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        logbook_record_diveProfile_controller.init();
        logbook_record_publish_controller.init();
        var tabName = 'Required';
        for (var i = 0; i < logbook_record_model.additionalFieldIds.length; i++) {
            if ($('#' + logbook_record_model.additionalFieldIds[i]).val()) {
                tabName = 'Certification';
                break;
            }
        }
        if (logbook_record_model.instructorId) {
            tabName = 'Certification';
        }
        if ($('#hasSafetyStop').prop("checked")) {
            tabName = 'Certification';
        }
        this.showTab(tabName);

        $('#requiredTab').click(function () {
            self.showTab('Required');
        });
        $('#certificationTab').click(function () {
            self.showTab('Certification');
        });

        $('#chooseSpot').click(function (e) {
            e.preventDefault();
            self.buildLogbookEntryForm();
            logbook_record_diveProfile_controller.maskInvalidValues();
            logbook_record_model.createDraftRecord(
                function () {
                    window.location = "/secure/showSpots.html?logbookEntryId=" + logbook_record_model.logbookEntry.id;
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
            window.location = "/secure/showLogbook.html";
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
        $('#deletePhoto').click(function () {
            self.deletePhoto();
        });
    },

    showTab: function (tabName) {
        if (tabName == 'Required') {
            $('#requiredTab').addClass("logbook-tab-chosen").removeClass("logbook-tab");
            $('#certificationTab').addClass("logbook-tab").removeClass("logbook-tab-chosen");

            $('#prevDiveDate_container').hide();
            $('#avgDepthMeters_container').hide();
            for (var i = 0; i < logbook_record_model.additionalFieldIds.length; i++) {
                $('#' + logbook_record_model.additionalFieldIds[i]).val(null).trigger('change');
            }
            $('#hasSafetyStop').prop('checked', false);
            $('label[for="hasSafetyStop"]').addClass("clr").removeClass('chk');
            $('#additional_container').hide();
            $('#instructor').hide();
            //todo remove instructor
        } else {
            $('#certificationTab').addClass("logbook-tab-chosen").removeClass("logbook-tab");
            $('#requiredTab').addClass("logbook-tab").removeClass("logbook-tab-chosen");

            $('#prevDiveDate_container').show();
            $('#avgDepthMeters_container').show();
            $('#additional_container').show();
            $('#instructor').show();
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
            function (/*json*/) {
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
            validation_controller.simpleShowErrors('create', diveProfileFormErrors);
        }
        var publishFormErrors = {"success": true};
        if (logbook_record_model.logbookEntry.state != 'NEW') {
            logbook_record_publish_controller.buildLogbookEntryForm();
            publishFormErrors = logbook_record_publish_controller.validateCreateForm();
            if (!publishFormErrors.success) {
                validation_controller.simpleShowErrors('create', publishFormErrors);
            }
        }

        if (diveProfileFormErrors.success && publishFormErrors.success) {
            logbook_record_diveProfile_controller.maskInvalidValues();
            const oldState = logbook_record_model.logbookEntry.state;
            logbook_record_model.logbookEntry.state = futureState;
            logbook_record_model.createRecord(
                function (/*json*/) {
                    if (logbook_record_model.logbookEntry.state == "PUBLISHED") {
                        window.location = "/secure/showLogbook.html";
                    } else {
                        $('#submitSuccess').show();
                    }
                }
                , function (json) {
                    logbook_record_model.logbookEntry.state = oldState;
                    validation_controller.simpleShowErrors('create', json);
                });
        }
    },

    deletePhoto: function () {
        if (logbook_record_model.logbookEntry.photoUrl) {
            logbook_record_model.deletePhotoFromRecord(
                function (/*json*/) {
                    $('#logbookEntryPhotoContainer').hide();
                }
                , function (/*json*/) {
                    error_dialog_controller.showErrorDialog(error_codes["error.logbook.deletePhotoFailed"]);
                });
        } else {
            $('#logbookEntryPhotoContainer').hide();
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
