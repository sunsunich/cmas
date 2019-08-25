<%@ tag body-content="scriptless" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ attribute name="headerCode" required="true" type="java.lang.String" %>
<%@ attribute name="command" required="true" type="org.cmas.presentation.model.PasswordFormObject" %>
<%@ attribute name="hasCode" required="true" type="java.lang.Boolean" %>
<%@ attribute name="hasSuccessBlock" required="true" type="java.lang.Boolean" %>

<div class="content" id="Content">
    <div id="formImage" class="formImage">
    </div>
    <div class="formWrapper" id="formWrapper">
        <form id="setPasswordForm" action="">
            <div id="setPasswordBlock">
                <div class="header1-text">
                    <s:message code="${headerCode}"/>
                </div>
                <div class="form-row"></div>
                <c:if test="${!hasCode}">
                    <div class="form-row">
                        <input id="setPassword_oldPassword" type="password"
                               placeholder="<s:message code="cmas.face.changePasswd.form.label.oldPassword"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="setPassword_error_ico_oldPassword"
                             style="display: none">
                        <div class="error" id="setPassword_error_oldPassword"></div>
                    </div>
                </c:if>
                <div class="form-row">
                    <input id="setPassword_password" type="password"
                           placeholder="<s:message code="cmas.face.changePasswd.form.label.password"/>"/>
                    <img src="/i/ic_error.png" class="error-input-ico" id="setPassword_error_ico_password"
                         style="display: none">
                    <div class="error" id="setPassword_error_password"></div>
                </div>
                <div class="form-row">
                    <input id="setPassword_checkPassword" type="password"
                           placeholder="<s:message code="cmas.face.changePasswd.form.label.checkPassword"/>"/>
                    <img src="/i/ic_error.png" class="error-input-ico" id="setPassword_error_ico_checkPassword"
                         style="display: none">
                    <div class="error" id="setPassword_error_checkPassword"></div>
                </div>
                <div class="form-row">
                    <c:if test="${hasCode}">
                        <input type="hidden" id="setPassword_code" name="code" value="${command.code}"/>
                    </c:if>
                    <div class="error" id="setPassword_error">
                    </div>
                </div>
                <button class="positive-button form-item-right form-button-single" id="setPasswordButton">
                    <s:message code="cmas.face.changePasswd.form.submitText"/>
                </button>
            </div>
            <c:if test="${hasSuccessBlock}">
                <div id="setPasswordSuccessBlock" style="display: none">
                    <div class="header1-text">
                        <s:message code="cmas.face.changePasswd.success.header"/>
                    </div>
                    <div class="form-description">
                        <s:message code="cmas.face.recoveredPasswd.success.message"/>
                    </div>
                    <button class="positive-button form-item-right form-button-single" id="backToLoginButton">
                        <s:message code="cmas.face.login.form.submitText"/>
                    </button>
                </div>
            </c:if>
        </form>
    </div>
</div>