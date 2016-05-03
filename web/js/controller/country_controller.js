var country_controller = {

    defaultValue: "",

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $("#country").select2({
            placeholder: '<img class="country-input-ico">' + labels["cmas.face.registration.form.label.country"],
            escapeMarkup: function (m) {
                return m;
            },
            templateSelection: self.formatCountry
        });
        $('#country').select2("val", this.defaultValue);
    },

    formatCountry: function (state) {
        if (!state.id) {
            return state.text;
        }
        return $(
            '<span><img class="country-input-ico">' + state.text + '</span>'
        );
    }
};

$(document).ready(function () {
    country_controller.init();
});
