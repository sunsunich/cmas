<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<my:nonsecurepage title="cmas.face.index.header" doNotDoAuth="true">
    <div class="content-left">
        <div class="panel">
            <div class="panel-menu-item" id="faqLink">
                <a href="${pageContext.request.contextPath}/faq.html"><s:message code="cmas.face.client.faq"/></a>
            </div>
            <div class="panel-menu-item" id="termsAndCondLink">
                <a href="${pageContext.request.contextPath}/termsAndCond.html">
                    <s:message code="cmas.face.client.termsAndCondHeader"/></a>
            </div class="panel-menu-item">
            <div class="panel-menu-item" id="cookiesLink">
                <a href="${pageContext.request.contextPath}/cookies.html">
                    <s:message code="cmas.face.client.cookies"/>
                </a>
            </div>
            <div class="panel-menu-item" id="contactsLink">
                <a href="${pageContext.request.contextPath}/contacts.html">
                    <s:message code="cmas.face.client.contact"/>
                </a>
            </div>
        </div>
    </div>
    <div class="content-right content-center">
        <div class="help-center-panel">
            <jsp:doBody/>
        </div>
    </div>

    <script type="application/javascript">
        var onResize = function () {
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

            adjustCssProperty("padding-left", ".help-center-panel", totalWidth, paddingCompressor, 8, 40);
            adjustCssProperty("padding-right", ".help-center-panel", totalWidth, paddingCompressor, 8, 40);
            adjustCssProperty("padding-left", ".panel", totalWidth, paddingCompressor, 8, 40);
            adjustCssProperty("padding-right", ".panel", totalWidth, paddingCompressor, 8, 40);
        };
        $(document).ready(function () {
            $('#Wrapper-content').addClass("clearfix");
            if (window.location.href.indexOf("faq.html") != -1) {
                $('#faqLink').addClass('panel-menu-item-active')
            } else if (window.location.href.indexOf("termsAndCond.html") != -1) {
                $('#termsAndCondLink').addClass('panel-menu-item-active')
            } else if (window.location.href.indexOf("cookies.html") != -1) {
                $('#cookiesLink').addClass('panel-menu-item-active')
            } else if (window.location.href.indexOf("contacts.html") != -1) {
                $('#contactsLink').addClass('panel-menu-item-active')
            }
        });
        $(window).load(function () {
            onResize();
        });
        $(window).resize(function () {
            onResize();
        });
    </script>

</my:nonsecurepage>
