<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="command" scope="request" type=" org.cmas.presentation.model.recovery.LostPasswordFormObject"/>
<jsp:useBean id="captchaError" scope="request" type="java.lang.Boolean"/>
<jsp:useBean id="reCaptchaPublicKey" scope="request" type="java.lang.String"/>

<my:nonsecurepage title="cmas.face.lostPasswd.title"
                  customScripts="js/controller/lost_password_controller.js,js/controller/registration_flow_controller.js">
    <script type="application/javascript">
        var reCaptchaOnLoadCallback = function () {
            lost_password_controller.renderRecaptcha('${reCaptchaPublicKey}');
        };

        $(document).ready(function () {
            registration_flow_controller.init('lostPassword');
        });
    </script>

    <script src="https://www.google.com/recaptcha/api.js?onload=reCaptchaOnLoadCallback&render=explicit"
            defer></script>

    <div class="content" id="Content">
        <div id="formImage" class="formImage">

        </div>
        <div class="formWrapper" id="formWrapper">
            <form:form id="lostPasswordForm"
                       action="${pageContext.request.contextPath}/lostPasswd.html"
                       method="POST">
                <div id="lostPasswordBlock">
                    <div class="header1-text">
                        <s:message code="cmas.face.lostPasswd.header"/>
                    </div>
                    <div class="form-description">
                        <s:message code="cmas.face.lostPasswd.description"/>
                    </div>
                    <div class="form-row">
                        <input id="lostPassword_email" type="text" name="email"
                               placeholder="<s:message code="cmas.face.login.form.label.login"/>"
                               value="${command.email}"
                        />
                        <img src="/i/ic_error.png" class="error-input-ico" id="lostPassword_error_ico_email"
                             style="display: none">
                        <div class="error" id="lostPassword_error_email">
                            <s:bind path="email">
                                <c:if test="${status.error}">
                                    <form:errors path="email" htmlEscape="true" delimiter=", "/>
                                </c:if>
                            </s:bind>
                        </div>
                    </div>
                    <div class="form-row">
                        <div id="lostPassword_captcha" class="capcha-block">
                        </div>
                        <div class="error" id="lostPassword_error">
                            <c:if test="${captchaError}">
                                <s:message code="cmas.face.captcha.incorrect"/>
                            </c:if>
                        </div>
                    </div>
                    <button class="white-button form-item-left form-button-bigger" id="backToLogin">
                        <s:message code="cmas.face.lostPasswd.back"/>
                    </button>
                    <button class="positive-button form-item-right form-button-smaller" id="lostPasswordSubmit">
                        <s:message code="cmas.face.registration.form.next"/>
                    </button>
                </div>
            </form:form>
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
        <img id="lostPasswordImageBackground" alt="lost password background" style="display: none">
    </picture>

</my:nonsecurepage>