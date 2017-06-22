var util_controller = {

    init: function () {
        $('#showDiverClose').click(function () {
            $('#showDiver').hide();
        });
        $('#showDiverOk').click(function () {
            $('#showDiver').hide();
        });
    },

    showDiver: function (elemId) {
        var diverId = elemId.split('_')[0];
        util_model.getDiver(
            diverId
            , function (json) {
                $('#showDiverContent').html(
                    new EJS({url: '/js/templates/diverDialog.ejs?v=' + webVersion}).render({"diver": json, "webVersion" : webVersion})
                );
                $('#showDiver').show();
            }
            , function (json) {
                if (json && json.hasOwnProperty("message")) {
                    error_dialog_controller.showErrorDialog(error_codes[json.message]);
                }
                else {
                    error_dialog_controller.showErrorDialog(error_codes["validation.internal"]);
                }
            });
    },

    maskInvalidInt: function (str) {
        var number = parseInt(str);
        if (isNaN(number)) {
            return null;
        }
        else {
            return number;
        }
    },

    maskInvalidFloat: function (str) {
        var number = parseFloat(str);
        if (isNaN(number)) {
            return null;
        }
        else {
            return number;
        }
    },

    filterNumbers: function (event, value, maxLength) {
        if (event.keyCode < 48 || event.keyCode > 57 || (value && value.length + 1 > maxLength)) {
            event.preventDefault();
        }
    },

    filterPositiveFloatNumberChars: function (elem, maxLength, maxNumbersAfterDot) {
        this.innerFilterFloatNumberChars(elem, maxLength, maxNumbersAfterDot, true);
    },

    filterFloatNumberChars: function (elem, maxLength, maxNumbersAfterDot) {
        this.innerFilterFloatNumberChars(elem, maxLength, maxNumbersAfterDot, false);
    },

    innerFilterFloatNumberChars: function (elem, maxLength, maxNumbersAfterDot, isPositive) {
        const element = elem;
        const oldVal = elem.val();
        setTimeout(function () {
            var newVal = element.val();
            if (!newVal) {
                return;
            }
            if (!isPositive) {
                if (newVal[0] == '-') {
                    newVal = newVal.substring(1);
                }
                if (!newVal) {
                    return;
                }
            }
            newVal = newVal.replace(',', '.');
            if (isNaN(newVal)) {
                element.val(oldVal);
            }
            var number = parseFloat(newVal) * Math.pow(10, maxNumbersAfterDot);
            //is integer
            if (number % 1 === 0) {
                if (number < 0 || number >= Math.pow(10, maxLength + maxNumbersAfterDot)) {
                    element.val(oldVal);
                }
            } else {
                element.val(oldVal);
            }
        }, 0);
    },

    setupSelect2: function (elemId, selectOptions, chosenOption, placeholder) {
        if (!chosenOption) {
            chosenOption = '';
        }
        var option = '';
        for (var i = 0; i < selectOptions.length; i++) {
            option += '<option value="' + selectOptions[i] + '"';
            if (selectOptions[i] == chosenOption) {
                option += ' selected="selected"';
            }
            option += '>' + labels[selectOptions[i]] + '</option>';
        }
        $("#" + elemId).append(option).select2({
            escapeMarkup: function (m) {
                return m;
            },
            placeholder: placeholder
        }).val(chosenOption).trigger("change");
    },

    setupDate: function (elemId) {
        $("#" + elemId).datepicker(
            {
                changeYear: true,
                changeMonth: true,
                yearRange: '-10:+0',
                maxDate: new Date(),
                defaultDate: new Date(),
                dateFormat: 'dd/mm/yy'
            }
        );
    },

    setupTime: function (elemId) {
        $("#" + elemId).timepicker(
            {
                timeFormat: 'HH:mm',
                dynamic: true,
                interval: 10,
                dropdown: true,
                scrollbar: true
            }
        );
    }
};

$(document).ready(function () {
    util_controller.init();
});
