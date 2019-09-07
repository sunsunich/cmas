var landing_page_controller = {

    textChangeInterval: null,

    init: function () {
        country_controller.init();
        this.setListeners();
        this.resetTextChangeInterval();
    },

    setListeners: function () {
        var self = this;
        $(window).load(function () {
            self.onResize();
        });
        $(window).resize(function () {
            self.onResize();
        });
        $('#findDiver').click(function () {
            window.location = "/diver-verification.html?name=" + $('#name').val() + '&country=' + $('#country').val();
        });
        $('#frontText1, #frontText2').each(function () {
            $(this).hover(function () {
                if (self.textChangeInterval) {
                    clearTimeout(self.textChangeInterval);
                    self.textChangeInterval = null;
                }
            }, function () {
                if (!self.textChangeInterval) {
                    self.resetTextChangeInterval();
                }
            }).click(function () {
                self.toggleTextVisibility();
            });
        });
    },

    resetTextChangeInterval: function () {
        var self = this;
        this.textChangeInterval = setTimeout(function run() {
            self.toggleTextVisibility(true);
        }, 5000);
    },

    toggleTextVisibility: function (resetInterval) {
        if ($('#frontText1').is(":visible")) {
            $('#frontText1').hide();
            $('#frontText2').fadeToggle("slow");
        } else {
            $('#frontText2').hide();
            $('#frontText1').fadeToggle("slow");
        }
        if (resetInterval) {
            this.resetTextChangeInterval();
        }
    },


    onResize: function () {
        var firstScreenElem = $('#firstScreen');
        var landingPageTextPartElem = $('#landingPageTextPart');
        var landingPageHeaderTextElem = $('#landingPageHeaderText');
        var frontText1Elem = $('#frontText1');
        var joinButtonElem = $('#joinButton');
        var interfaceExamplesElem = $('#interfaceExamples');
        var bigExampleBackgroundElem = $('#bigExampleBackground');
        var smallExampleBackgroundElem = $('#smallExampleBackground');

        var viewPortHeight = $(window).height();
        var viewPortWidth = $(window).width();

        var secondScreenVisiblePart = viewPortHeight > 380 ? viewPortWidth * 5 / 100 + 20 : 0;
        var firstScreenViewPortHeight = viewPortHeight - secondScreenVisiblePart;
        var minViewPortSize = Math.min(parseFloat(firstScreenViewPortHeight), parseFloat(viewPortWidth));
        var pagePaddingTop = minViewPortSize * 5 / 100;
        firstScreenViewPortHeight -= pagePaddingTop;
        minViewPortSize = Math.min(parseFloat(firstScreenViewPortHeight), parseFloat(viewPortWidth));

        var fontCompressor = 3.4;
        var buttonCompressor = 3;

        firstScreenElem.css('padding-top', pagePaddingTop);

        adjustCssProperty("height", "#landingPageLogo", minViewPortSize, 1, 30, 74);

        adjustCssProperty("font-size", "#landingPageHeader", minViewPortSize, 1.4, 32, 70);
        adjustCssProperty("font-size", ".adjustableText", minViewPortSize, fontCompressor, 12, 18);
        adjustCssProperty("font-size", ".adjustableButton", minViewPortSize, buttonCompressor, 12, 18);

        adjustCssProperty("padding-top", ".adjustableButton", minViewPortSize, buttonCompressor, 10, 16);
        adjustCssProperty("padding-bottom", ".adjustableButton", minViewPortSize, buttonCompressor, 10, 16);
        adjustCssProperty("padding-left", ".adjustableButton", minViewPortSize, buttonCompressor, 15, 24);
        adjustCssProperty("padding-right", ".adjustableButton", minViewPortSize, buttonCompressor, 15, 24);

        frontText1Elem.css('width', landingPageHeaderTextElem.width() * 1.9);
        $('#frontText2').css('width', landingPageHeaderTextElem.width() * 1.9);

        $('#frontTextContainer').css('height', frontText1Elem.height())
            .css('width', landingPageHeaderTextElem.width());

        $('#landingPageHeader').css('margin-top', minViewPortSize * 1 / 100)
            .css('margin-bottom', minViewPortSize * 1 / 100);
        var marginCoefficient;
        if (minViewPortSize > 600) {
            marginCoefficient = 15 / 100;
        }
        else {
            // marginCoefficient = (1.78 * 0.56 / 1.22 / 4) * firstScreenViewPortHeight / viewPortWidth + 5 / 16 - 1.78 / 1.22 / 4;
            marginCoefficient = (1 * 0.56 / 0.44 / 4) * firstScreenViewPortHeight / viewPortWidth + 5 / 16 - 1 / 0.44 / 4;
            marginCoefficient = Math.max(0.04, Math.min(marginCoefficient, 0.35));
        }
        $('#landingPageText').css('margin-top', minViewPortSize * marginCoefficient)
            .css('margin-bottom', minViewPortSize * 4.7 / 100);
        // $('#landingPageText').detach();
        // $('#landingPageText').insertAfter($('#frontTextContainer'));

        adjustCssProperty("margin-right", "#joinButton", minViewPortSize, buttonCompressor, 10, 20);

        $('#signInButton').css('width',
            joinButtonElem.width() +
            parseFloat(joinButtonElem.css('padding-left')) +
            parseFloat(joinButtonElem.css('padding-right'))
        );
        replaceBackgroundFullWindow("firstScreen", "firstScreenBackground", pagePaddingTop + secondScreenVisiblePart);
        var textPartFullSize = parseFloat(landingPageTextPartElem.width()) +
            parseFloat(landingPageTextPartElem.css("margin-right")) +
            parseFloat(landingPageTextPartElem.css("margin-left"));
        if (viewPortWidth < 600 || textPartFullSize / 64 * 100 > viewPortWidth) {
            interfaceExamplesElem.hide();
        } else {
            interfaceExamplesElem.show();
        }
        bigExampleBackgroundElem.css('width', viewPortWidth * 34 / 100);

        smallExampleBackgroundElem.css('width', viewPortWidth * 25 / 100);

        var smallImageLeft = textPartFullSize - 0.062 * viewPortWidth;
        var smallImageTop =
            parseFloat(pagePaddingTop) +
            parseFloat(bigExampleBackgroundElem.height()) -
            parseFloat(smallExampleBackgroundElem.height());
        $('#smallExampleBackgroundWrapper').css("left", smallImageLeft)
            .css("top", smallImageTop)
            .css("height", firstScreenViewPortHeight + pagePaddingTop - smallImageTop);

        // second screen
        adjustCssProperty("font-size", "#secondScreenHeader", minViewPortSize, 1.4, 22, 48);

        $('#name').css('width', Math.max(230, viewPortWidth * 31 / 100));
        adjustCssProperty("margin-right", "#name", viewPortWidth, 1, 16, 40);
        adjustCssProperty("margin-top", "#name", viewPortHeight, 3, 16, 40);

        $('.select2').css('width', Math.max(230, viewPortWidth * 31 / 100));
        adjustCssProperty("margin-right", ".select2", viewPortWidth, 1, 16, 40);
        adjustCssProperty("margin-top", ".select2", viewPortHeight, 3, 16, 40);

        adjustCssProperty("padding-left", "#findDiver", minViewPortSize, 1, 24, viewPortWidth * 5.7 / 100);
        adjustCssProperty("padding-right", "#findDiver", minViewPortSize, 1, 24, viewPortWidth * 5.7 / 100);
        adjustCssProperty("margin-right", "#findDiver", viewPortWidth, 1, 16, 40);
        adjustCssProperty("margin-top", "#findDiver", viewPortHeight, 3, 16, 40);

        //3rd screen
        var insuranceIllustrationElem = $('#insuranceIllustration');
        var insuranceTextPartElem = $('#insuranceTextPart');

        var maxWidthFullDisplay = 639;
        var relativeWidth = viewPortWidth > maxWidthFullDisplay ? viewPortWidth : viewPortWidth * 2;
        adjustCssProperty("font-size", ".secondaryHeader", relativeWidth, 3.2, 18, 40);
        adjustCssProperty("margin-bottom", ".secondaryHeader", relativeWidth, 3, 16, 40);

        adjustCssProperty("font-size", ".secondaryText", relativeWidth, 6, 12, 18);
        adjustCssProperty("margin-bottom", "#insuranceText", relativeWidth, 3, 24, 58);
        adjustCssProperty("font-size", "#insuranceLink", relativeWidth, 6, 12, 14);
        $('#insuranceLink a').hover(function () {
            $('#insuranceLinkArrow').attr('src', '/i/ic_see-more_hover.png').height(8).width(19.6);
        }, function () {
            $('#insuranceLinkArrow').attr('src', '/i/ic_see-more.png').height(8).width(28);
        });

        if (viewPortWidth > maxWidthFullDisplay) {
            insuranceIllustrationElem.show();
            insuranceTextPartElem.css('width', '31%')
                .css('height', insuranceIllustrationElem.height() - parseFloat(insuranceTextPartElem.css('padding-top')))
                .css('padding-bottom', 0);
        } else {
            insuranceIllustrationElem.hide();
            insuranceTextPartElem.css('width', '81%')
                .css('height', '95%')
                .css('padding-bottom', parseFloat(insuranceTextPartElem.css('padding-top')));
        }
        replaceBackground('insuranceTextPart', 'insuranceTextBackground');

        //features
        var featureItemWidth = relativeWidth / 4 - 0.25;
        $('.featureIllustration').css('width', featureItemWidth);
        adjustCssProperty("font-size", ".featureText", relativeWidth, 8, 12, 18);
        adjustCssProperty("padding-left", ".featureDescription", relativeWidth, 8, 26, 80);
        adjustCssProperty("padding-right", ".featureDescription", relativeWidth, 8, 24, 72);
        adjustCssProperty("font-size", ".featureHeader", relativeWidth, 6, 12, 18);
        adjustCssProperty("margin-bottom", ".featureHeader", relativeWidth, 8, 15, 46);
        var certificatesDescriptionElem = $('#certificatesDescription');
        var paddings = parseFloat(certificatesDescriptionElem.css('padding-left')) + parseFloat(certificatesDescriptionElem.css('padding-right'));
        $('.featureDescription').css('width', featureItemWidth - paddings);
        var maxHeight = Math.max(
            parseFloat($('#certificatesHeader').height()) +
            parseFloat($('#certificatesHeader').css("margin-bottom")) +
            parseFloat($('#certificatesText').height()),
            parseFloat($('#buddiesHeader').height()) +
            parseFloat($('#buddiesHeader').css("margin-bottom")) +
            parseFloat($('#buddiesText').height()),
            parseFloat($('#spotsHeader').height()) +
            parseFloat($('#spotsHeader').css("margin-bottom")) +
            parseFloat($('#spotsText').height()),
            parseFloat($('#memoriesHeader').height()) +
            parseFloat($('#memoriesHeader').css("margin-bottom")) +
            parseFloat($('#memoriesText').height())
        );
        $('.featureDescription').css('height', maxHeight);
        adjustCssProperty("padding-top", ".featureDescription", relativeWidth, 8, 24, 72);
        adjustCssProperty("padding-bottom", ".featureDescription", relativeWidth, 8, 21, 64);

        //4th screen
        replaceBackground('fourthScreen', 'insuranceTextBackground');
        $('#submissionText').css('width', relativeWidth * 27 / 100);

        adjustCssProperty("margin-bottom", "#submissionText", relativeWidth, 3, 16, 110);

        adjustCssProperty("font-size", "#bottomJoinButton", relativeWidth, 6, 12, 18);
        adjustCssProperty("padding-top", "#bottomJoinButton", relativeWidth, buttonCompressor, 10, 22);
        adjustCssProperty("padding-bottom", "#bottomJoinButton", relativeWidth, buttonCompressor, 10, 22);
        adjustCssProperty("padding-left", "#bottomJoinButton", relativeWidth, buttonCompressor, 15, 60);
        adjustCssProperty("padding-right", "#bottomJoinButton", relativeWidth, buttonCompressor, 15, 60);
        adjustCssProperty("margin-bottom", "#bottomJoinButton", relativeWidth, 3, 24, 110);

        $('#submissionBackground').css('width', Math.min(583, relativeWidth * 45 / 100));

        //footer
        replaceBackground('footer', 'firstScreenFooter');
    }

};

$(document).ready(function () {
    menu_controller.firstItemClass = 'footer-item-first';
    menu_controller.itemClass = 'footer-item';
    menu_controller.linkClass = 'footer-link';
    menu_controller.init();
    landing_page_controller.init();
});