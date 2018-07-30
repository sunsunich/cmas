<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.user.EmailEditFormObject"/>

<my:securepage title="cmas.face.changeEmail.form.page.title"
               customScripts="js/model/profile_model.js,js/controller/registration_flow_controller.js,js/controller/change_email_controller.js"
>
    <div class="content" id="Content">
        <div id="formImage" class="formImage">
        </div>
        <div class="formWrapper" id="formWrapper">
            <form id="changeEmailForm" action="">
                <div id="changeEmailBlock">
                    <div class="header1-text">
                        <s:message code="cmas.face.changeEmail.form.header"/>
                    </div>
                    <div class="form-row"></div>
                    <div class="form-row">
                        <input id="changeEmail_password" type="password"
                               placeholder="<s:message code="cmas.face.changeEmail.form.label.password"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="changeEmail_error_ico_password"
                             style="display: none">
                        <div class="error" id="changeEmail_error_password"></div>
                    </div>
                    <div class="form-row">
                        <input id="changeEmail_email" type="text"
                               placeholder="<s:message code="cmas.face.changeEmail.form.label.newEmail"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="changeEmail_error_ico_email"
                             style="display: none">
                        <div class="error" id="changeEmail_error_email"></div>
                    </div>
                    <button class="positive-button form-item-right form-button-single" id="changeEmailButton">
                        <s:message code="cmas.face.changeEmail.form.submitText"/>
                    </button>
                </div>
                <div id="changeEmailSuccessBlock" style="display: none">
                    <div class="header1-text">
                        <s:message code="cmas.face.changeEmail.form.header"/>
                    </div>
                    <div class="form-description">
                        <s:message code="cmas.face.changeEmail.sent.message"/>
                    </div>
                    <button class="positive-button form-item-right form-button-single" id="backToMyAccountButton">
                        <s:message code="cmas.face.account.back"/>
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
        <img id="changeEmailImageBackground" alt="change email background" style="display: none">
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
        <img id="changeEmailSuccessImageBackground" alt="change email success background" style="display: none">
    </picture>

</my:securepage>
