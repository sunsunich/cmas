<%-- Базовое обрамление для любых страниц сайта --%>
<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html itemtype="http://schema.org/Product" itemscope="" xmlns="http://www.w3.org/1999/xhtml">
<head>

    <title>${title}</title>
    <meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/>
    <meta http-equiv="X-UA-Compatible" content="IE=8"/>

    <%--<link href="/favicon.ico" rel="shortcut icon"/>--%>
    <%--<link type="text/css" rel="stylesheet" href="/c/ie_hack.css"/>--%>
    <%--<link type="text/css" rel="stylesheet" href="/c/loader.css"/>--%>


    <c:forEach items="${customCSSFiles}" var="customCSSFile">
        <link type="text/css" rel="stylesheet" href="${customCSSFile}"/>
    </c:forEach>

    <c:forEach items="${customScripts}" var="customScript">
        <script type="text/javascript" src="${customScript}"></script>
    </c:forEach>


</head>

<body id="body">

<c:if test="${doNotDoAuth == null || !doNotDoAuth}">
    <my:authorize/>
</c:if>


<div id="Wrapper" class="wrapper">       <!-- Wrapper -->


    <form id="loginForm" action="">
        <span class="close_login" id="Close_pop-up"><var class="sign_white close_icn"></var>закрыть</span>

        <div class="login-block">
            <span class="login_label">E-mail</span> <input id="loginField" type="text" class="login_in"/>
            <br/>
            <span class="login_label">Пароль</span> <input id="passField" type="password" class="login_in"/>
            <br/>
            <span class="remember"> <input id="rememberLogin" type="checkbox"/>Запомнить</span>

            <input type="submit" value="Подтвердить" class="ok"/>
        </div>

        <div class="error" style="display: none" id="error">e-mail и пароль неверны</div>
        <a class="pass_link" href="/lostPasswdForm.html">Восстановление пароля</a>
        <a class="reg_link" href="/registration.html">Регистрация </a>
    </form>


    <!-- END HEADER -->


    <div class="header" id="header">    <!-- Header -->
        <!--<div class="f"></div>-->

    </div>
    <!-- end of Header -->


    <div id="">           <!-- Content -->
        <!--<div id="loading" class="loader" title="Подождите..."></div>-->
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






