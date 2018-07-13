var menu_controller = {

    menuThreshHoldWidth: 340,
    firstItemClass: 'menu-item-first',
    itemClass: 'menu-item',
    linkClass: 'menu-link',

    footerThreshHoldWidth: 448,
    footerFirstItemClass: 'footer-item-first',

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
        var viewPortWidth = $(window).width();

        var headerMenuElem = $('#headerMenu');
        if (headerMenuElem[0]) {
            var headerWidthLimit = isCMAS ? 700 : 550;
            if (viewPortWidth > headerWidthLimit) {
                headerMenuElem.show();
                $('#cssMenu').hide();
            } else {
                headerMenuElem.hide();
                $('#cssMenu').show();
            }
        }

        var termsAndCondElem = $("#termsAndCond");
        var languageChangeElem = $("#languageChange");

        if (viewPortWidth > this.footerThreshHoldWidth) {
            termsAndCondElem.insertBefore(languageChangeElem);
            termsAndCondElem.removeClass(this.footerFirstItemClass);
            termsAndCondElem.css('margin-left', 0);
        } else {
            languageChangeElem.insertBefore(termsAndCondElem);
            termsAndCondElem.addClass(this.footerFirstItemClass);
        }

        adjustCssProperty("width", ".logo-ico", viewPortWidth, 1, 40, 50);
        adjustCssProperty("height", ".logo-ico", viewPortWidth, 1, 40, 50);
        reverseAdjustCssProperty("padding-top", ".logo-ico", viewPortWidth, 704, 12, 17);
        reverseAdjustCssProperty("padding-bottom", ".logo-ico", viewPortWidth, 704, 12, 17);
        adjustCssProperty("margin-left", ".logo-ico", viewPortWidth, 8, 8, 44);

        var firstItemMarginLeft = viewPortWidth > this.menuThreshHoldWidth ? viewPortWidth * 2.4 / 100 : 0;
        $('.' + this.firstItemClass).css('margin-left', Math.max(0, firstItemMarginLeft));
        $('.' + this.itemClass).css('margin-right', Math.max(16, viewPortWidth * 2.8 / 100));
        adjustCssProperty("font-size",
            "." + this.linkClass + " a, ." + this.linkClass + " a:visited", viewPortWidth, 6, 12, 14);

        $('.' + this.footerFirstItemClass).css('margin-left', Math.max(16, viewPortWidth * 8.8 / 100));
        adjustCssProperty("font-size", ".footer-large-text", viewPortWidth, 6, 12, 14);
        adjustCssProperty("font-size", ".footer-text", viewPortWidth, 6, 10, 12);
    }
};
$(document).ready(function () {
    menu_controller.init();
});
