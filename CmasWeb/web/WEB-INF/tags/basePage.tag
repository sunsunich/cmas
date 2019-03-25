<%-- Базовое обрамление для любых страниц сайта --%>
<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="myfun" uri="/WEB-INF/tld/myfun" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="lightHeader" required="false" %>
<%@ attribute name="intrenal" required="false" %>
<%@ attribute name="customScripts" required="false" %>
<%@ attribute name="customCSSFiles" required="false" %>
<%@ attribute name="doNotDoAuth" required="false" %>
<%@ attribute name="bodyId" required="false" %>
<%@ attribute name="hideFooter" required="false" %>

<c:if test="${empty bodyId}">
    <c:set var="bodyId" value="body"/>
</c:if>

<c:if test="${empty intrenal}">
    <c:set var="intrenal" value="false"/>
</c:if>

<c:if test="${empty hideFooter}">
    <c:set var="hideFooter" value="false"/>
</c:if>

<!DOCTYPE html>
<html lang="en" class="english">
<head>
    <c:set var="url">${pageContext.request.requestURL}</c:set>
    <base href="${fn:substring(url, 0, fn:length(url) - fn:length(pageContext.request.requestURI))}${pageContext.request.contextPath}/"/>

    <meta charset="utf-8"/>
    <title><s:message code="${title}"/></title>
    <meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/>
    <meta name="description"
          content="In addition to organizing international underwater sport events it is at the forefront of technical and scientific research and development. It can be associated with elaborating one of the oldest and most extensive dive training system."/>
    <meta name="distribution" content="local"/>
    <meta name="keywords" content="cmas, diving, finswimming, free diving, sport diving, sport, underwater"/>
    <meta name="copyright" content="All rights reserved"/>

    <meta name="robots" content="index, follow"/>
    <base href="/"/>
    <meta itemprop="name" content="World Underwater Federation - Quality in diving"/>
    <%--<link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,600,700" rel="stylesheet">--%>

    <link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon.png?v=${webVersion}">
    <link rel="icon" type="image/png" href="/favicon-32x32.png?v=${webVersion}" sizes="32x32">
    <link rel="icon" type="image/png" href="/favicon-194x194.png?v=${webVersion}" sizes="194x194">
    <link rel="icon" type="image/png" href="/android-chrome-192x192.png?v=${webVersion}" sizes="192x192">
    <link rel="icon" type="image/png" href="/favicon-16x16.png?v=${webVersion}" sizes="16x16">
    <link rel="manifest" href="/manifest.json?v=${webVersion}">
    <link rel="mask-icon" href="/safari-pinned-tab.svg?v=${webVersion}" color="#5bbad5">
    <link rel="shortcut icon" href="/favicon.ico?v=${webVersion}">
    <meta name="msapplication-TileColor" content="#2d89ef">
    <meta name="msapplication-TileImage" content="/mstile-144x144.png?v=${webVersion}">
    <meta name="theme-color" content="#ffffff">

    <%--
    more icons just in case
