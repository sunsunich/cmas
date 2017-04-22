var logbook_record_diveProfile_controller = {

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        util_controller.setupDate('diveDate');
        util_controller.setupTime('diveTime');
        util_controller.setupDate('prevDiveDate');
        util_controller.setupTime('prevDiveTime');

        $('#latitude').keypress(function () {
            util_controller.filterFloatNumberChars($(this), 12, 12);
        });
        $('#longitude').keypress(function () {
            util_controller.filterFloatNumberChars($(this), 12, 12);
        });
        $('#airTemp').keypress(function () {
            util_controller.filterFloatNumberChars($(this), 2, 1);
        });
        $('#waterTemp').keypress(function () {
            util_controller.filterFloatNumberChars($(this), 2, 1);
        });
        $('#addWeight').keypress(function () {
            util_controller.filterPositiveFloatNumberChars($(this), 2, 1);
        });
        $('#duration').keypress(function (event) {
            util_controller.filterNumbers(event, $(this).val(), 3);
        });
        $('#depth').keypress(function (event) {
            util_controller.filterNumbers(event, $(this).val(), 3);
        });
        $('#avgDepth').keypress(function (event) {
            util_controller.filterNumbers(event, $(this).val(), 3);
        });
        $('#cnsToxicity').keypress(function () {
            util_controller.filterPositiveFloatNumberChars($(this), 3, 1);
        });
        util_controller.setupSelect2(
            'weather', logbook_record_model.weatherTypes, logbook_record_model.logbookEntry.diveSpec.weather,
            labels["cmas.face.logbook.weather"]
        );
        util_controller.setupSelect2(
            'surface', logbook_record_model.surfaceTypes, logbook_record_model.logbookEntry.diveSpec.surface,
            labels["cmas.face.logbook.surface"]
        );
        util_controller.setupSelect2(
            'current', logbook_record_model.currentTypes, logbook_record_model.logbookEntry.diveSpec.current,
            labels["cmas.face.logbook.current"]
        );
        util_controller.setupSelect2(
            'underWaterVisibility',
            logbook_record_model.underWaterVisibilityTypes,
            logbook_record_model.logbookEntry.diveSpec.underWaterVisibility,
            labels["cmas.face.logbook.underWaterVisibility"]
        );
        util_controller.setupSelect2(
            'waterType', logbook_record_model.waterTypes, logbook_record_model.logbookEntry.diveSpec.waterType,
            labels["cmas.face.logbook.waterType"]
        );
        util_controller.setupSelect2(
            'temperatureMeasureUnit',
            logbook_record_model.temperatureMeasureUnits,
            logbook_record_model.logbookEntry.diveSpec.temperatureMeasureUnit,
            labels["cmas.face.logbook.temperatureMeasureUnit"]
        );
        util_controller.setupSelect2(
            'divePurpose', logbook_record_model.divePurposeTypes, logbook_record_model.logbookEntry.diveSpec.divePurpose,
            labels["cmas.face.logbook.divePurpose"]
        );
        util_controller.setupSelect2(
            'entryType', logbook_record_model.entryTypes, logbook_record_model.logbookEntry.diveSpec.entryType,
            labels["cmas.face.logbook.entryType"]
        );
        util_controller.setupSelect2(
            'diveSuit', logbook_record_model.diveSuitTypes, logbook_record_model.logbookEntry.diveSpec.diveSuit,
            labels["cmas.face.logbook.diveSuit"]
        );
        if (logbook_record_model.logbookEntry.id) {
            this.restoreTanks();
        }
        if (logbook_record_model.logbookEntry.diveSpec.isApnea) {
            $('#tanksSection').hide();
        }
        else {
            $('#tanksSection').show();
        }
        $('#isApnea').change(
            function () {
                if ($(this).prop('checked')) {
                    $('#tanksSection').hide();
                } else {
                    $('#tanksSection').show();
                }
            }
        );
        $('#addTankButton').click(
            function () {
                var tank = {
                    "isDecoTank": false,
                    "size": "",
                    "volumeMeasureUnit": "",
                    "startPressure": "",
                    "endPressure": "",
                    "pressureMeasureUnit": "",
                    "supplyType": "",
                    "isAir": false,
                    "oxygenPercent": "",
                    "heliumPercent": ""
                };
                var tanks = logbook_record_model.logbookEntry.diveSpec.scubaTanks;
                tanks.push(tank);
                $('#tanks').append(
                    new EJS({url: '/js/templates/tanksList.ejs'}).render({
                        data: {
                            "tanks": [tank],
                            "index": tanks.length
                        }
                    })
                );
                self.setupTank(tank, tanks.length);
            }
        );
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function (position) {
                if (!$('#latitude').val()) {
                    $('#latitude').val(position.coords.latitude);
                }
                if (!$('#longitude').val()) {
                    $('#longitude').val(position.coords.longitude);
                }
            });
        }
    },

    setupTank: function (tank, i) {
        var self = this;
        $('#removeTank_' + i).click(function () {
            var index = $(this)[0].id.split('_')[1];
            self.collectTanks();
            logbook_record_model.logbookEntry.diveSpec.scubaTanks =
                removeFromArrayByIndex(logbook_record_model.logbookEntry.diveSpec.scubaTanks, index - 1);
            self.restoreTanks();
        });
        util_controller.setupSelect2(
            'supplyType_' + i, logbook_record_model.supplyTypes, tank.supplyType,
            labels["cmas.face.logbook.supplyType"]
        );
        util_controller.setupSelect2(
            'volumeMeasureUnit_' + i, logbook_record_model.volumeMeasureUnits, tank.volumeMeasureUnit,
            labels["cmas.face.logbook.volumeMeasureUnit"]
        );
        util_controller.setupSelect2(
            'pressureMeasureUnit_' + i, logbook_record_model.pressureMeasureUnits, tank.pressureMeasureUnit,
            labels["cmas.face.logbook.pressureMeasureUnit"]
        );
        if (tank.isDecoTank) {
            tank.isAir = false;
            $('#isAirRow_' + i).hide();
        } else {
            $('#isAirRow_' + i).show();
        }
        if (tank.isAir) {
            $('#isDecoRow_' + i).hide();
            $('#gasMix_' + i).hide();
        }
        else {
            $('#isDecoRow_' + i).show();
            $('#gasMix_' + i).show();
        }

        $('#isDecoTank_' + i).change(
            function () {
                var index = $(this)[0].id.split('_')[1];
                if ($(this).prop('checked')) {
                    $('#isAir_' + index).attr('checked', false);
                    $('#isAirRow_' + index).hide();
                } else {
                    $('#isAirRow_' + index).show();
                }
            }
        );
        $('#isAir_' + i).change(
            function () {
                var index = $(this)[0].id.split('_')[1];
                if ($(this).prop('checked')) {
                    $('#isDecoTank_' + index).attr('checked', false);
                    $('#isDecoRow_' + index).hide();
                    $('#gasMix_' + index).hide();
                } else {
                    $('#isDecoRow_' + index).show();
                    $('#gasMix_' + index).show();
                }
            }
        );
        $('#size_' + i).keypress(function (event) {
            util_controller.filterNumbers(event, $(this).val(), 3);
        });
        $('#startPressure_' + i).keypress(function (event) {
            util_controller.filterNumbers(event, $(this).val(), 4);
        });
        $('#endPressure_' + i).keypress(function (event) {
            util_controller.filterNumbers(event, $(this).val(), 4);
        });
        $('#oxygenPercent_' + i).keypress(function (event) {
            util_controller.filterNumbers(event, $(this).val(), 2);
        });
        $('#heliumPercent_' + i).keypress(function (event) {
            util_controller.filterNumbers(event, $(this).val(), 2);
        });
        //}
    },

    restoreTanks: function () {
        var tanks = logbook_record_model.logbookEntry.diveSpec.scubaTanks;
        if (tanks.length > 0) {
            $('#tanks').html(
                new EJS({url: '/js/templates/tanksList.ejs'}).render({data: {"tanks": tanks, "index": 1}})
            );
            for (var i = 0; i < tanks.length; i++) {
                var tank = tanks[i];
                this.setupTank(tank, i + 1);
            }
        } else {
            $('#tanks').empty();
        }
    },

    buildLogbookEntryForm: function () {
        // dd/MM/yyyy HH:mm
        logbook_record_model.logbookEntry.diveDate = $('#diveDate').val() + ' ' + $('#diveTime').val();
        logbook_record_model.logbookEntry.prevDiveDate = $('#prevDiveDate').val() + ' ' + $('#prevDiveTime').val();
        logbook_record_model.logbookEntry.name = $('#spotName').val();
        logbook_record_model.logbookEntry.latitude = $('#latitude').val();
        logbook_record_model.logbookEntry.longitude = $('#longitude').val();
        logbook_record_model.logbookEntry.note = $('#comments').val();

        logbook_record_model.logbookEntry.diveSpec.durationMinutes = $('#duration').val();
        logbook_record_model.logbookEntry.diveSpec.maxDepthMeters = $('#depth').val();
        logbook_record_model.logbookEntry.diveSpec.avgDepthMeters = $('#avgDepth').val();

        logbook_record_model.logbookEntry.diveSpec.weather = $('#weather').val();
        logbook_record_model.logbookEntry.diveSpec.surface = $('#surface').val();
        logbook_record_model.logbookEntry.diveSpec.waterType = $('#waterType').val();
        logbook_record_model.logbookEntry.diveSpec.current = $('#current').val();
        logbook_record_model.logbookEntry.diveSpec.underWaterVisibility = $('#underWaterVisibility').val();
        logbook_record_model.logbookEntry.diveSpec.airTemp = $('#airTemp').val();
        logbook_record_model.logbookEntry.diveSpec.waterTemp = $('#waterTemp').val();
        logbook_record_model.logbookEntry.diveSpec.temperatureMeasureUnit = $('#temperatureMeasureUnit').val();

        logbook_record_model.logbookEntry.diveSpec.divePurpose = $('#divePurpose').val();
        logbook_record_model.logbookEntry.diveSpec.entryType = $('#entryType').val();

        logbook_record_model.logbookEntry.diveSpec.additionalWeightKg = $('#addWeight').val();
        logbook_record_model.logbookEntry.diveSpec.diveSuit = $('#diveSuit').val();

        logbook_record_model.logbookEntry.diveSpec.decoStepsComments = $('#decoStepsComments').val();
        logbook_record_model.logbookEntry.diveSpec.hasSafetyStop = $('#hasSafetyStop').prop("checked");
        logbook_record_model.logbookEntry.diveSpec.cnsToxicity = $('#cnsToxicity').val();

        logbook_record_model.logbookEntry.diveSpec.isApnea = $('#isApnea').prop("checked");
        if (logbook_record_model.logbookEntry.diveSpec.isApnea) {
            logbook_record_model.logbookEntry.diveSpec.scubaTanks = [];
        }
        this.collectTanks();
    },

    collectTanks: function () {
        var tanks = logbook_record_model.logbookEntry.diveSpec.scubaTanks;
        for (var i = 0; i < tanks.length; i++) {
            var tank = tanks[i];
            var index = i + 1;
            tank.size = $('#size_' + index).val();
            tank.volumeMeasureUnit = $('#volumeMeasureUnit_' + index).val();
            tank.startPressure = $('#startPressure_' + index).val();
            tank.endPressure = $('#endPressure_' + index).val();
            tank.pressureMeasureUnit = $('#pressureMeasureUnit_' + index).val();
            tank.supplyType = $('#supplyType_' + index).val();

            tank.isDecoTank = $('#isDecoTank_' + index).prop("checked");
            if (tank.isDecoTank) {
                tank.isAir = false;
            } else {
                tank.isAir = $('#isAir_' + index).prop("checked");
            }
            if (tank.isAir) {
                tank.oxygenPercent = "0";
                tank.heliumPercent = "0";
            }
            else {
                tank.oxygenPercent = $('#oxygenPercent_' + index).val();
                tank.heliumPercent = $('#heliumPercent_' + index).val();
            }
        }
    },

    maskInvalidValues: function () {
        logbook_record_model.logbookEntry.latitude = util_controller.maskInvalidFloat(
            logbook_record_model.logbookEntry.latitude
        );
        logbook_record_model.logbookEntry.longitude = util_controller.maskInvalidFloat(
            logbook_record_model.logbookEntry.longitude
        );

        logbook_record_model.logbookEntry.diveSpec.durationMinutes = util_controller.maskInvalidInt(
            logbook_record_model.logbookEntry.diveSpec.durationMinutes
        );
        logbook_record_model.logbookEntry.diveSpec.maxDepthMeters = util_controller.maskInvalidInt(
            logbook_record_model.logbookEntry.diveSpec.maxDepthMeters
        );
        logbook_record_model.logbookEntry.diveSpec.avgDepthMeters = util_controller.maskInvalidInt(
            logbook_record_model.logbookEntry.diveSpec.avgDepthMeters
        );

        logbook_record_model.logbookEntry.diveSpec.airTemp = util_controller.maskInvalidFloat(
            logbook_record_model.logbookEntry.diveSpec.airTemp
        );
        logbook_record_model.logbookEntry.diveSpec.waterTemp = util_controller.maskInvalidFloat(
            logbook_record_model.logbookEntry.diveSpec.waterTemp
        );

        logbook_record_model.logbookEntry.diveSpec.additionalWeightKg = util_controller.maskInvalidFloat(
            logbook_record_model.logbookEntry.diveSpec.additionalWeightKg
        );

        var tanks = logbook_record_model.logbookEntry.diveSpec.scubaTanks;
        for (var i = 0; i < tanks.length; i++) {
            var tank = tanks[i];
            tank.size = util_controller.maskInvalidFloat(tank.size);
            tank.startPressure = util_controller.maskInvalidFloat(tank.startPressure);
            tank.endPressure = util_controller.maskInvalidFloat(tank.endPressure);
            if (!tank.isAir) {
                tank.oxygenPercent = util_controller.maskInvalidFloat(tank.oxygenPercent);
                tank.heliumPercent = util_controller.maskInvalidFloat(tank.heliumPercent);
            }
        }
    },

    validateCreateForm: function () {
        var result = {};
        result.fieldErrors = {};
        result.errors = {};
        var diveDateFull = logbook_record_model.logbookEntry.diveDate;
        if (isStringTrimmedEmpty(diveDateFull)) {
            result.fieldErrors["diveDate"] = 'validation.emptyField';
            result.fieldErrors["diveTime"] = 'validation.emptyField';
        } else {
            var diveDate = diveDateFull.split(' ')[0];
            if (isStringTrimmedEmpty(diveDate)) {
                result.fieldErrors["diveDate"] = 'validation.emptyField';
            }
            var diveTime = diveDateFull.split(' ')[1];
            if (isStringTrimmedEmpty(diveTime)) {
                result.fieldErrors["diveTime"] = 'validation.emptyField';
            }
        }
        if (isStringTrimmedEmpty(logbook_record_model.logbookEntry.name)) {
            result.fieldErrors["name"] = 'validation.emptyField';
        }
        if (isStringTrimmedEmpty(logbook_record_model.logbookEntry.latitude)) {
            result.fieldErrors["latitude"] = 'validation.emptyField';
        } else if (!is_positive_float(logbook_record_model.logbookEntry.latitude)) {
            result.fieldErrors["latitude"] = 'validation.incorrectNumber';
        }
        if (isStringTrimmedEmpty(logbook_record_model.logbookEntry.longitude)) {
            result.fieldErrors["longitude"] = 'validation.emptyField';
        } else if (!is_positive_float(logbook_record_model.logbookEntry.longitude)) {
            result.fieldErrors["longitude"] = 'validation.incorrectNumber';
        }
        if (isStringTrimmedEmpty(logbook_record_model.logbookEntry.diveSpec.durationMinutes)) {
            result.fieldErrors["durationMinutes"] = 'validation.emptyField';
        } else if (!is_positive_int(logbook_record_model.logbookEntry.diveSpec.durationMinutes)) {
            result.fieldErrors["durationMinutes"] = 'validation.incorrectNumber';
        }
        if (isStringTrimmedEmpty(logbook_record_model.logbookEntry.diveSpec.maxDepthMeters)) {
            result.fieldErrors["maxDepthMeters"] = 'validation.emptyField';
        } else if (!is_positive_int(logbook_record_model.logbookEntry.diveSpec.maxDepthMeters)) {
            result.fieldErrors["maxDepthMeters"] = 'validation.incorrectNumber';
        }
        if (!isStringTrimmedEmpty(logbook_record_model.logbookEntry.diveSpec.avgDepthMeters)) {
            if (!is_positive_int(logbook_record_model.logbookEntry.diveSpec.avgDepthMeters)) {
                result.fieldErrors["avgDepthMeters"] = 'validation.incorrectNumber';
            }
        }
        if (!isStringTrimmedEmpty(logbook_record_model.logbookEntry.diveSpec.airTemp)) {
            if (is_float(logbook_record_model.logbookEntry.diveSpec.airTemp)) {
                result.fieldErrors["airTemp"] = 'validation.incorrectNumber';
            }
        }
        if (!isStringTrimmedEmpty(logbook_record_model.logbookEntry.diveSpec.waterTemp)) {
            if (is_float(logbook_record_model.logbookEntry.diveSpec.waterTemp)) {
                result.fieldErrors["waterTemp"] = 'validation.incorrectNumber';
            }
        }
        if ((!isStringTrimmedEmpty(logbook_record_model.logbookEntry.diveSpec.airTemp)
            || !isStringTrimmedEmpty(logbook_record_model.logbookEntry.diveSpec.waterTemp))
            && !result.fieldErrors["waterTemp"] && !result.fieldErrors["airTemp"]
            && isStringTrimmedEmpty(logbook_record_model.logbookEntry.diveSpec.temperatureMeasureUnit)
        ) {
            result.fieldErrors["temperatureMeasureUnit"] = 'validation.emptyField';
        }

        if (!isStringTrimmedEmpty(logbook_record_model.logbookEntry.diveSpec.additionalWeightKg)) {
            if (!is_positive_float(logbook_record_model.logbookEntry.diveSpec.additionalWeightKg)) {
                result.fieldErrors["additionalWeightKg"] = 'validation.incorrectNumber';
            }
        }
        var tanks = logbook_record_model.logbookEntry.diveSpec.scubaTanks;
        for (var i = 0; i < tanks.length; i++) {
            var tank = tanks[i];
            var index = i + 1;
            if (isStringTrimmedEmpty(tank.supplyType)) {
                result.fieldErrors["supplyType_" + index] = 'validation.emptyField';
            }
            if (isStringTrimmedEmpty(tank.size)) {
                result.fieldErrors["size_" + index] = 'validation.emptyField';
            } else if (!is_positive_float(tank.size)) {
                result.fieldErrors["size_" + index] = 'validation.incorrectNumber';
            }
            if (isStringTrimmedEmpty(tank.volumeMeasureUnit)) {
                result.fieldErrors["volumeMeasureUnit_" + index] = 'validation.emptyField';
            }
            if (isStringTrimmedEmpty(tank.startPressure)) {
                result.fieldErrors["startPressure_" + index] = 'validation.emptyField';
            } else if (!is_positive_float(tank.startPressure)) {
                result.fieldErrors["startPressure_" + index] = 'validation.incorrectNumber';
            }
            if (isStringTrimmedEmpty(tank.endPressure)) {
                result.fieldErrors["endPressure_" + index] = 'validation.emptyField';
            } else if (!is_non_negative_float(tank.endPressure)) {
                result.fieldErrors["endPressure_" + index] = 'validation.incorrectNumber';
            }
            if (!result.fieldErrors["startPressure_" + index] && !result.fieldErrors["endPressure_" + index]
                && parseFloat(tank.startPressure) < parseFloat(tank.endPressure)
            ) {
                result.fieldErrors["startPressure_" + index] = 'validation.logbook.start.pressure.less.end.pressure';
            }
            if (isStringTrimmedEmpty(tank.pressureMeasureUnit)) {
                result.fieldErrors["pressureMeasureUnit_" + index] = 'validation.emptyField';
            }

            if (!tank.isAir) {
                if (isStringTrimmedEmpty(tank.oxygenPercent)) {
                    result.fieldErrors["oxygenPercent_" + index] = 'validation.emptyField';
                } else if (!is_non_negative_float(tank.oxygenPercent)) {
                    result.fieldErrors["oxygenPercent_" + index] = 'validation.incorrectNumber';
                }
                if (!isStringTrimmedEmpty(tank.heliumPercent)) {
                    if (!is_non_negative_float(tank.heliumPercent)) {
                        result.fieldErrors["heliumPercent_" + index] = 'validation.incorrectNumber';
                    }
                }
            }
        }
        result.success = jQuery.isEmptyObject(result.fieldErrors) && jQuery.isEmptyObject(result.errors);
        return result;
    },

    cleanCreateFormErrors: function () {
        $('#create_error_diveDate').empty();
        $('#create_error_diveTime').empty();
        $('#create_error_name').empty();
        $('#create_error_latitude').empty();
        $('#create_error_longitude').empty();
        $('#create_error_durationMinutes').empty();
        $('#create_error_maxDepthMeters').empty();
        $('#create_error_avgDepthMeters').empty();
        $('#create_error_airTemp').empty();
        $('#create_error_waterTemp').empty();
        $('#create_error_additionalWeightKg').empty();
        var tanks = logbook_record_model.logbookEntry.diveSpec.scubaTanks;
        for (var i = 0; i < tanks.length; i++) {
            var index = i + 1;
            $('#create_error_supplyType_' + index).empty();
            $('#create_error_size_' + index).empty();
            $('#create_error_volumeMeasureUnit_' + index).empty();
            $('#create_error_startPressure_' + index).empty();
            $('#create_error_endPressure_' + index).empty();
            $('#create_error_pressureMeasureUnit_' + index).empty();
            $('#create_error_oxygenPercent_' + index).empty();
            $('#create_error_heliumPercent_' + index).empty();
        }
    }
};

$(document).ready(function () {
    logbook_record_diveProfile_controller.init();
});
