var panel_resize_controller = {

    init: function () {
        this.setListeners();
    },

    setListeners: function () {
        var self = this;
        $(window).load(function () {
            self.onResize();
        });
        $(window).resize(function () {
            self.onResize();
        });
    },

    onResize: function () {
        const paddingCompressor = 2;
        const marginsCompressor = 8;
        var totalWidth = $(window).width();
        var isTimeToShowUnder = totalWidth < 721;
        var widthCompressor;
        if (isTimeToShowUnder) {
            $('.content-right').css("padding-top", "0");
            widthCompressor = 0.11;
        } else {
            $('.content-right').css("padding-top", "50px");
            widthCompressor = 0.15;
        }
        adjustCssProperty("left", ".content-left", totalWidth, marginsCompressor, 8, totalWidth * 0.1);
        adjustCssProperty("left", ".content-right", totalWidth, marginsCompressor, 8, totalWidth * 0.1);
        adjustCssProperty("margin-right", ".content-left", totalWidth, marginsCompressor, 8, 32);

        adjustCssProperty("width", ".content-center", totalWidth, widthCompressor, 285, 1200);

        adjustCssProperty("padding-left", ".panel", totalWidth, paddingCompressor, 8, 40);
        adjustCssProperty("padding-right", ".panel", totalWidth, paddingCompressor, 8, 40);
    }
};
$(document).ready(function () {
    panel_resize_controller.init();
});

