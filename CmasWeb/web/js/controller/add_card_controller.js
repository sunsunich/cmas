var add_card_controller = {

    validationController: null,
    isValidateFilesAdded: false,

    init: function () {
        var self = this;

        util_controller.setupSelect2(
            'card_diverLevel', add_card_model.diverLevels, '', labels["cmas.face.card.level"]
        );
        util_controller.setupSelect2('card_countryCode', [], '',
            labels["cmas.face.registration.form.label.country"]
        );

        this.validationController = simpleClone(validation_controller);
        this.validationController.prefix = 'card';
        this.validationController.fields = [
            {
                id: 'diverLevel',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyField';
                    }
                }
            },
            {
                id: 'countryCode',
                validateField: function (value) {
                    if (isStringTrimmedEmpty(value)) {
                        return 'validation.emptyField';
                    }
                }
            }
        ];
        this.validationController.submitButton = $('#cardSubmit');

        multiple_fileUpload_controller.addImageElem = $('#addImage');
        multiple_fileUpload_controller.photoListContainer = $('#photoListContainer');
        multiple_fileUpload_controller.errorElem = $('#card_error_photo');
        multiple_fileUpload_controller.addImageCallback = function () {
            self.validationController.checkForm();
        };
        multiple_fileUpload_controller.init();

        this.validationController.fromValidator = function (/*form*/) {
            if (multiple_fileUpload_controller.availableIds.length == 0) {
                self.isValidateFilesAdded = true;
                return null;
            } else {
                if (self.isValidateFilesAdded) {
                    return "validation.cards.images";
                } else {
                    return null;
                }
            }
        };
        this.validationController.init();

        this.setListeners();
    },


    setListeners: function () {
        var self = this;
        $('#cardSubmit').click(function () {
            self.submitForm();
            return false;
        });

        $('#cardAddedDialogClose').click(function () {
            $('#cardAddedDialog').hide();
        });
    },

    submitForm: function () {
        var self = this;
        this.isValidateFilesAdded = true;
        if (this.validationController.validateForm()) {
            add_card_model.uploadCardRequest(
                this.validationController.form,
                multiple_fileUpload_controller.files,
                function () {
                    $('#cardAddedDialog').show();
                }, function (json) {
                    self.validationController.showErrors(json);
                }
            )
        }
    }
};

$(document).ready(function () {
    add_card_controller.init();
});
