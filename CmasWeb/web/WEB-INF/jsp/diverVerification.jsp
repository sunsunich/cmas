<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="command" scope="request" type=" org.cmas.presentation.model.registration.DiverVerificationFormObject"/>
<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>
<jsp:useBean id="captchaError" scope="request" type="java.lang.Boolean"/>
<jsp:useBean id="hasUsers" scope="request" type="java.lang.Boolean"/>
<jsp:useBean id="reCaptchaPublicKey" scope="request" type="java.lang.String"/>

<my:basePage title="cmas.face.index.header"
             customScripts="js/controller/country_controller.js,https://www.google.com/recaptcha/api.js"
        >
    <script type="application/javascript">
        $(document).ready(function () {
            country_controller.init();
        });
    </script>

    <div class="content" id="Content">
        <div class="form-logo">
            <a href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/i/logo.png?v=${webVersion}">
            </a>
        </div>
        <form:form id="diverVerificationForm"
                   action="${pageContext.request.contextPath}/diver-verification-submit.html"
                   method="POST">
            <div class="reg-block">
                <div class="form-row">
                    <select name="country" id="country" style="width: 100%" size=1 onChange="">
                        <c:forEach items="${countries}" var="country">
                            <c:choose>
                                <c:when test="${command.country == country.code}">
                                    <option value='${country.code}' selected="selected">${country.name}</option>
                                </c:when>
                                <c:otherwise>
                                    <option value='${country.code}'>${country.name}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </select>
                </div>
                <div class="error" id="error_country">
                    <s:bind path="country">
                        <c:if test="${status.error}">
                            <form:errors path="country" htmlEscape="true"/>
                        </c:if>
                    </s:bind>
                </div>
                <div class="form-row">
                    <img class="name-input-ico">
                    <input id="name" type="text" name="name" value="${command.name}"
                           placeholder="<s:message code="cmas.face.registration.form.label.name"/>"/>
                </div>
                <div class="error" id="error_name">
                    <s:bind path="name">
                        <c:if test="${status.error}">
                            <form:errors path="name" htmlEscape="true"/>
                        </c:if>
                    </s:bind>
                </div>
            </div>
            <div id="" class="capcha-block">
                <ef:captcha reCaptchaPublicKey="${reCaptchaPublicKey}"/>
            </div>
            <c:if test="${captchaError}">
                <div class="error"><s:message code="cmas.face.captcha.incorrect"/></div>
            </c:if>
            <c:if test="${!hasUsers}">
                <div class="error"><s:message code="cmas.face.diver.verification.noDivers"/></div>
            </c:if>
            <div class="button-container">
                <button class="form-button enter-button" id="regSubmit">
                    <s:message code="cmas.face.lostPasswd.confirm"/>
                </button>
            </div>
        </form:form>
        <div class="pass_link">
            <a class="link" href="${pageContext.request.contextPath}/">
                <s:message code="cmas.face.registration.form.link.login"/>
            </a>
        </div>
    </div>

</my:basePage>
