<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="authz" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ff" tagdir="/WEB-INF/tags/form" %>

<%@ attribute name="title" required="false" %>
<%@ attribute name="customScripts" required="false" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="en" xml:lang="en">
<head>

    <title>Administration</title>

    <link rel="stylesheet" type="text/css" href="/c/admin.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/buttons.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/select2.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/jquery-ui.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/jquery-ui.structure.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/jquery-ui.theme.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/jquery.timepicker.min.css?v=${webVersion}" media="all"/>
    <link rel="stylesheet" type="text/css" href="/c/print.css?v=${webVersion}" media="print"/>


    <script type="text/javascript" src="/js/lib/modernizr-custom.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/lib/json.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/lib/jquery-1.12.2.min.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/lib/jquery-ui.min.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/lib/jquery.timepicker.min.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/lib/ejs_production.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/lib/select2.full.min.js"?v=${webVersion}></script>

    <script type="text/javascript" src="/js/i18n/error_codes.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/i18n/labels.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/util.js?v=${webVersion}"></script>

    <script type="text/javascript" src="/js/controller/loader_controller.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/controller/validation_controller.js?v=${webVersion}"></script>
    <script type="text/javascript" src="/js/controller/error_dialog_controller.js?v=${webVersion}"></script>

    <c:forEach items="${customScripts}" var="customScript">
        <script type="text/javascript" src="${customScript}?v=${webVersion}"></script>
    </c:forEach>
    
</head>
<body>
    <div id="Wrapper" class="wrapper">       <!-- Wrapper -->

    <!-- END HEADER -->


    <div class="header" id="header">    <!-- Header -->
        <div class="f"></div>


    </div>
    <!-- end of Header -->


    <div id="" class="content">           <!-- Content -->
        <div class="content-body">
    <table>
        <tr>
            <td><a href="/logout.html">Sign out</a>&nbsp;&nbsp;&nbsp;</td>
            <td><a href="/fed/index.html">Users</a></td>
        </tr>
    </table>


<jsp:doBody/>
    </div>
    </div> <!-- end of Content -->
    </div>
<!-- end of Wrapper -->

<div id="Footer" class="footer_wrapper">            <!-- Footer -->
    <div class="footer">
        <div class="footer_content">

        </div>
    </div>
</div>
    
    




</body>
</html>
