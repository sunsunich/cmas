var fed_billing_controller = {

    init: function () {
        this.reloadDiverPaymentList();
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $('.addDiverToPaymentList').click(function () {
            self.addDiverToPaymentList($(this));
        });
    },

    displayDiverPaymentList: function (divers) {
        $('.addDiverToPaymentList').show();
        if (divers && divers.length > 0) {
            $('#diverFromPaymentList').html(
                new EJS({url: '/js/templates/diverPaymentList.ejs?v=' + webVersion}).render({"divers": divers})
            );
            var self = this;
            for (var i = 0; i < divers.length; i++) {
                var diver = divers[i];
                $('#removeDiverFromPaymentList_' + diver.id).click(function () {
                    var id = $(this)[0].id.split('_')[1];
                    fed_billing_model.removeDiverFromPaymentList(
                        id,
                        function (/*json*/) {
                            self.reloadDiverPaymentList();
                        }, function () {
                            window.location.reload();
                        }
                    );
                });
                $('#addDiverToPaymentList_' + diver.id).hide();
            }
            $('#payForDivers').show();
        } else {
            $('#diverFromPaymentList').empty();
            $('#payForDivers').hide();
        }
    },

    addDiverToPaymentList: function (elem) {
        var self = this;
        var diverId = elem[0].id.split('_')[1];
        fed_billing_model.addDiverToPaymentList(diverId,
            function () {
                self.reloadDiverPaymentList();
            }, function () {
                confirm(labels["cmas.fed.addDiverFromPaymentList.failed"]);
            }
        );
    },

    reloadDiverPaymentList: function () {
        var self = this;
        fed_billing_model.getDiverPaymentList(
            function (json) {
                self.displayDiverPaymentList(json.divers);
            }, function () {
                window.location.reload();
            }
        );
    }
};

$(document).ready(function () {
    fed_billing_controller.init();
});
