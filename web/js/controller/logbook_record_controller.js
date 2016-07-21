var logbook_record_controller = {

    init: function () {
        country_controller.inputId = "findDiverCountry";
        country_controller.drawIcon = false;
        country_controller.init();
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $("#diveDate").datepicker(
            {
                changeYear: true,
                changeMonth: true,
                yearRange: '-10:+0',
                maxDate: new Date(),
                defaultDate: new Date(),
                dateFormat: 'dd/mm/yy'
            }
        );
        var option = '';
        for (var i = 0; i < visibilityTypes.length; i++) {
            option += '<option value="' + visibilityTypes[i] + '"';
            if (visibilityTypes[i] == logbookVisibility) {
                option += ' selected="selected"';
            }
            option += '>' + labels[visibilityTypes[i]] + '</option>';
        }
        $("#visibility").append(option);
        $("#visibility").select2({
            escapeMarkup: function (m) {
                return m;
            }
        });
        $('#duration').keypress(function (event) {
            var value = $(this).val();
            if (event.charCode < 48 || event.charCode > 57 || (value && value.length >= 3)) {
                event.preventDefault();
            }
        });

        $('#depth').keypress(function (event) {
            var value = $(this).val();
            if (event.charCode < 48 || event.charCode > 57 || (value && value.length >= 4)) {
                event.preventDefault();
            }
        });

        $("#fileFromDiscSelect").click(function (e) {
            e.preventDefault();
            self.selectPhotoFromDrive();
        });
        $("#photoFileInput").change(function () {
            self.loadFileToPreview(this);
        });

        $(".diveScore").hover(function () {
                self.colorDiveScore($(this)[0].id);
            }, function () {
                self.restoreDiveScore();
            }
        ).click(function () {
                self.setDiveScore($(this)[0].id);
            });

        $('#createLogbookEntryButton').click(function () {
            self.createLogbookEntry();
        });

        $('#viewBuddies').click(function (e) {
            e.preventDefault();
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
            $('#mainContent').show();
            $('#findDiver').show();
        });

        $('#friendRemoveClose').click(function () {
            $('#friendRemove').hide();
        });
        $('#friendRemoveOk').click(function () {

        });
        $('#showDiverClose').click(function () {
            $('#showDiver').hide();
        });
        $('#showDiverOk').click(function () {
            $('#showDiver').hide();
        });
    },

    colorDiveScore: function (elemId) {
        var scoreInt = parseInt(elemId.split('_')[1]);
        for (var i = 1; i <= scoreInt; i++) {
            $('#diveScore_' + i).attr('src', '/i/heart-full.png');
        }
    },

    restoreDiveScore: function () {
        for (var i = 1; i <= logbook_record_model.scoreInt; i++) {
            $('#diveScore_' + i).attr('src', '/i/heart-full.png');
        }
        for (i = logbook_record_model.scoreInt + 1; i <= 5; i++) {
            $('#diveScore_' + i).attr('src', '/i/heart-empty.png');
        }
    },

    setDiveScore: function (elemId) {
        var scoreInt = parseInt(elemId.split('_')[1]);
        logbook_record_model.scoreInt = scoreInt;
        switch (scoreInt) {
            case 1:
                logbook_record_model.score = 'ONE_STAR';
                break;
            case 2:
                logbook_record_model.score = 'TWO_STAR';
                break;
            case 3:
                logbook_record_model.score = 'THREE_STAR';
                break;
            case 4:
                logbook_record_model.score = 'FOUR_STAR';
                break;
            case 5:
                logbook_record_model.score = 'FIVE_STAR';
                break;
        }
        this.restoreDiveScore();
    },


    createLogbookEntry: function () {
        var imageData = $('#logbookPic').attr("src");
        if (imageData) {
            if (imageData.indexOf('base64') == -1) {
                imageData = null;
            }
            else {
                imageData = imageData.substring(imageData.indexOf(',') + 1);
            }
        }
        var createForm = {
            spotId: logbook_record_model.spotId,
            duration: $('#duration').val(),
            depth: $('#depth').val(),
            diveDate: $('#diveDate').val(),
            score: logbook_record_model.score,
            visibility: $('#visibility').val(),
            note: $('#comments').val(),
            instructorId: logbook_record_model.instructorId,
            buddiesIds: logbook_record_model.buddiesIds,
            photo: imageData
        };

        this.cleanCreateFormErrors();
        var formErrors = this.validateCreateForm(createForm);
        if (formErrors.success) {
            logbook_record_model.createRecord(
                createForm
                , function (/*json*/) {
                    window.location = "/secure/showLogbook.html";
                }
                , function (json) {
                    validation_controller.showErrors('create', json);
                });
        }
        else {
            validation_controller.showErrors('create', formErrors);
        }
    },

    validateCreateForm: function (regForm) {
        var result = {};
        result.fieldErrors = {};
        result.errors = {};
        if (isStringTrimmedEmpty(regForm.duration)) {
            result.fieldErrors["duration"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(regForm.depth)) {
            result.fieldErrors["depth"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(regForm.diveDate)) {
            result.fieldErrors["diveDate"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(regForm.visibility)) {
            result.fieldErrors["visibility"] = 'validation.emptyField';
        }

        result.success = jQuery.isEmptyObject(result.fieldErrors) && jQuery.isEmptyObject(result.errors);

        return result;
    },

    cleanCreateFormErrors: function () {
        $("#create_error").empty().hide();
        $('#create_error_diveDate').empty();
        $('#create_error_visibility').empty();
        $('#create_error_photo').empty();
        $('#create_error_duration').empty();
        $('#create_error_depth').empty();
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
        var findDiverForm = {
            diverType: $("input[name=findDiverType]:checked").val(),
            country: $('#findDiverCountry').val(),
            name: $('#findDiverName').val()
        };

        this.cleanFindDiverErrors();
        var formErrors = this.validateFindDiverForm(findDiverForm);
        if (formErrors.success) {
            social_model.searchNewFriends(
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

        result.success = jQuery.isEmptyObject(result.fieldErrors) && jQuery.isEmptyObject(result.errors);
        return result;
    },

    cleanFindDiverForm: function () {
        $("input[name=findDiverType]:checked").attr('checked', false);
        $('#findDiverCountry').select2("val", '');
        $('#findDiverName').val('');
        this.cleanFindDiverErrors();
    },

    cleanFindDiverErrors: function () {
        $("#findDiver_error").empty().hide();
        $('#findDiver_error_diverType').empty();
        $('#findDiver_error_country').empty();
        $('#findDiver_error_name').empty();
    },

    showDiver: function (elemId) {
        var diverId = elemId.split('_')[0];
        social_model.getDiver(
            diverId
            , function (json) {
                $('#showDiverContent').html(
                    new EJS({url: '/js/templates/diverDialog.ejs'}).render({"diver": json})
                );
                $('#showDiver').show();
            }
            , function (json) {
                if (json && json.hasOwnProperty("message")) {
                    error_dialog_controller.showErrorDialog(error_codes[json.message]);
                }
                else {
                    error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
                }
            });
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
                $('#' + divers[i].id + '_addFriend').click(function () {
                    self.addBuddie($(this)[0].id);
                });
            }
            $('#mainContent').hide();
            $('#diversList').show();
        } else {
            $('#noDiversFound').show();
        }
    },

    removeFriend: function (elemId) {
        var diverId = elemId.split('_')[0];
        social_model.removeFriendId = diverId;
        var diverName = $('#' + diverId + '_showFriendDiver').html();
        $('#removeDiverName').html(diverName);
        $('#friendRemove').show();
    },

    removeFriendRequest: function (elemId) {
        social_model.removeFriendRequestId = elemId.split('_')[0];
        $('#friendRequestRemove').show();
    },


};

$(document).ready(function () {
    logbook_record_controller.init();
});
