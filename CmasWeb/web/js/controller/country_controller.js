var country_controller = {

    defaultValue: "",
    inputId: "country",

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var options = {
            escapeMarkup: function (m) {
                return m;
            },
            theme: "bootstrap"
        };

        options.placeholder = labels["cmas.face.registration.form.label.country"];

        $("#" + this.inputId).select2(options)
            .val(this.defaultValue).trigger("change");
    }
};
