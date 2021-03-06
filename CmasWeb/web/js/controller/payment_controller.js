var payment_controller = {

    //todo create model
    featuresMap: {},
    selectedFeatureIds: [],

    init: function (features, selectedFeatureIds) {
        for (var i = 0; i < features.length; i++) {
            this.featuresMap[features[i].id] = features[i];
        }
        for (i = 0; i < selectedFeatureIds.length; i++) {
            this.addFeature(selectedFeatureIds[i]);
        }
        registration_flow_controller.init('simple', {backgroundImageId: 'paymentImageBackground'});
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
                            self.addFeature(featureId);
                        } else {
                            self.removeFeature(featureId);
                        }
                    });
                    if (featureCheckBox[0].checked) {
                        self.addFeature(key);
                    }
                }
            }
        }
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