<link rel="apple-touch-icon" sizes="57x57" href="/apple-touch-icon-57x57.png?v=${webVersion}">
<link rel="apple-touch-icon" sizes="60x60" href="/apple-touch-icon-60x60.png?v=${webVersion}">
<link rel="apple-touch-icon" sizes="72x72" href="/apple-touch-icon-72x72.png?v=${webVersion}">
<link rel="apple-touch-icon" sizes="76x76" href="/apple-touch-icon-76x76.png?v=${webVersion}">
<link rel="apple-touch-icon" sizes="114x114" href="/apple-touch-icon-114x114.png?v=${webVersion}">
<link rel="apple-touch-icon" sizes="120x120" href="/apple-touch-icon-120x120.png?v=${webVersion}">
<link rel="apple-touch-icon" sizes="144x144" href="/apple-touch-icon-144x144.png?v=${webVersion}">
<link rel="apple-touch-icon" sizes="152x152" href="/apple-touch-icon-152x152.png?v=${webVersion}">
<link rel="apple-touch-icon" sizes="180x180" href="/apple-touch-icon-180x180.png?v=${webVersion}">
<link rel="icon" type="image/png" href="/favicon-32x32.png?v=${webVersion}" sizes="32x32">
<link rel="icon" type="image/png" href="/favicon-194x194.png?v=${webVersion}" sizes="194x194">
<link rel="icon" type="image/png" href="/android-chrome-192x192.png?v=${webVersion}" sizes="192x192">
<link rel="icon" type="image/png" href="/favicon-16x16.png?v=${webVersion}" sizes="16x16">
<link rel="manifest" href="/manifest.json?v=${webVersion}">
<link rel="mask-icon" href="/safari-pinned-tab.svg?v=${webVersion}" color="#5bbad5">
<link rel="shortcut icon" href="/favicon.ico?v=${webVersion}">
<meta name="msapplication-TileColor" content="#2d89ef">
<meta name="msapplication-TileImage" content="/mstile-144x144.png?v=${webVersion}">
<meta name="theme-color" content="#ffffff">

    --%>


    <link rel="stylesheet" type="text/css" href="c/select2.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/select2-bootstrap.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/jquery-ui.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/jquery-ui.structure.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/jquery-ui.theme.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/jquery.timepicker.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/checkbox.css?v=${webVersion}" media="all"/>

    <link rel="stylesheet" type="text/css" href="c/print.css?v=${webVersion}" media="print"/>
    <c:choose>
        <c:when test="${intrenal}">
            <link rel="stylesheet" type="text/css" href="c/internal-form.css?v=${webVersion}" media="all"/>
        </c:when>
        <c:otherwise>
            <link rel="stylesheet" type="text/css" href="c/form.css?v=${webVersion}" media="all"/>
        </c:otherwise>
    </c:choose>
    <link rel="stylesheet" type="text/css" href="c/buttons.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/feed.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/menu.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/loader.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/styles.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/logbook.css?v=${webVersion}" media="all"/>

    <c:forEach items="${customCSSFiles}" var="customCSSFile">
        <link type="text/css" rel="stylesheet" href="${customCSSFile}?v=${webVersion}"/>
    </c:forEach>

    <meta name="google-site-verification" content="Xaw1kGm7_ZeykPyMuWwVpY8t9_tOTAkWEaKu6aOv6i0"/>
    <meta name="creator" content="dotindot">
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <![endif]-->
    <!--[if lt IE 9]>
    <script src="js/lib/html5shiv.min.js?v=${webVersion}"></script>
    <script src="js/lib/html5shiv-printshiv.min.js?v=${webVersion}"></script>
    <![endif]-->
    <script type="text/javascript">
        function PrintMail(argName) {
            document.write('<A href="mailto:' + argName + '@cmasdata.org">' + argName + '@cmasdata.org</A>');
        }
    </script>

    <%--add localization hl param--%>

    <script type="text/javascript" src="js/lib/modernizr-custom.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/lib/json.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/lib/jquery-1.12.2.min.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/lib/jquery-ui.min.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/lib/jquery.timepicker.min.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/lib/ejs_production.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/lib/select2.full.min.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/lib/checkbox_patch.js?v=${webVersion}"></script>

    <script type="text/javascript">
        // Picture element HTML5 shiv
        document.createElement("picture");
    </script>
    <script type="text/javascript" src="js/lib/pf.intrinsic.min.js?v=${webVersion}" async=""></script>
    <script type="text/javascript" src="js/lib/picturefill.js?v=${webVersion}" async=""></script>

    <script type="text/javascript" src="js/i18n/error_codes.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/i18n/labels.js?v=${webVersion}"></script>

    <script type="text/javascript" src="js/util.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/model/basic_client.js?v=${webVersion}"></script>

    <script type="text/javascript" src="js/controller/cookie_controller.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/controller/loader_controller.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/controller/validation_controller.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/controller/error_dialog_controller.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/controller/util_controller.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/controller/menu_controller.js?v=${webVersion}"></script>

    <c:forEach items="${customScripts}" var="customScript">
        <script type="text/javascript" src="${customScript}?v=${webVersion}"></script>
    </c:forEach>

    <script type="application/javascript">
        labels["cmas.face.findDiver.form.page.title"] = '<s:message code="cmas.face.findDiver.form.page.title"/>';
        <c:if test="${webVersion != null}">
        var webVersion = ${webVersion};
        </c:if>
        var imagesData = {
            userpicRoot: "${userpicRoot}",
            logbookPicRoot: "${logbookEntryImagesRoot}",
            cardsPicRoot: "${cardsRoot}"
        };
    </script>

