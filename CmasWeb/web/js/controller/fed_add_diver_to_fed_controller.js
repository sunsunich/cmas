var fed_add_diver_to_fed_controller = {
    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        $('#dob').datepicker(
            {
                changeYear: true,
                changeMonth: true,
                yearRange: '-100:-10',
                defaultDate: "-20y",
                dateFormat: 'dd/mm/yy'
            }
        ).val(dob);
    }
};

$(document).ready(function () {
    fed_add_diver_to_fed_controller.init();
});
