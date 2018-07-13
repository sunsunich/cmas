<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.recovery.PasswordChangeFormObject"/>

<my:nonsecurepage title="cmas.face.registration.setPassword.page.title"
                  customScripts="js/controller/set_password_controller.js,js/controller/registration_flow_controller.js"
>
    <div class="content" id="Content">
        <div id="formImage" class="formImage">

        </div>
        <div class="formWrapper" id="formWrapper">
            <form id="setPasswordForm" action="">
                <div id="setPasswordBlock">
                    <div class="header1-text">
                        <s:message code="cmas.face.registration.setPassword.header"/>
                    </div>
                    <div class="form-row"></div>
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
                        <input type="hidden" id="setPassword_code" name="code" value="${command.code}"/>
                        <div class="error" id="setPassword_error">
                        </div>
                    </div>
                    <button class="positive-button form-item-right form-button-single" id="setPasswordButton">
                        <s:message code="cmas.face.changePasswd.form.submitText"/>
                    </button>
                </div>
            </form>
        </div>
    </div>

    <picture>
        <!--[if IE 9]>
        <video style="display: none;">
        <![endif]-->
        <source srcset="/i/requestImage@3x.png 1x"
                media="(min-width: 3000px)">
        <source srcset="/i/requestImage@2x.png"
                media="(min-width: 2000px)">
        <source srcset="/i/requestImage.png 1x, /i/requestImage@2x.png 2x, /i/requestImage@3x.png 3x"
                media="(min-width: 631px)">
        <source srcset="/i/requestImageMob@2x.png" media="(min-width: 500px)">
        <source srcset="/i/requestImageMob.png 1x, /i/requestImageMob@2x.png 2x, /i/requestImageMob@3x.png 3x">

        <!--[if IE 9]></video><![endif]-->
        <img id="setPasswordImageBackground" alt="set password background" style="display: none">
    </picture>

</my:nonsecurepage>
