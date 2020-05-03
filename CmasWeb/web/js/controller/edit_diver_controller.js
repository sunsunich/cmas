var edit_diver_controller = {

    validationController: null,

    init: function (areaOfInterest, countryCode, isCmas) {
        util_controller.setupSelect2(
            'editDiver_areaOfInterest', profile_model.areas, areaOfInterest,
            labels["cmas.face.registration.area"]
        );
        util_controller.setupSelect2('editDiver_countryCode',
            [],
            countryCode,
            labels["cmas.face.registration.form.label.country"]
        );

        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'editDiver';
        this.validationController.fields = [
            {
                id: 'firstName',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyFirstName';
                    }
                }
            },
            {
                id: 'lastName',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyLastName';
                    }
                }
            },
            {
                id: 'dob',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyDob';
                    }
                }
            },
            {
                id: 'countryCode',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyCountry';
                    }
                }
            },
            {
                id: 'areaOfInterest',
                validateField: function (value) {}
            }
        ];
        if(isCmas) {
            $('#editDiver_firstName').prop('disabled', true);
            $('#editDiver_lastName').prop('disabled', true);
            $('#editDiver_dob').prop('disabled', true);
            $('#editDiver_countryCode').prop('disabled', true);

            $('#cmasDiversInfo').show();
        }

        this.validationController.submitButton = $('#editDiverButton');
        this.validationController.init();
        this.setListeners();
        registration_flow_controller.init(
            'simple',
            {
                "visibleInputElemId": 'editDiver_firstName',
                "backgroundImageId" : "editDiverImageBackground"
            }
        );
    },

    setListeners: function () {
        var self = this;
        $("#editDiver_dob").datepicker(
            {
                changeYear: true,
                changeMonth: true,
                yearRange: '-100:-10',
                defaultDate: "-20y",
                dateFormat: 'dd/mm/yy'
            }
        );
        $('#editDiverButton').click(function () {
            self.submitForm();
            return false;
        });
        $('#diverSaveSuccessClose').click(function () {
            window.location.reload();
        });
        $('#diverSaveSuccessOk').click(function () {
            window.location.reload();
        });
    },

    submitForm: function () {
        var self = this;
        if (this.validationController.validateForm()) {
            profile_model.editDiver(
                this.validationController.form
                , function (/*json*/) {
                    $("#diverSaveSuccess").show();
                }
                , function (json) {
                    self.validationController.showErrors(json);
                }
            );
        }
    }
};
