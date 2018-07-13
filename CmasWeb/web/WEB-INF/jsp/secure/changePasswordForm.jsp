<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.recovery.PasswordChangeFormObject"/>

<my:securepage title="cmas.face.changePasswd.form.page.title"
                  customScripts="js/model/recovery_model.js,js/controller/recovery_controller.js,js/controller/registration_flow_controller.js"
>
    <script type="application/javascript">
        $(document).ready(function () {
            registration_flow_controller.init('changePassword');
        });
    </script>

    <div class="content" id="Content">
        <div id="formImage" class="formImage">

        </div>
        <div class="formWrapper" id="formWrapper">
            <form id="changePasswordForm" action="">
                <div id="changePasswordBlock">
                    <div class="header1-text">
                        <s:message code="cmas.face.changePasswd.form.header"/>
                    </div>
                    <div class="form-row"></div>
                    <div class="form-row">
                        <input id="changePassword_password" type="password"
                               placeholder="<s:message code="cmas.face.changePasswd.form.label.password"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="changePassword_error_ico_password"
                             style="display: none">
                        <div class="error" id="changePassword_error_password"></div>
                    </div>
                    <div class="form-row">
                        <input id="changePassword_checkPassword" type="password"
                               placeholder="<s:message code="cmas.face.changePasswd.form.label.checkPassword"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="changePassword_error_ico_checkPassword"
                             style="display: none">
                        <div class="error" id="changePassword_error_checkPassword"></div>
                    </div>
                    <div class="form-row">
                        <input type="hidden" id="changePassword_code" name="code" value="${command.code}"/>
                        <div class="error" id="changePassword_error">
                        </div>
                    </div>
                    <button class="positive-button form-item-right form-button-single" id="changePasswordButton">
                        <s:message code="cmas.face.changePasswd.form.submitText"/>
                    </button>
                </div>
                <div id="changePasswordSuccessBlock" style="display: none">
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
            </form>
        </div>
    </div>

    <picture>
        <!--[if IE 9]>
        <video style="display: none;">
        <![endif]-->
        <source srcset="/i/changePasswordImage@3x.png 1x"
                media="(min-width: 3000px)">
        <source srcset="/i/changePasswordImage@2x.png"
                media="(min-width: 2000px)">
        <source srcset="/i/changePasswordImage.png 1x, /i/changePasswordImage@2x.png 2x, /i/changePasswordImage@3x.png 3x"
                media="(min-width: 631px)">
        <source srcset="/i/changePasswordImageMob@2x.png" media="(min-width: 500px)">
        <source srcset="/i/changePasswordImageMob.png 1x, /i/changePasswordImageMob@2x.png 2x, /i/changePasswordImageMob@3x.png 3x">

        <!--[if IE 9]></video><![endif]-->
        <img id="changePasswordImageBackground" alt="change password background" style="display: none">
    </picture>
    <picture>
        <!--[if IE 9]>
        <video style="display: none;">
        <![endif]-->
        <source srcset="/i/changePasswordSuccessImage@3x.png 1x"
                media="(min-width: 3000px)">
        <source srcset="/i/changePasswordSuccessImage@2x.png"
                media="(min-width: 2000px)">
        <source srcset="/i/changePasswordSuccessImage.png 1x, /i/changePasswordSuccessImage@2x.png 2x, /i/changePasswordSuccessImage@3x.png 3x"
                media="(min-width: 631px)">
        <source srcset="/i/changePasswordSuccessImageMob@2x.png" media="(min-width: 500px)">
        <source srcset="/i/changePasswordSuccessImageMob.png 1x, /i/changePasswordSuccessImageMob@2x.png 2x, /i/changePasswordSuccessImageMob@3x.png 3x">

        <!--[if IE 9]></video><![endif]-->
        <img id="changePasswordSuccessImageBackground" alt="change password success background" style="display: none">
    </picture>

</my:securepage>
