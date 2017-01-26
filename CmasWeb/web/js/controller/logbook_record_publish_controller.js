var logbook_record_publish_controller = {

    federationCountryController: {},

    init: function () {
        this.federationCountryController = simpleClone(country_controller);
        this.federationCountryController.inputId = "findDiverFederationCountry";
        this.federationCountryController.drawIcon = false;
        this.federationCountryController.init();
        country_controller.inputId = "findDiverCountry";
        country_controller.drawIcon = false;
        country_controller.init();
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        util_controller.setupSelect2(
            'visibility', logbook_record_model.visibilityTypes, logbook_record_model.logbookEntry.visibility,
            labels["cmas.face.logbook.visibility"]
        );

        $("#fileFromDiscSelect").click(function (e) {
            e.preventDefault();
            self.selectPhotoFromDrive();
        });
        $("#photoFileInput").change(function () {
            self.loadFileToPreview(this);
        });

        $('#addBuddies').click(function (e) {
            e.preventDefault();
            logbook_record_model.isInstructorSearch = false;
            self.cleanFindDiverForm();
            $('#findDiver').show();
        });
        $('#addInstructor').click(function (e) {
            e.preventDefault();
            logbook_record_model.isInstructorSearch = true;
            self.cleanFindDiverForm();
            $('#findDiver').show();
        });
        $("#findDiverClose").click(function () {
            $('#findDiver').hide();
        });
        $('#findDiverOk').click(function () {
            self.findDiver();
        });
        $("#noDiversFoundClose").click(function () {
            $('#noDiversFound').hide();
        });
        $('#noDiversFoundOk').click(function () {
            $('#noDiversFound').hide();
            $('#findDiver').show();
        });
        $('#newDiverSearch').click(function () {
            $('#diversList').hide();
            $('#createLogbookEntry').show();
            $('#findDiver').show();
        });
        $('#searchByName').click(function () {
            logbook_record_model.findDiverMode = 'NAME';
            $('#searchByName').hide();
            $('#searchByFedNumber').show();
            $('#searchByCmasCard').show();
            $('#findDiverByCmasCardForm').hide();
            $('#findDiverByFederationCardNumberForm').hide();
            $('#findDiverByNameForm').show();
        });
        $('#searchByFedNumber').click(function () {
            logbook_record_model.findDiverMode = 'FEDERATION';
            $('#searchByFedNumber').hide();
            $('#searchByName').show();
            $('#searchByCmasCard').show();
            $('#findDiverByCmasCardForm').hide();
            $('#findDiverByNameForm').hide();
            $('#findDiverByFederationCardNumberForm').show();
        });
        $('#searchByCmasCard').click(function () {
            logbook_record_model.findDiverMode = 'CMAS';
            $('#searchByCmasCard').hide();
            $('#searchByName').show();
            $('#searchByFedNumber').show();
            $('#findDiverByFederationCardNumberForm').hide();
            $('#findDiverByNameForm').hide();
            $('#findDiverByCmasCardForm').show();
        });

        $('#friendRemoveClose').click(function () {
            $('#friendRemove').hide();
        });
        $('#friendRemoveOk').click(function () {

        });
        this.setBuddies();
        this.setInstructor();
    },

    setBuddies: function () {
        var self = this;
        logbook_record_model.getDivers(
            logbook_record_model.buddiesIds,
            function (json) {
                if (json.length == 0) {
                    $('#buddies').hide();
                }
                else {
                    $('#buddies').show();
                    $('#buddiesList').html(
                        new EJS({url: '/js/templates/buddiesList.ejs'}).render({"divers": json})
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
                    $('#instructor').hide();
                }
                else {
                    $('#instructor').show();
                    $('#instructorContainer').html(
                        new EJS({url: '/js/templates/buddiesList.ejs'}).render({"divers": json})
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

    buildLogbookEntryForm: function () {
        var imageData = $('#logbookPic').attr("src");
        if (imageData) {
            if (imageData.indexOf('base64') == -1) {
                imageData = null;
            }
            else {
                imageData = imageData.substring(imageData.indexOf(',') + 1);
            }
        }
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

        logbook_record_model.logbookEntry.photoBase64 = imageData;
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
                $('#logbookPic').attr('src', e.target.result);
            };
            reader.readAsDataURL(file);
        }
        catch (err) {
            $('#logbookPic').hide();
            $('#photoFileInput').show();
        }
    },

    validateFile: function (input) {
        $('#create_error_photo').html('');
        if (!(input.files && input.files[0])) {
            $('#create_error_photo').html(error_codes["validation.emptyField"]);
            return false;
        }
        var file = input.files[0];
        if (file.name.length < 1) {
            $('#create_error_photo').html(error_codes["validation.emptyField"]);
            return false;
        }
        else if (file.size > 1048576) {
            $('#create_error_photo').html(error_codes["validation.logbookImageSize"]);
            return false;
        }
        else if (file.type != 'image/png' && file.type != 'image/jpg' && file.type != 'image/gif' && file.type != 'image/jpeg') {
            $('#create_error_photo').html(error_codes["validation.imageFormat"]);
            return false;
        }
        return true;
    },

    findDiver: function () {
        var self = this;
        var findDiverForm = {};
        switch (logbook_record_model.findDiverMode) {
            case 'NAME':
                findDiverForm.diverType = logbook_record_model.isInstructorSearch
                    ? "INSTRUCTOR"
                    : $("input[name=findDiverType]:checked").val();
                findDiverForm.country = $('#findDiverCountry').val();
                findDiverForm.name = $('#findDiverName').val();
                break;
            case 'FEDERATION':
                findDiverForm.federationCountry = $('#findDiverFederationCountry').val();
                findDiverForm.federationCardNumber = $('#findDiverFederationCardNumber').val();
                break;
            case 'CMAS':
                findDiverForm.cmasCardNumber = $('#findDiverCmasCardNumber').val();
                break;
        }

        this.cleanFindDiverErrors();
        var formErrors = this.validateFindDiverForm(findDiverForm);
        if (formErrors.success) {
            logbook_record_model.searchDivers(
                findDiverForm
                , function (json) {
                    self.showFoundDivers(json);
                }
                , function (json) {
                    validation_controller.showErrors('findDiver', json);
                });
        }
        else {
            validation_controller.showErrors('findDiver', formErrors);
        }
    },

    validateFindDiverForm: function (form) {
        var result = {};
        result.fieldErrors = {};
        result.errors = [];
        switch (logbook_record_model.findDiverMode) {
            case 'NAME':
                if (isStringTrimmedEmpty(form.diverType)) {
                    result.fieldErrors["diverType"] = 'validation.diverTypeEmpty';
                }
                if (isStringTrimmedEmpty(form.country)) {
                    result.fieldErrors["country"] = 'validation.emptyField';
                }
                if (isStringTrimmedEmpty(form.name)) {
                    result.fieldErrors["name"] = 'validation.emptyField';
                }
                else if (form.name.length < 3) {
                    result.fieldErrors["name"] = 'validation.searchNameTooShort';
                }
                break;
            case 'FEDERATION':
                if (isStringTrimmedEmpty(form.federationCountry)) {
                    result.fieldErrors["federationCountry"] = 'validation.emptyField';
                }
                if (isStringTrimmedEmpty(form.federationCardNumber)) {
                    result.fieldErrors["federationCardNumber"] = 'validation.emptyField';
                }
                break;
            case 'CMAS':
                if (isStringTrimmedEmpty(form.cmasCardNumber)) {
                    result.fieldErrors["cmasCardNumber"] = 'validation.emptyField';
                }
                break;
        }


        result.success = jQuery.isEmptyObject(result.fieldErrors) && jQuery.isEmptyObject(result.errors);
        return result;
    },

    cleanFindDiverForm: function () {
        $("input[name=findDiverType]:checked").attr('checked', false);
        if (logbook_record_model.isInstructorSearch) {
            $('#findDiverTypeChoose').hide();
            $('#findDiverTitle').html(labels["cmas.face.findDiver.form.page.title.instructor"]);
        }
        else {
            $('#findDiverTitle').html(labels["cmas.face.findDiver.form.page.title"]);
            $('#findDiverTypeChoose').show();
        }
        $('#findDiverCountry').val('').trigger("change");
        $('#findDiverName').val('');
        $('#findDiverFederationCountry').val('').trigger("change");
        $('#findDiverFederationCardNumber').val('');
        $('#findDiverCmasCardNumber').val('');
        this.cleanFindDiverErrors();
    },

    cleanFindDiverErrors: function () {
        $("#findDiver_error").empty().hide();
        $('#findDiver_error_diverType').empty();
        $('#findDiver_error_country').empty();
        $('#findDiver_error_name').empty();
        $('#findDiver_error_federationCountry').empty();
        $('#findDiver_error_federationCardNumber').empty();
        $('#findDiver_error_cmasCardNumber').empty();
    },

    showFoundDivers: function (divers) {
        var self = this;
        $('#findDiver').hide();
        if (divers.length > 0) {
            $('#diversListContent').html(
                new EJS({url: '/js/templates/diversList.ejs'}).render({"divers": divers})
            );
            var i;
            for (i = 0; i < divers.length; i++) {
                var notification = null;
                if (divers[i].id == logbook_record_model.diverId) {
                    notification = error_codes["validation.cannotAddSelf"];
                }
                else if (logbook_record_model.buddiesIds.indexOf(divers[i].id) != -1) {
                    notification = error_codes["validation.buddieAlready"];
                }
                else if (logbook_record_model.instructorId == divers[i].id) {
                    notification = error_codes["validation.instructorAlready"];
                }
                if (notification == null) {
                    var addText;
                    if (logbook_record_model.isInstructorSearch) {
                        addText = labels["cmas.face.findDiver.add.instructor"];
                    }
                    else {
                        addText = labels["cmas.face.findDiver.add.buddie"];
                    }
                    $('#' + divers[i].id + '_addFriend').html(addText).click(function () {
                        self.addBuddie($(this)[0].id);
                    });
                }
                else {
                    $('#' + divers[i].id + '_addFriend').hide();
                    $('#' + divers[i].id + '_addFriendNotification').html(notification).show();
                }
            }
            $('#createLogbookEntry').hide();
            $('#diversList').show();
        } else {
            $('#noDiversFound').show();
        }
    },

    addBuddie: function (elemId) {
        var diverId = elemId.split('_')[0];
        if (logbook_record_model.isInstructorSearch) {
            logbook_record_model.instructorId = diverId;
            this.setInstructor();
        }
        else {
            logbook_record_model.buddiesIds.push(diverId);
            this.setBuddies();
        }
        $('#diversList').hide();
        $('#createLogbookEntry').show();
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

$(document).ready(function () {
    logbook_record_publish_controller.init();
});
