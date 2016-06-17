var country_controller = {

    defaultValue: "",
    inputId: "country",
    drawIcon: true,

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        var options = {
            escapeMarkup: function (m) {
                return m;
            }
        };
        if (this.drawIcon) {
            options.placeholder = '<img class="country-input-ico">' + labels["cmas.face.registration.form.label.country"];
            options.templateSelection = self.formatCountry;
        }
        else {
            options.placeholder = labels["cmas.face.registration.form.label.country"];
        }

        $("#" + this.inputId).select2(options);
        $("#" + this.inputId).select2("val", this.defaultValue);
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
