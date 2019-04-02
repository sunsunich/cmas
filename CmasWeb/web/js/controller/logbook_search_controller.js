var logbook_search_controller = {

    controller: null,
    model: null,

    init: function (logbook_feed_controller) {
        this.controller = logbook_feed_controller;
        this.model = logbook_feed_controller.model;
        country_controller.init();
        util_controller.setupDate('fromDate');
        util_controller.setupDate('toDate');
        var self = this;
        $('#fromMeters').keypress(function (event) {
            util_controller.filterNumbers(event, $(this).val(), 3);
        });
        $('#toMeters').keypress(function (event) {
            util_controller.filterNumbers(event, $(this).val(), 3);
        });
        $('#fromMinutes').keypress(function (event) {
            util_controller.filterNumbers(event, $(this).val(), 3);
        });
        $('#toMinutes').keypress(function (event) {
            util_controller.filterNumbers(event, $(this).val(), 3);
        });
        $('#searchRecords').click(function () {
            self.search();
        });
    },

    search: function () {
        this.model.country = $('#country').val();
        this.model.fromDate = $('#fromDate').val();
        this.model.toDate = $('#toDate').val();
        this.model.fromMeters = $('#fromMeters').val();
        this.model.toMeters = $('#toMeters').val();
        this.model.fromMinutes = $('#fromMinutes').val();
        this.model.toMinutes = $('#toMinutes').val();
        this.cleanErrors();
        var formErrors = this.validateForm();
        if (formErrors.success) {
            var self = this;
            this.model.searchForRecords(
                function (json) {
                    self.controller.stop();
                    self.controller.renderFeed(json, true, true);
                }
                , function (json) {
                    validation_controller.simpleShowErrors('search', json);
                });
        }
        else {
            validation_controller.simpleShowErrors('search', formErrors);
        }
    },

    validateForm: function () {
        var result = {};
        result.fieldErrors = {};
        result.errors = {};
        if (!isStringTrimmedEmpty(this.model.fromMeters)
            && !is_positive_int(this.model.fromMeters)) {
            result.fieldErrors["fromMeters"] = 'validation.incorrectNumber';
        }
        if (!isStringTrimmedEmpty(this.model.toMeters)
            && !is_positive_int(this.model.toMeters)) {
            result.fieldErrors["toMeters"] = 'validation.incorrectNumber';
        }
        if (!isStringTrimmedEmpty(this.model.fromMinutes)
            && !is_positive_int(this.model.fromMinutes)) {
            result.fieldErrors["fromMinutes"] = 'validation.incorrectNumber';
        }
        if (!isStringTrimmedEmpty(this.model.toMinutes)
            && !is_positive_int(this.model.toMinutes)) {
            result.fieldErrors["toMinutes"] = 'validation.incorrectNumber';
        }

        result.success = jQuery.isEmptyObject(result.fieldErrors) && jQuery.isEmptyObject(result.errors);
        return result;
    },

    cleanErrors: function () {
        $('#search_error_fromDate').empty();
        $('#search_error_toDate').empty();
        $('#search_error_fromMeters').empty();
        $('#search_error_toMeters').empty();
        $('#search_error_fromMinutes').empty();
        $('#search_error_toMinutes').empty();
    }
};