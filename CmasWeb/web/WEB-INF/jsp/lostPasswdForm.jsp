<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="command" scope="request" type=" org.cmas.presentation.model.recovery.LostPasswordFormObject"/>
<jsp:useBean id="captchaError" scope="request" type="java.lang.Boolean"/>
<jsp:useBean id="reCaptchaPublicKey" scope="request" type="java.lang.String"/>

<my:basePage title="cmas.face.lostPasswd.title" indexpage="false">
    <div class="content" id="Content">
        <div class="form-logo">
            <a href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/i/logo.png">
            </a>
        </div>
        <div class="form-description">
            <s:message code="cmas.face.lostPasswd.description"/>
        </div>
        <form:form id="lostPasswdForm"
                   action="${pageContext.request.contextPath}/lostPasswd.html"
                   method="POST">
            <div class="block">
                <div class="form-row">
                    <img class="email-input-ico">
                    <input id="email" type="text" name="email"
                           placeholder="<s:message code="cmas.face.login.form.label.login"/>"
                           value="${command.email}"
                            />
                </div>
                <div class="error" id="error_email">
                    <s:bind path="email">
                        <c:if test="${status.error}">
                            <form:errors path="email" htmlEscape="true"/>
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