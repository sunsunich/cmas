var car_mobile_controller = {

    isInit: false,
    validationController: null,
    emailConfirmationValidationController: null,

    init: function () {
        util_controller.setupSelect2('car_federationId', [], '', labels["cmas.face.registration.form.label.federation"]);
        util_controller.setupSelect2(
            'car_diverType', add_card_model.diverTypes, '',
            labels["cmas.face.card.diverType"]
        );
        util_controller.setupSelect2(
            'car_diverLevel', add_card_model.diverLevels, '',
            labels["cmas.face.card.level"]
        );
        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'car';
        this.validationController.fields = [
            {
                id: 'diverType',
                validateField: function (value) {
                    // if (isStringTrimmedEmpty(value)) {
                    //     return 'validation.emptyFirstName';
                    // }
                }
            },
            {
                id: 'diverLevel',
                validateField: function (value) {
                    // if (isStringTrimmedEmpty(value)) {
                    //     return 'validation.emptyLastName';
                    // }
                }
            },
            {
                id: 'cardType',
                validateField: function (value) {
                    // if (isStringTrimmedEmpty(value)) {
                    //     return 'validation.emptyDob';
                    // }
                }
            },
            {
                id: 'validUntil',
                validateField: function (value) {
                    // if (isStringTrimmedEmpty(value)) {
                    //     return 'validation.emailEmpty';
                    // }
                }
            },
            {
                id: 'federationCardNumber',
                validateField: function (value) {
                    // if (isStringTrimmedEmpty(value)) {
                    //     return 'validation.emptyCountry';
                    // }
                }
            },
            {
                id: 'token',
                validateField: function (value) {
                    // if (isStringTrimmedEmpty(value)) {
                    //     return 'validation.emptyFirstName';
                    // }
                }
            },
            {
                id: 'federationId',
                validateField: function (value) {
                    // if (isStringTrimmedEmpty(value)) {
                    //     return 'validation.emptyField';
                    // }
                }
            }
        ];
        this.validationController.submitButton = $('#carSubmit');
        var self = this;
        multiple_fileUpload_controller.addImageElem = $('#addImage');
        multiple_fileUpload_controller.photoListContainer = $('#photoListContainer');
        multiple_fileUpload_controller.errorElem = $('#card_error_images');
        multiple_fileUpload_controller.addImageCallback = function () {
            self.validationController.checkForm();
        };
        multiple_fileUpload_controller.maxFilesToAttach = 10;
        multiple_fileUpload_controller.init();

        this.validationController.fromValidator = function (/*form*/) {
            if (multiple_fileUpload_controller.availableIds.length == 10) {
                return "validation.registration.images";
            }
        };

        this.validationController.init();

        this.setListeners();
        this.isInit = true;
    },

    setListeners: function () {
        var self = this;
        $('#carSubmit').click(function () {
            self.sendCardApprovalRequest();
            return false;
        });
        $("#car_validUntil").datepicker(
            {
                changeYear: true,
                changeMonth: true,
                yearRange: '0:+10',
                defaultDate: "+1d",
                dateFormat: 'dd/mm/yy'
            }
        );
    },

    sendCardApprovalRequest: function () {
        if (this.validationController.validateForm()) {
            this.validationController.form.images = [];
            for (var base64String in multiple_fileUpload_controller.base64Strings) {
                if (multiple_fileUpload_controller.base64Strings.hasOwnProperty(base64String)) {
                    this.validationController.form.images.push(
                        multiple_fileUpload_controller.base64Strings[base64String]
                    );
                }
            }
            var self = this;
            add_card_model.mobileUploadCardRequest(
                self.validationController.form
                , function (json) {
                    window.alert(json)
                }
                , function (json) {
                    if (json && json.message) {
                        self.processCustomError(json.message);
                    } else {
                        self.validationController.showErrors(json);
                    }
                }
            );
        }
    },

    processCustomError: function (code) {
        window.alert( error_codes[code]);
    },
};
