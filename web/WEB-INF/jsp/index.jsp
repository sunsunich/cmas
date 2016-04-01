<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<my:basePage title="World Underwater Federation - Quality in diving" indexpage="true"
             customScripts="/js/model/login_model.js,/js/controller/login_controller.js"
        >

    <div class="content" id="Content">
        <div class="login-form-logo">
            <img src="${pageContext.request.contextPath}/i/logo.png">
        </div>
        <form id="loginForm" action="">
            <div class="login-block">
                <div class="form-row">
                    <img src="${pageContext.request.contextPath}/i/mail_ico.png" class="email-input-ico">
                    <input id="loginField" type="text" class="email-input"
                           placeholder="<s:message code="cmas.face.login.form.label.login"/>"/>

                </div>
                <div class="form-row">
                    <img src="${pageContext.request.contextPath}/i/password_ico.png" class="pass-input-ico">
                    <input id="passField" type="password" class="pass-input"
                           placeholder="<s:message code="cmas.face.login.form.label.password"/>"/>
                </div>
                <div class="row-checkbox">
                    <span class="remember">
                        <input id="rememberLogin" type="checkbox"/>
                        <s:message code="cmas.face.login.form.label.remember"/>
                    </span>
                </div>
            </div>
            <div class="error" style="display: none" id="error">
                <s:message code="cmas.face.login.form.errorMessage"/>
            </div>
            <button class="form-button" id="loginSubmit">
                <s:message code="cmas.face.login.form.submitText"/>
            </button>
            <button class="form-button" id="regLink">
                <s:message code="cmas.face.login.form.link.reg"/>
            </button>
            <div class="pass_link">
                <a class="link" href="${pageContext.request.contextPath}/lostPasswdForm.html">
                    <s:message code="cmas.face.login.form.link.lostPasswd"/>
                </a>
            </div>
        </form>
    </div>

</my:basePage>
