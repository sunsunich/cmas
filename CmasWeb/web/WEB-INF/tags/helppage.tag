<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<my:nonsecurepage title="cmas.face.index.header" doNotDoAuth="true" customScripts="js/controller/panel_resize_controller.js">
    <div class="content-left">
        <div class="panel panel-header">
            <div class="panel-menu-item" id="faqLink"
                 onclick="window.location = '${pageContext.request.contextPath}/faq.html';"
            >
                <a href="${pageContext.request.contextPath}/faq.html"><s:message code="cmas.face.client.faq"/></a>
            </div>
            <div class="panel-menu-item" id="termsAndCondLink"
                 onclick="window.location = '${pageContext.request.contextPath}/termsAndCond.html';"
            >
                <a href="${pageContext.request.contextPath}/termsAndCond.html">
                    <s:message code="cmas.face.client.termsAndCondHeader"/>
                </a>
            </div>
            <div class="panel-menu-item" id="cookiesLink"
                 onclick="window.location='${pageContext.request.contextPath}/cookies.html';"
            >
                <a href="${pageContext.request.contextPath}/cookies.html">
                    <s:message code="cmas.face.client.cookies"/>
                </a>
            </div>
            <div class="panel-menu-item" id="contactsLink"
                 onclick=" window.location='${pageContext.request.contextPath}/contacts.html';"
            >
                <a href="${pageContext.request.contextPath}/contacts.html">
                    <s:message code="cmas.face.client.contact"/>
                </a>
            </div>
        </div>
    </div>
    <div class="content-right content-center">
        <div class="panel help-center-panel">
            <jsp:doBody/>
        </div>
    </div>

    <script type="application/javascript">
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
    </script>

</my:nonsecurepage>
