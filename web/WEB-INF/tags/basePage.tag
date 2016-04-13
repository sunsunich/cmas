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
<%@ attribute name="customScripts" required="false" %>
<%@ attribute name="customCSSFiles" required="false" %>
<%@ attribute name="doNotDoAuth" required="false" %>

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
    <meta name="generator" content="Eplos CMS 3.0"/>
    <meta name="robots" content="index, follow"/>
    <base href="/"/>
    <meta itemprop="name" content="World Underwater Federation - Quality in diving"/>
    <meta itemprop="description"
          content="In addition to organizing international underwater sport events it is at the forefront of technical and scientific research and development. It can be associated with elaborating one of the oldest and most extensive dive training system."/>
    <meta property="og:title" content="World Underwater Federation - Quality in diving"/>
    <meta property="og:image" content="php_images/jutland_2016_banner_v3-450x150.jpg"/>
    <link rel="shortcut icon" type="image/x-icon" href="i/favicon.ico"/>
    <link rel="apple-touch-icon" href="i/apple-touch-icon.png">
    <link rel="stylesheet" type="text/css" href="c/loader.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/styles.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/form.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/buttons.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/select2.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/jquery-ui.min.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/jquery-ui.structure.min.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/jquery-ui.theme.min.css" media="all"/>
    <link rel="stylesheet" type="text/css" href="c/print.css" media="print"/>
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

    <script type="text/javascript" src="js/lib/json.js"></script>
    <script type="text/javascript" src="js/lib/jquery-1.12.2.min.js"></script>
    <script type="text/javascript" src="js/lib/jquery-ui.min.js"></script>
    <script type="text/javascript" src="js/lib/ejs_production.js"></script>
    <script type="text/javascript" src="js/lib/select2.full.min.js"></script>

    <script type="text/javascript" src="js/util.js"></script>

    <script type="text/javascript" src="js/controller/cookie_controller.js"></script>
    <script type="text/javascript" src="js/controller/loader_controller.js"></script>
    <script type="text/javascript" src="js/i18n/error_codes.js"></script>
    <script type="text/javascript" src="js/controller/validation_controller.js"></script>

    <c:forEach items="${customCSSFiles}" var="customCSSFile">
        <link type="text/css" rel="stylesheet" href="${customCSSFile}"/>
    </c:forEach>

    <c:forEach items="${customScripts}" var="customScript">
        <script type="text/javascript" src="${customScript}"></script>
    </c:forEach>

</head>
<body id="body" background="${pageContext.request.contextPath}/i/background.png">

<c:if test="${doNotDoAuth == null || !doNotDoAuth}">
    <my:authorize/>
</c:if>

<div id="Wrapper" class="wrapper">       <!-- Wrapper -->

    <!-- END HEADER -->


    <div class="header" id="header">    <!-- Header -->
        <!--<div class="f"></div>-->

    </div>
    <!-- end of Header -->


    <div id="">           <!-- Content -->
        <div id="loading" class="loader" title="Please wait..."></div>
        <jsp:doBody/>

    </div>
    <!-- end of Content -->


</div>
<!-- end of Wrapper -->

<div id="Footer" class="footer_wrapper">            <!-- Footer -->

</div>
<!-- end of Footer -->

</body>
</html>