var insurance_request_controller = {

    validationController: null,
    successHandler: null,

    init: function (isGold, hasInsurance) {
        var self = this;
        var config;
        if (isGold && !hasInsurance) {
            config = {
                backgroundImageId: 'loyaltyProgramImageBackground',
                visibleInputElemId: "insuranceRequest_zipCode"
            };
            this.setListenersForm();
        } else {
            config = {backgroundImageId: 'loyaltyProgramImageBackground'};
            this.setListenersInfo();
        }
        registration_flow_controller.init('simple', config);
        $(window).load(function () {
            self.onResize();
        });
        $(window).resize(function () {
            self.onResize();
        });
    },

    onResize: function () {
        registration_flow_controller.onResize();
        var isDisplayImage = $(window).width() > 630;
        var compressor = 2.5;
        var panelHeight = isDisplayImage ? $('#formImage').height() / compressor : $('#Wrapper-content').height() / compressor;
        $('#insuranceFeaturePanel').css('max-height', panelHeight);
        registration_flow_controller.onResize();

    },

    setListenersInfo: function () {
        var self = this;
        $('#openInsuranceTable').click(function (e) {
            e.preventDefault();
            $('#insuranceFeatureWrapper').show();
            self.onResize();
            return false;
        });
        $('#insuranceFeaturePanelCloseBottom').click(function (e) {
            return self.closeInsuranceInfo(e);
        });
        $('#becomeGold').click(function (e) {
            return window.location = '/secure/pay.html';
        });
    },

    closeInsuranceInfo: function (e) {
        e.preventDefault();
        $('#insuranceFeatureWrapper').hide();
        this.onResize();
        return false;
    },

    setListenersForm: function () {
        util_controller.setupSelect2(
            'insuranceRequest_gender', insurance_request_model.genders, '',
            labels["cmas.loyalty.insurance.gender"]
        );
        util_controller.setupSelect2('insuranceRequest_country', [], '',
            labels["cmas.face.registration.form.label.country"]
        );

        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'insuranceRequest';
        this.validationController.fields = [
            {
                id: 'gender',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyField';
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
                id: 'region',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyCountry';
                    }
                }
            },
            {
                id: 'zipCode',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyField';
                    }
                }
            },
            {
                id: 'city',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyField';
                    }
                }
            },
            {
                id: 'street',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyField';
                    }
                }
            },
            {
                id: 'house',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyField';
                    }
                }
            },
            {
                id: 'termsAndCondAccepted',
                validateField: function (value) {
                    if (value !== 'true') {
                        return 'validation.insurance.termsAndCondNotAccepted';
                    }
                }
            }

        ];
        this.validationController.fieldIdsToValidate = ['gender', 'country', "zipCode", "city", "street", "house", "termsAndCondAccepted"];
        this.validationController.submitButton = $('#insuranceRequestSubmit');
        this.validationController.init();

        var self = this;
        this.validationController.submitButton.click(function () {
            self.submitForm();
            return false;
        });

        $('#insuranceRequestSuccessClose').click(function () {
            window.location.reload();
        });
        $('#insuranceRequestSuccessOk').click(function () {
            window.location.reload();
        });
    },

    submitForm: function () {
        if (this.validationController.validateForm()) {

            insurance_request_model.insuranceRequest.gender = this.validationController.form.gender;
            insurance_request_model.insuranceRequest.address.country.code = this.validationController.form.country;
            insurance_request_model.insuranceRequest.address.region = this.validationController.form.region;
            insurance_request_model.insuranceRequest.address.zipCode = this.validationController.form.zipCode;
            insurance_request_model.insuranceRequest.address.city = this.validationController.form.city;
            insurance_request_model.insuranceRequest.address.street = this.validationController.form.street;
            insurance_request_model.insuranceRequest.address.house = this.validationController.form.house;
            insurance_request_model.insuranceRequest.termsAndCondAccepted = this.validationController.form.termsAndCondAccepted;

            var self = this;
            insurance_request_model.sendInsuranceRequest(
                function (json) {
                    if (self.successHandler) {
                        self.successHandler(json);
                    } else {
                        $('#insuranceRequestSuccess').show();
                    }
                }
                , function (json) {
                    self.validationController.showErrors(json);
                }
            );
        }
    }
};
