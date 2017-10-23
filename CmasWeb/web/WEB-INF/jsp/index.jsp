<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<my:basePage title="cmas.face.index.header" indexpage="true"
             customScripts="/js/model/login_model.js,/js/controller/login_controller.js"
        >

    <div class="content" id="Content">
        <div class="form-logo">
            <img src="${pageContext.request.contextPath}/i/logo.png?v=${webVersion}">
        </div>
        <form id="loginForm" action="">
            <div class="login-block">
                <div class="form-row">
                    <img class="email-input-ico">
                    <input id="loginField" type="text"
                           placeholder="<s:message code="cmas.face.login.form.label.login"/>"/>
                </div>
                <div class="form-row">
                    <img class="pass-input-ico">
                    <input id="passField" type="password"
                           placeholder="<s:message code="cmas.face.login.form.label.password"/>"/>
                </div>
                <div class="row-checkbox">
                    <span >
                        <input id="rememberLogin" type="checkbox"/>
                        <s:message code="cmas.face.login.form.label.remember"/>
                    </span>
                </div>
            </div>
            <div class="error" style="display: none" id="error">
                <s:message code="cmas.face.login.form.errorMessage"/>
            </div>
            <div class="button-container">
                <button class="form-button enter-button" id="loginSubmit">
                    <s:message code="cmas.face.login.form.submitText"/>
                </button>
                <button class="form-button reg-button" id="regLink">
                    <s:message code="cmas.face.login.form.link.reg"/>
                </button>
                <button class="form-button reg-button" id="verifyLink">
                    <s:message code="cmas.face.login.form.link.verify"/>
                </button>
            </div>
        </form>
        <div class="pass_link">
            <a class="link" href="${pageContext.request.contextPath}/lostPasswdForm.html">
                <s:message code="cmas.face.login.form.link.lostPasswd"/>
            </a>
        </div>
    </div>

</my:basePage>
