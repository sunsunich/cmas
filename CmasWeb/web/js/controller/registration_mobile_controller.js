var registration_mobile_controller = {

    isInit: false,
    validationController: null,
    emailConfirmationValidationController: null,

    init: function () {
        util_controller.setupSelect2('reg_countryCode', [], '', labels["cmas.face.registration.form.label.country"]);
        util_controller.setupSelect2('reg_federationId', [], '', labels["cmas.face.registration.form.label.federation"]);
        util_controller.setupSelect2(
            'reg_area', registration_model.areas, '',
            labels["cmas.face.registration.area"]
        );
        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'reg';
        this.validationController.fields = [
            {
                id: 'firstName',
                validateField: function (value) {
                    // if (isStringTrimmedEmpty(value)) {
                    //     return 'validation.emptyFirstName';
                    // }
                }
            },
            {
                id: 'lastName',
                validateField: function (value) {
                    // if (isStringTrimmedEmpty(value)) {
                    //     return 'validation.emptyLastName';
                    // }
                }
            },
            {
                id: 'dob',
                validateField: function (value) {
                    // if (isStringTrimmedEmpty(value)) {
                    //     return 'validation.emptyDob';
                    // }
                }
            },
            {
                id: 'email',
                validateField: function (value) {
                    // if (isStringTrimmedEmpty(value)) {
                    //     return 'validation.emailEmpty';
                    // }
                }
            },
            {
                id: 'countryCode',
                validateField: function (value) {
                    // if (isStringTrimmedEmpty(value)) {
                    //     return 'validation.emptyCountry';
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
            },
            {
                id: 'termsAndCondAccepted',
                validateField: function (value) {
                    // if (value !== 'true') {
                    //     return 'validation.termsAndCondNotAccepted';
                    // }
                }
            }
        ];
        this.validationController.submitButton = $('#regSubmit');
        var self = this;
        multiple_fileUpload_controller.addImageElem = $('#addImage');
        multiple_fileUpload_controller.photoListContainer = $('#photoListContainer');
        multiple_fileUpload_controller.errorElem = $('#card_error_photo');
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
        $('#regSubmit').click(function () {
            self.registerMobile();
            return false;
        });
        $("#reg_dob").datepicker(
            {
                changeYear: true,
                changeMonth: true,
                yearRange: '-100:-10',
                defaultDate: "-20y",
                dateFormat: 'dd/mm/yy'
            }
        );
        util_controller.setupSelect2(
            'emailConfirmation_area', registration_model.areas, '',
            labels["cmas.face.registration.area"]
        );
        $('#registerButton').click(function () {
            self.registerMobile();
            return false;
        });
    },

    registerMobile: function () {
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
            registration_model.registerMobile(
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
        registration_flow_controller.toggleMessage("message", {
                text: labels['cmas.face.registration.form.custom.error.text'] + '<br />' + error_codes[code],
                button: {
                    text: labels["cmas.face.link.back.text"]
                },
                saveRegState: true
            }
        );
    },
};
