<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.recovery.PasswordChangeFormObject"/>

<my:basePage title="cmas.face.changePasswd.form.page.title" indexpage="false"
             customScripts="js/model/recovery_model.js,js/controller/recovery_controller.js"
        >
    <div class="content" id="Content">
        <div class="form-logo">
            <a href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/i/logo.png?v=${webVersion}">
            </a>
        </div>
        <div class="form-description">
            <s:message code="cmas.face.changePasswd.form.description"/>
        </div>
        <form id="changePasswordForm" action="">
            <div class="form-row">
                <img class="pass-input-ico">
                <input id="password" type="password"
                       placeholder="<s:message code="cmas.face.changePasswd.form.label.password"/>"/>
            </div>
            <div class="error" id="error_password"></div>
            <div class="form-row">
                <img class="pass-input-ico">
                <input id="checkPassword" type="password"
                       placeholder="<s:message code="cmas.face.changePasswd.form.label.checkPassword"/>"/>
            </div>
            <div class="error" id="error_checkPassword"></div>
            <input type="hidden" id="code" name="code" value="${command.code}"/>
            <div class="error" style="display: none" id="error">
            </div>
            <div class="button-container">
                <button class="form-button enter-button" id="changePasswordButton">
                    <s:message code="cmas.face.changePasswd.form.submitText"/>
                </button>
            </div>
        </form>
        <div class="pass_link">
            <a class="link" href="${pageContext.request.contextPath}/">
                <s:message code="cmas.face.registration.form.link.login"/>
            </a>
        </div>
    </div>

    <my:dialog id="changePassword"
               title="cmas.face.changePasswd.form.page.title"
               buttonText="cmas.face.dialog.ok">
        <div class="dialog-form-row"><s:message code="cmas.face.recoveredPasswd.success.message"/></div>
    </my:dialog>

</my:basePage>
