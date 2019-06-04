var insurance_request_controller = {

    validationController: null,

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        util_controller.setupSelect2(
            'gender', insurance_request_model.genders, '',
            labels["cmas.loyalty.insurance.gender"]
        );
        util_controller.setupSelect2('country', [], '',
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
                id: 'number',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyField';
                    }
                }
            }

        ];
        this.validationController.fieldIdsToValidate = ['gender', 'country', "zipCode", "city", "street", "number"];
        this.validationController.submitButton = $('#insuranceRequestSubmit');
        this.validationController.init();

        $('#insuranceRequestSubmit').click(function () {
            self.submitForm();
            return false;
        });

        $('#insuranceRequestSuccessClose').click(function () {
            $('#insuranceRequestSuccess').hide();
        });
        $('#insuranceRequestSuccessOk').click(function () {
            $('#insuranceRequestSuccess').hide();
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

            var self = this;
            insurance_request_model.sendInsuranceRequest(
                function (json) {
                    $('#insuranceRequestSuccess').show();
                }
                , function (json) {
                    self.validationController.showErrors(json);
                }
            );
        }
    }
};

$(document).ready(function () {
    insurance_request_controller.init();
});
