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
<%@ attribute name="indexpage" required="false" %>
<%@ attribute name="intrenal" required="false" %>
<%@ attribute name="customScripts" required="false" %>
<%@ attribute name="customCSSFiles" required="false" %>
<%@ attribute name="doNotDoAuth" required="false" %>
<%@ attribute name="bodyId" required="false" %>
<%@ attribute name="hideMenu" required="false" %>

<c:if test="${empty bodyId}">
    <c:set var="bodyId" value="body"/>
</c:if>

<c:if test="${empty intrenal}">
    <c:set var="intrenal" value="false"/>
</c:if>

<c:if test="${empty hideMenu}">
    <c:set var="hideMenu" value="true"/>
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
    <meta name="keywords" content="cmas, diving, finswimming, apnea, sport diving, sport, underwater"/>
    <meta name="copyright" content="All rights reserved"/>

    <meta name="robots" content="index, follow"/>
    <base href="/"/>
    <meta itemprop="name" content="World Underwater Federation - Quality in diving"/>

    <link rel="stylesheet" type="text/css" href="c/loader.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/styles.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/logbook.css?v=${webVersion}" media="all"/>


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
    <link rel="stylesheet" type="text/css" href="c/select2.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/jquery-ui.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/jquery-ui.structure.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/jquery-ui.theme.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/jquery.timepicker.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/print.css?v=${webVersion}" media="print"/>
    <meta name="google-site-verification" content="Xaw1kGm7_ZeykPyMuWwVpY8t9_tOTAkWEaKu6aOv6i0"/>
    <meta name="creator" content="dotindot">
    <!--[if IE]>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <![endif]-->
    <!--[if lt IE 9]>
    <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <script type="text/javascript">
        function PrintMail(argName) {
            document.write('<A href="mailto:' + argName + '@cmasdata.org">' + argName + '@cmasdata.org</A>');
        }
    </script>

    <%--add localization hl param--%>
    <script src='https://www.google.com/recaptcha/api.js'></script>

    <script type="text/javascript" src="js/lib/modernizr-custom.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/lib/json.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/lib/jquery-1.12.2.min.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/lib/jquery-ui.min.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/lib/jquery.timepicker.min.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/lib/ejs_production.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/lib/select2.full.min.js"?v=${webVersion}></script>

    <script type="text/javascript" src="js/i18n/error_codes.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/i18n/labels.js?v=${webVersion}"></script>

    <script type="text/javascript" src="js/util.js?v=${webVersion}"></script>

    <script type="text/javascript" src="js/controller/cookie_controller.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/controller/loader_controller.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/controller/validation_controller.js?v=${webVersion}"></script>
    <script type="text/javascript" src="js/controller/error_dialog_controller.js?v=${webVersion}"></script>

    <c:forEach items="${customCSSFiles}" var="customCSSFile">
        <link type="text/css" rel="stylesheet" href="${customCSSFile}?v=${webVersion}"/>
    </c:forEach>

    <c:forEach items="${customScripts}" var="customScript">
        <script type="text/javascript" src="${customScript}?v=${webVersion}"></script>
    </c:forEach>

    <script type="application/javascript">
        labels["cmas.face.findDiver.form.page.title"] = '<s:message code="cmas.face.findDiver.form.page.title"/>';
    </script>

</head>
<body id="${bodyId}">

<c:if test="${doNotDoAuth == null || !doNotDoAuth}">
    <my:authorize/>
</c:if>

<div id="Wrapper" class="wrapper">

    <div class="page-header" id="header">
    </div>

    <div id="Wrapper-content">
        <div id="loading" class="loader" title="Please wait..."></div>
        <jsp:doBody/>
        <div id="Footer" class="footer_wrapper">
            <c:if test="${hideMenu}">
                <a href="${pageContext.request.contextPath}/faq.html">
                    <span><b><s:message code="cmas.face.client.faq"/></b></span>
                </a>
            </c:if>
        </div>
        <my:dialog id="errorDialog"
                   title="cmas.face.error.title"
                   buttonText="cmas.face.error.submitText">
            <div id="errorDialogText"></div>
        </my:dialog>
    </div>
</div>
</body>
</html>