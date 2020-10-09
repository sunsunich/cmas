var landing_page_controller = {

    featureOver: null,

    init: function () {
        country_controller.init();
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
        $('#findDiver').click(function () {
            window.location = "/diver-verification.html?name=" + $('#name').val()
                + '&dob=' + $('#dob').val()
                + '&country=' + $('#country').val();
        });
        $('#insuranceFeature').hover(function () {
            self.showFeature('insurance');
        }, function () {
            self.hideFeature();
        }).click(function () {
            if (self.featureOver) {
                self.hideFeature(true);
            } else {
                self.showFeature('insurance');
            }
        })
        ;
        $('#insuranceFeaturePanel').hover(function () {
            self.showFeature('insurance');
        }, function () {
            self.hideFeature();
        }).click(function () {
            if (!self.isWidthThresholdReachedForFeatures(self.evalSizes())) {
                self.hideFeature(true);
            }
        })
        ;
    },

    showFeature: function (featureName) {
        if (this.featureOver) {
            $('#' + this.featureOver + 'FeaturePanel').hide();
        }
        this.featureOver = featureName;
        var interfaceExamplesElem = $('#interfaceExamples');
        interfaceExamplesElem.hide();
        var landingPageTextPartElem = $('#landingPageTextPart');
        var featurePanelElem = $('#' + featureName + 'FeaturePanel');
        var featureTextElem = $('#' + featureName + 'FeatureText');
        var sizes = this.evalSizes();
        if (this.isWidthThresholdReachedForFeatures(sizes)) {
            this.resetLandingPageTextPart();
            featurePanelElem.detach();
            featurePanelElem.insertAfter(featureTextElem);
            featurePanelElem.css('float', 'none')
                .css('max-height', $('#firstScreen').height() * 25 / 100)
            ;
        } else {
            landingPageTextPartElem.css('margin-right', 0 /*1%*/);
            featurePanelElem.detach();
            featurePanelElem.insertAfter(interfaceExamplesElem);

            featurePanelElem.css('float', 'left')
                .css('height', sizes.viewPortWidth * 46.11 / 100 - 36)
                .css('max-height', sizes.viewPortHeight * 80 / 100)
            ;
        }
        featurePanelElem.show();
    },

    resetLandingPageTextPart: function () {
        var landingPageTextPartElem = $('#landingPageTextPart');
        landingPageTextPartElem.css('margin-right', '9%');
    },

    hideFeature: function (force) {
        var featurePanelElemId = '#' + this.featureOver + 'FeaturePanel';
        if (!force && this.featureOver) {
            var featureElemId = '#' + this.featureOver + 'Feature';
            if ($(featurePanelElemId + ':hover').length != 0
                || $(featureElemId + ':hover').length != 0
            ) {
                return;
            }
        }

        this.resetLandingPageTextPart();
        $(featurePanelElemId).hide();
        this.showExamples();
        this.featureOver = null;
    },

    isWidthThresholdReachedForFeatures: function (sizes) {
        return sizes.textPartFullSize * 90 / 100 + 535 > sizes.viewPortWidth;
    },

    isWidthThresholdReachedForExamples: function (sizes) {
        return sizes.viewPortWidth < 600 || sizes.textPartFullSize / 64 * 100 > sizes.viewPortWidth;
    },

    showExamples: function () {
        var interfaceExamplesElem = $('#interfaceExamples');
        var bigExampleBackgroundElem = $('#bigExampleBackground');
        var smallExampleBackgroundElem = $('#smallExampleBackground');
        var sizes = this.evalSizes();
        if (this.isWidthThresholdReachedForExamples(sizes)) {
            interfaceExamplesElem.hide();
        } else {
            interfaceExamplesElem.show();
        }
        bigExampleBackgroundElem.css('width', sizes.viewPortWidth * 34 / 100);
        smallExampleBackgroundElem.css('width', sizes.viewPortWidth * 25 / 100);

        var smallImageLeft = sizes.textPartFullSize - 0.062 * sizes.viewPortWidth;
        var smallImageTop =
            parseFloat(sizes.pagePaddingTop) +
            parseFloat(bigExampleBackgroundElem.height()) -
            parseFloat(smallExampleBackgroundElem.height());
        $('#smallExampleBackgroundWrapper').css("left", smallImageLeft)
            .css("top", smallImageTop)
            .css("height", $('#firstScreen').height() + sizes.pagePaddingTop - smallImageTop);
    },

    evalSizes: function () {
        var viewPortHeight = $(window).height();
        var viewPortWidth = $(window).width();
        var firstScreenViewPortHeight = viewPortHeight;
        var minViewPortSize = Math.min(parseFloat(firstScreenViewPortHeight), parseFloat(viewPortWidth));
        var pagePaddingTop = minViewPortSize * 5 / 100;
        firstScreenViewPortHeight -= pagePaddingTop;
        minViewPortSize = Math.min(parseFloat(firstScreenViewPortHeight), parseFloat(viewPortWidth));
        var landingPageTextPartElem = $('#landingPageTextPart');
        var lpTextPartElemMarginRight = parseFloat(landingPageTextPartElem.css("margin-right"));
        var lpTextPartElemMarginLeft = parseFloat(landingPageTextPartElem.css("margin-left"));
        var textPartFullSize = parseFloat(landingPageTextPartElem.width()) + lpTextPartElemMarginRight + lpTextPartElemMarginLeft;

        return {
            viewPortHeight: viewPortHeight,
            viewPortWidth: viewPortWidth,
            firstScreenViewPortHeight: firstScreenViewPortHeight,
            minViewPortSize: minViewPortSize,
            pagePaddingTop: pagePaddingTop,
            textPartFullSize: textPartFullSize
        };
    },

    onResize: function () {
        var firstScreenElem = $('#firstScreen');
        var landingPageHeaderTextElem = $('#landingPageHeaderText');
        var frontTextElem = $('#frontText');
        var joinButtonElem = $('#joinButton');

        var sizes = this.evalSizes();

        var firstPageFontCompressor = 3.4;

        var fontCompressor = 3.5;
        var buttonCompressor = 3.5;

        firstScreenElem.css('padding-top', sizes.pagePaddingTop)
            .css('height', sizes.viewPortHeight * 93 / 100)
            .css('min-height', 520)
        ;

        adjustCssProperty("height", "#landingPageLogo", sizes.minViewPortSize, 1, 30, 74);

        adjustCssProperty("font-size", "#landingPageHeader", sizes.minViewPortSize, 1.4, 32, 70);
        adjustCssProperty("font-size", ".firstPageAdjustableText", sizes.minViewPortSize, firstPageFontCompressor, 12, 32);
        // $('#insuranceFeaturePanel').css("font-size", 12);
        //adjustCssProperty("line-height", ".firstPageAdjustableText", minViewPortSize, firstPageFontCompressor, 34, 32);

        adjustCssProperty("font-size", ".adjustableText", sizes.minViewPortSize, fontCompressor, 12, 18);
        adjustCssProperty("font-size", ".adjustableButton", sizes.minViewPortSize, buttonCompressor, 12, 18);

        adjustCssProperty("padding-top", ".adjustableButton", sizes.minViewPortSize, buttonCompressor, 10, 16);
        adjustCssProperty("padding-bottom", ".adjustableButton", sizes.minViewPortSize, buttonCompressor, 10, 16);
        adjustCssProperty("padding-left", ".adjustableButton", sizes.minViewPortSize, buttonCompressor, 15, 24);
        adjustCssProperty("padding-right", ".adjustableButton", sizes.minViewPortSize, buttonCompressor, 15, 24);

        frontTextElem.css('width', landingPageHeaderTextElem.width() * 2);
        /*
                $('#frontTextContainer').css('height', frontTextElem.height())
                    .css('width', landingPageHeaderTextElem.width());

                $('#landingPageHeader').css('margin-top', minViewPortSize * 1 / 100)
                    .css('margin-bottom', minViewPortSize * 1 / 100);
                var marginCoefficient;
                if (minViewPortSize > 600) {
                    marginCoefficient = 15 / 100;
                } else {
                    // marginCoefficient = (1.78 * 0.56 / 1.22 / 4) * firstScreenViewPortHeight / viewPortWidth + 5 / 16 - 1.78 / 1.22 / 4;
                    marginCoefficient = (1 * 0.56 / 0.44 / 4) * firstScreenViewPortHeight / viewPortWidth + 5 / 16 - 1 / 0.44 / 4;
                    marginCoefficient = Math.max(0.04, Math.min(marginCoefficient, 0.35));
                }
                $('#landingPageText').css('margin-top', minViewPortSize * marginCoefficient)
                    .css('margin-bottom', minViewPortSize * 4.7 / 100);
                // $('#landingPageText').detach();
                // $('#landingPageText').insertAfter($('#frontTextContainer'));
               */
        adjustCssProperty("margin-right", "#joinButton", sizes.minViewPortSize, buttonCompressor, 10, 20);
        $('#signInButton').css('width',
            joinButtonElem.width() +
            parseFloat(joinButtonElem.css('padding-left')) +
            parseFloat(joinButtonElem.css('padding-right'))
        );
        replaceBackground("firstScreen", "firstScreenBackground");
        this.showExamples();

        // second screen
        adjustCssProperty("font-size", "#secondScreenHeader", sizes.minViewPortSize, 1.4, 22, 48);

        $('#name').css('width', Math.max(230, sizes.viewPortWidth * 25 / 100));
        adjustCssProperty("margin-right", "#name", sizes.viewPortWidth, 1, 16, 40);
        adjustCssProperty("margin-top", "#name", sizes.viewPortHeight, 3, 16, 40);
        $('#dob').css('width', 230);
        $("#dob").datepicker(
            {
                changeYear: true,
                changeMonth: true,
                yearRange: '-100:-10',
                defaultDate: "-20y",
                dateFormat: 'dd/mm/yy'
            }
        );
        adjustCssProperty("margin-right", "#dob", sizes.viewPortWidth, 1, 16, 40);
        adjustCssProperty("margin-top", "#dob", sizes.viewPortHeight, 3, 16, 40);
        let dobMarginTop = parseFloat($('#dob').css('margin-top'));
        $('#ico_dob').css('top', 16 + dobMarginTop).css('left', 195);

        $('.select2').css('width', Math.max(230, sizes.viewPortWidth * 25 / 100));
        adjustCssProperty("margin-right", ".select2", sizes.viewPortWidth, 1, 16, 40);
        adjustCssProperty("margin-top", ".select2", sizes.viewPortHeight, 3, 16, 40);

        adjustCssProperty("padding-left", "#findDiver", sizes.minViewPortSize, 1, 24, sizes.viewPortWidth * 5.7 / 100);
        adjustCssProperty("padding-right", "#findDiver", sizes.minViewPortSize, 1, 24, sizes.viewPortWidth * 5.7 / 100);
        adjustCssProperty("margin-right", "#findDiver", sizes.viewPortWidth, 1, 16, 40);
        adjustCssProperty("margin-top", "#findDiver", sizes.viewPortHeight, 3, 16, 40);

        //3rd screen
        var insuranceIllustrationElem = $('#insuranceIllustration');
        var insuranceTextPartElem = $('#insuranceTextPart');

        var maxWidthFullDisplay = 639;
        var relativeWidth = sizes.viewPortWidth > maxWidthFullDisplay ? sizes.viewPortWidth : sizes.viewPortWidth * 2;
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

        if (sizes.viewPortWidth > maxWidthFullDisplay) {
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
        var buddiesHeaderElem = $('#buddiesHeader');
        var spotsHeaderElem = $('#spotsHeader');
        var memoriesHeaderElem = $('#memoriesHeader');
        var certificatesHeaderElem = $('#certificatesHeader');
        var maxHeight = Math.max(
            parseFloat(certificatesHeaderElem.height()) +
            parseFloat(certificatesHeaderElem.css("margin-bottom")) +
            parseFloat($('#certificatesText').height()),
            parseFloat(buddiesHeaderElem.height()) +
            parseFloat(buddiesHeaderElem.css("margin-bottom")) +
            parseFloat($('#buddiesText').height()),
            parseFloat(spotsHeaderElem.height()) +
            parseFloat(spotsHeaderElem.css("margin-bottom")) +
            parseFloat($('#spotsText').height()),
            parseFloat(memoriesHeaderElem.height()) +
            parseFloat(memoriesHeaderElem.css("margin-bottom")) +
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