var logbook_record_publish_controller = {

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        util_controller.setupSelect2(
            'visibility', logbook_record_model.visibilityTypes, logbook_record_model.logbookEntry.visibility,
            labels["cmas.face.logbook.visibility"]
        );

        $("#fileFromDiscSelect").click(function () {
            self.selectPhotoFromDrive();
        });
        $("#photoFileInput").change(function () {
            self.loadFileToPreview(this);
        });

        $('#addBuddies').click(function (e) {
            e.preventDefault();
            fast_search_divers_controller.clean(false);
            $('#findDiver').show();
            $('#overlay').show();
        });
        $('#addInstructor').click(function (e) {
            e.preventDefault();
            fast_search_divers_controller.clean(true);
            $('#findDiver').show();
            $('#overlay').show();
        });
        $("#findDiverClose").click(function () {
            $('#findDiver').hide();
            $('#overlay').hide();
        });
        $("#cancelFindDivers").click(function () {
            $('#findDiver').hide();
            $('#overlay').hide();
        });
        $("#addDivers").click(function () {
            self.addDivers();
            $('#findDiver').hide();
            $('#overlay').hide();
        });
        $('#friendRemoveClose').click(function () {
            $('#friendRemove').hide();
        });
        $('#friendRemoveOk').click(function () {

        });
        this.setBuddies();
        this.setInstructor();
    },

    buildLogbookEntryForm: function () {
        logbook_record_model.logbookEntry.visibility = $('#visibility').val();

        logbook_record_model.logbookEntry.instructor = null;
        if (logbook_record_model.instructorId) {
            logbook_record_model.logbookEntry.instructor = {"id": logbook_record_model.instructorId};
        }
        logbook_record_model.logbookEntry.buddies = [];
        for (var i = 0; i < logbook_record_model.buddiesIds.length; i++) {
            var buddieId = logbook_record_model.buddiesIds[i];
            if (buddieId) {
                logbook_record_model.logbookEntry.buddies.push({"id": buddieId});
            }
        }
    },

    validateCreateForm: function () {
        var result = {};
        result.fieldErrors = {};
        result.errors = {};
        if (isStringTrimmedEmpty(logbook_record_model.logbookEntry.visibility)) {
            result.fieldErrors["visibility"] = 'validation.emptyField';
        }

        result.success = jQuery.isEmptyObject(result.fieldErrors) && jQuery.isEmptyObject(result.errors);

        return result;
    },

    cleanCreateFormErrors: function () {
        $('#create_error_visibility').empty();
        $('#create_error_photo').empty();
    },

    selectPhotoFromDrive: function () {
        $('#photoFileInput').trigger('click');
    },

    loadFileToPreview: function (input) {
        try {
            if (!this.validateFile(input)) {
                return;
            }
            var file = input.files[0];
            var reader = new FileReader();
            reader.onload = function (e) {
                $('#logbookEntryPhoto').attr('src', e.target.result);
            };
            reader.readAsDataURL(file);
        }
        catch (err) {
            $('#logbookEntryPhoto').hide();
            $('#photoFileInput').show();
        }
    },

    validateFile: function (input) {
        var createErrorPhotoElem = $('#create_error_photo');
        createErrorPhotoElem.html('');
        if (!(input.files && input.files[0])) {
            createErrorPhotoElem.html(error_codes["validation.emptyField"]);
            return false;
        }
        var file = input.files[0];
        if (file.name.length < 1) {
            createErrorPhotoElem.html(error_codes["validation.emptyField"]);
            return false;
        }
        else if (file.size > 10 * 1024 * 1024) {
            createErrorPhotoElem.html(error_codes["validation.logbookImageSize"]);
            return false;
        }
        else if (file.type != 'image/png' && file.type != 'image/jpg' && file.type != 'image/gif' && file.type != 'image/jpeg') {
            createErrorPhotoElem.html(error_codes["validation.imageFormat"]);
            return false;
        }
        return true;
    },

    setBuddies: function () {
        var self = this;
        logbook_record_model.getDivers(
            logbook_record_model.buddiesIds,
            function (json) {
                if (json.length == 0) {
                    $('#buddiesList').empty();
                }
                else {
                    $('#buddiesList').html(
                        new EJS({url: '/js/templates/buddiesList.ejs?v=' + webVersion}).render({"divers": json, "webVersion" : webVersion})
                    );
                    for (var i = 0; i < json.length; i++) {
                        $('#' + json[i].id + '_showDiver').click(function (e) {
                            e.preventDefault();
                            util_controller.showDiver($(this)[0].id);
                        });
                        $('#' + json[i].id + '_removeBuddie').click(function () {
                            self.removeBuddie($(this)[0].id, false);
                        });
                    }
                }
            },
            function () {
            }
        );
    },

    setInstructor: function () {
        var self = this;
        var diverArray;
        if (logbook_record_model.instructorId == "") {
            diverArray = [];
        }
        else {
            diverArray = [logbook_record_model.instructorId];
        }
        logbook_record_model.getDivers(
            diverArray,
            function (json) {
                if (json.length == 0) {
                    $('#instructorContainer').empty();
                }
                else {
                    $('#instructorContainer').html(
                        new EJS({url: '/js/templates/buddiesList.ejs?v=' + webVersion}).render({"divers": json, "webVersion" : webVersion})
                    );
                    for (var i = 0; i < json.length; i++) {
                        $('#' + json[i].id + '_showDiver').click(function (e) {
                            e.preventDefault();
                            util_controller.showDiver($(this)[0].id);
                        });
                        $('#' + json[i].id + '_removeBuddie').click(function () {
                            self.removeBuddie($(this)[0].id, true);
                        });
                    }
                }
            },
            function () {
            }
        );
    },

    addDivers: function () {
        if (fast_search_divers_controller.isInstructorSearch) {
            logbook_record_model.instructorId = fast_search_divers_controller.listSelectedDiverIds()[0];
            this.setInstructor();
        } else {
            for (var i = 0; i < logbook_record_model.buddiesIds.length; i++) {
                var buddieId = logbook_record_model.buddiesIds[i];
                fast_search_divers_controller.selectedDiverIds[buddieId] = true;
            }
            logbook_record_model.buddiesIds = fast_search_divers_controller.listSelectedDiverIds();
            this.setBuddies();
        }
    },

    removeBuddie: function (elemId, isInstructor) {
        var diverId = elemId.split('_')[0];
        if (isInstructor) {
            logbook_record_model.instructorId = "";
            this.setInstructor();
        }
        else {
            logbook_record_model.buddiesIds = removeFromArray(logbook_record_model.buddiesIds, diverId);
            this.setBuddies();
        }
    }
};
