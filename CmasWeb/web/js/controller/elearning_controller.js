var elearning_controller = {

    init: function () {
        var self = this;
        elearning_model.getElearningToken(
            function () {
                self.renderTokenPanel();
            }, function () {
                self.renderTokenPanel();
            }
        );

        this.setListeners();
    },

    renderTokenPanel: function () {
        if (elearning_model.tokenStr) {
            $('#eLearningRegister').show();
            $('#returnToElearningBlock').show();
            $('#tokeErrorText').empty().hide();
        } else {
            $('#eLearningRegister').hide();
            $('#returnToElearningBlock').hide();

            var message = error_codes[elearning_model.tokeErrorCode];
            message = message == null ? elearning_model.tokeErrorCode : message;
            $('#tokeErrorText').html(message).show();
        }
        $('#registerToElearningBlock').show();
        if (elearning_model.tokenStatus == 'CLICKED') {
            var childNode1 = $("#returnToElearningBlock")[0];
            var childNode2 = $("#registerToElearningBlock")[0];
            childNode1.parentNode.insertBefore(childNode1, childNode2);
        }
    },

    eLearningRegister: function () {
        elearning_model.elearningRegister(function (){}, function (){});
        window.open(
            'https://elearning.cloud-cmas.org/blocks/coupon/view/signup.php?submissioncode='
            + elearning_model.tokenStr,
            '_blank'
        ).focus();
    },

    setListeners: function () {
        var self = this;
        $('#eLearningRegister').click(function () {
            self.eLearningRegister();
            return false;
        });
    },
};

$(document).ready(function () {
    elearning_controller.init();
});
