var payment_controller = {

    //todo create model
    featuresMap: {},
    selectedFeatureIds: [],

    init: function (features, selectedFeatureIds) {
        for (var i = 0; i < features.length; i++) {
            this.featuresMap[features[i].id] = features[i];
        }
        for (i = 0; i < selectedFeatureIds.length; i++) {
            this.selectFeature(selectedFeatureIds[i]);
        }
        registration_flow_controller.init('simple',
            {backgroundImageId: 'paymentImageBackground', visibleInputElemId: "insuranceRequest_zipCode"});
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $('#payButton').click(function () {
            if (self.selectedFeatureIds.length > 0) {
                window.location = '/secure/pay.html?featuresIdsJson=' + JSON.stringify(self.selectedFeatureIds);
            } else {
                $('#payment_error').html(error_codes["validation.noPaidFeatureSelected"]).show();
            }
            return false;
        });
        for (var key in this.featuresMap) {
            if (this.featuresMap.hasOwnProperty(key)) {
                var featureCheckBox = $('#paidFeature_' + key);
                if (featureCheckBox[0]) {
                    featureCheckBox.change(function () {
                        var featureId = $(this)[0].id.split('_')[1];
                        if (this.checked) {
                            self.selectFeature(featureId);
                        } else {
                            self.removeFeature(featureId);
                        }
                    });
                    if (featureCheckBox[0].checked) {
                        self.selectFeature(key);
                    }
                }
            }
        }

        $('#insuranceRequestCancel').click(function () {
            self.backToPayForm();
            $('#paidFeature_2').prop('checked', false);
            $("label[for='paidFeature_2']").removeClass('chk').addClass('clr');
            return false;
        });

        insurance_request_controller.successHandler = function (json) {
            self.addFeature('2');
            self.backToPayForm();
        };
    },

    selectFeature: function (featureId) {
        if (featureId == '2') {
            $('#insuranceBlock').show();
            $('#paymentBlock').hide();
        } else {
            this.addFeature(featureId);
        }
    },

    backToPayForm: function () {
        $('#insuranceBlock').hide();
        $('#paymentBlock').show();
    },

    addFeature: function (featureId) {
        this.selectedFeatureIds.push(featureId);
        this.updateAmount();
    },

    removeFeature: function (featureId) {
        this.selectedFeatureIds = removeFromArray(this.selectedFeatureIds, featureId);
        this.updateAmount();
    },

    updateAmount: function () {
        var amount = 0;
        if (this.selectedFeatureIds.length == 0) {
            $('#payment_error').html(error_codes["validation.noPaidFeatureSelected"]).show();
        } else {
            $('#payment_error').empty().hide();
            for (var i = 0; i < this.selectedFeatureIds.length; i++) {
                amount += this.featuresMap[this.selectedFeatureIds[i]].price;
            }
        }
        $('#total').html(amount);
    }
};