</head>
<body id="${bodyId}">

<%--<div class="banner" onclick="window.location = 'http://www.cmasdata.org'">--%>
    <%--<img src="/i/banner-logo.png" class="banner-logo-image">--%>
    <%--<div class="banner-header">JOIN CMAS AQUA LINK--%>
    <%--</div>--%>
<%--</div>--%>
<!-- stylesheet for this button -->
<style type="text/css">
    .banner {
        backface-visibility: hidden;
        position: absolute;
        left: 50%;
        transform: translate(-50%, 0);
        z-index: 900;
        cursor: pointer;
        display: inline-block;
        white-space: nowrap;
        background: linear-gradient(180deg, #ffd616 0%, #fda315 100%);
        border: 0;
        padding: 28px 32px 32px 114px;
        box-shadow: inset 0 1px 0 rgba(100%, 100%, 100%, 0.6), 0 1px 2px rgba(0%, 0%, 0%, 0.5);
        color: #333333;
        font-family: 'Source Sans Pro';
    }

    .banner:hover {
        background: linear-gradient(120deg, #fda315 0%, #ffd616 100%);
    }

    .banner:active {
        background: linear-gradient(180deg, #ffd616 0%, #fda315 100%);
        box-shadow: none;
    }

    .banner-header {
        font-size: 30px;
        text-shadow: 0 1px 0 rgba(51, 51, 51, 0.5);
        letter-spacing: 0.08em;
        font-weight: bold;
        font-style: normal;
    }

    .banner-logo-image {
        width: 60px;
        position: absolute;
        left: 35px;
        top: 50%;
        transform: translate(0, -50%);
    }
</style>


<div class="cookieWarning clearfix" id="cookieWarning">
     <span class="cookieWarningText menu-item-left menu-item menu-item-first basic-text">
         <s:message code="cmas.face.client.cookies.warning.text"/>
     </span>
    <span class="cookieWarningAccept positive-button menu-item menu-item-right" id="cookieWarningOk">
        <s:message code="cmas.face.client.cookies.accept"/>
    </span>
</div>
<c:if test="${doNotDoAuth == null || !doNotDoAuth}">
    <my:authorize/>
</c:if>
<div id="overlay" class="overlay" style="display: none"></div>
<div id="Wrapper" class="wrapper">
    <jsp:doBody/>
</div>
<c:if test="${!hideFooter}">
    <div id="footer" class="footer_wrapper">
        <div class="menu-link menu-item-left menu-item footer-item-first">
            <a href="${pageContext.request.contextPath}/faq.html"><s:message code="cmas.face.client.faq"/></a>
        </div>
            <%--todo content--%>
        <div class="menu-link menu-item-left menu-item" style="visibility: hidden">
            <a href="${pageContext.request.contextPath}/paymentInfo.html">
                <s:message code="cmas.face.client.paymentInfo"/>
            </a>
        </div>
        <div class="menu-link menu-item-left menu-item" id="termsAndCond">
            <a href="${pageContext.request.contextPath}/privacyPolicy.html">
                <s:message code="cmas.face.client.termsAndCond"/>
            </a>
        </div>
            <%--todo implement--%>
        <div class="menu-link menu-item-left menu-item" id="languageChange">
            <a style="color: #FFFFFF" href="${pageContext.request.contextPath}/privacyPolicy.html">
                ENGLISH
            </a>
        </div>
        <div class="menu-text menu-item menu-item-right" id="credits">
                <span class="secondary-large-text footer-large-text">
                    <s:message code="cmas.face.client.creditsCMAS"/>
                </span><br/>
            <span class="secondary-text footer-text"><s:message code="cmas.face.client.credits"/></span>
        </div>
    </div>
</c:if>
</body>
</html>