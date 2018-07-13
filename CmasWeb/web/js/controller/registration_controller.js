var registration_controller = {

    isInit: false,
    validationController: null,
    emailConfirmationValidationController: null,

    init: function () {
        util_controller.setupSelect2('reg_country', [], '', labels["cmas.face.registration.form.label.country"]);
        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'reg';
        this.validationController.fields = [
            {
                id: 'certificate',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyCertificate';
                    }
                }
            },
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
                id: 'country',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyCountry';
                    }
                }
            },
            {
                id: 'termsAndCondAccepted',
                validateField: function (value) {
                    if (value !== 'true') {
                        return 'validation.termsAndCondNotAccepted';
                    }
                }
            }
        ];
        //validation_controller.submitButton = $('#regSubmit');
        this.validationController.fieldIdsToValidate = ['certificate', 'termsAndCondAccepted'];
        this.validationController.init();

        this.emailConfirmationValidationController = simpleClone(validation_controller);
        this.emailConfirmationValidationController.prefix = 'emailConfirmation';
        this.emailConfirmationValidationController.fields = [
            {
                id: 'email',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emailEmpty';
                    }
                }
            }
        ];
        this.emailConfirmationValidationController.submitButton = $('#registerButton');
        this.emailConfirmationValidationController.init();

        this.setListeners();
        this.isInit = true;
    },

    setListeners: function () {
        var self = this;
        $('#regSubmit').click(function () {
            self.registerFirstStep();
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
        $('#reg_certificate').on("input", function () {
            self.toggleRegistrationSearchFields(!isStringTrimmedEmpty($(this).val()));
        });
        $('#reg_noCertificate').change(function () {
            var regCertificateElem = $('#reg_certificate');
            if (this.checked) {
                self.validationController.clearFieldError('certificate');
                self.validationController.fieldIdsToValidate = ['firstName', 'lastName', 'dob', 'country', 'termsAndCondAccepted'];
                self.validationController.init();
                self.toggleRegistrationSearchFields(false);
                regCertificateElem.prop('disabled', true);
            } else {
                self.validationController.clearFieldError('firstName');
                self.validationController.clearFieldError('lastName');
                self.validationController.clearFieldError('dob');
                self.validationController.clearFieldError('country');
                self.validationController.fieldIdsToValidate = ['certificate', 'termsAndCondAccepted'];
                self.validationController.init();
                self.toggleRegistrationSearchFields(!isStringTrimmedEmpty(regCertificateElem.val()));
                regCertificateElem.prop('disabled', false);
            }
        });
        $('#foundDiverSubmit').click(function () {
            self.showEmailConfirmation();
        });
        $('#cannotFindButton').click(function () {
            registration_flow_controller.modeStack.pop();
            registration_flow_controller.toggleMode('registration');
        });
        util_controller.setupSelect2(
            'emailConfirmation_area', registration_model.areas, '',
            labels["cmas.face.registration.area"]
        );
        $('#registerButton').click(function () {
            self.confirmEmail();
            return false;
        });
        $('#emailConfirmationBack').click(function () {
            registration_flow_controller.modeStack.pop();
            registration_flow_controller.toggleMode('registration');
            return false;
        });
    },

    toggleRegistrationSearchFields: function (disableFullSearch) {
        $("#reg_firstName").prop('disabled', disableFullSearch);
        $("#reg_lastName").prop('disabled', disableFullSearch);
        $("#reg_dob").prop('disabled', disableFullSearch);
        $("#reg_country").prop('disabled', disableFullSearch);
    },

    registerFirstStep: function () {
        if (this.validationController.validateForm()) {
            var self = this;
            registration_model.registerFirstStep(
                self.validationController.form
                , function (json) {
                    self.processFirstStepResponse(json);
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

    processFirstStepResponse: function (json) {
        if (json.length == 0) {
            this.showEmailConfirmation();
        } else if (json.length == 1) {
            var diver = json[0];
            registration_flow_controller.toggleMode("message", {
                text: '<p>' + labels['cmas.face.hello'] + ' ' + diver.firstName + '! '
                + labels['cmas.face.registration.welcome.certificate'] + ' ' + diver.cardNumber + '.' + '</p>'
                + '<p>' + labels['cmas.face.registration.welcome.email'] + ' ' + diver.maskedEmail + ' '
                + labels['cmas.face.registration.welcome.email2'] + '</p>'
                + '<p>' + labels['cmas.face.registration.welcome.help'] + '</p>'
            });
        } else {
            $('#diversToChooseList').html(
                new EJS({url: '/js/templates/diversToChoose.ejs?v=' + webVersion}).render({
                    "divers": json,
                    "webVersion": webVersion
                })
            );
            $('#foundDiverSubmit').prop('disabled', true);
            $(".diver-to-choose-list-item").click(
                function () {
                    $(".diver-to-choose-list-item").removeClass('diver-to-choose-list-item-chosen');
                    registration_model.chosenDiverId = $(this)[0].id.split('_')[0];
                    $(this).addClass('diver-to-choose-list-item-chosen');
                    $('#foundDiverSubmit').prop('disabled', false);
                }
            );
            registration_flow_controller.toggleMode('chooseDivers', {saveRegState: true});
        }
    },

    showEmailConfirmation: function () {
        this.emailConfirmationValidationController.clearFieldError('email');
        $('#emailConfirmation_email').val('');
        registration_flow_controller.toggleMode("emailConfirmation", {saveRegState: true});
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

    confirmEmail: function () {
        if (this.emailConfirmationValidationController.validateForm()) {
            var self = this;
            if (registration_model.chosenDiverId) {
                registration_model.chooseDiver(
                    this.emailConfirmationValidationController.form.email,
                    $('#emailConfirmation_area').val(),
                    function () {
                        self.processRegistrationComplete();
                    },
                    function (json) {
                        self.emailConfirmationValidationController.showErrors(json);
                    }
                )
            } else {
                registration_model.createNewRegistration(
                    this.emailConfirmationValidationController.form.email,
                    $('#emailConfirmation_area').val(),
                    function (/*json*/) {
                        self.processRegistrationComplete();
                    },
                    function (json) {
                        self.emailConfirmationValidationController.showErrors(json);
                    }
                );
            }
        }
    },

    processRegistrationComplete: function () {
        registration_flow_controller.modeStack = [];
        registration_flow_controller.toggleMode("message", {
            text: '<p>' + labels['cmas.face.registration.complete'] + '</p>'
            + '<p>' + labels['cmas.face.registration.email'] + ' ' + registration_model.regForm.email + ' '
            + labels['cmas.face.registration.welcome.email2'] + '</p>'
            + '<p>' + labels['cmas.face.registration.welcome.help'] + '</p>'
        });
    }
};
