<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>
<jsp:useBean id="areas" scope="request" type="org.cmas.entities.diver.AreaOfInterest[]"/>
<jsp:useBean id="mode" scope="request" type="java.lang.String"/>

<my:nonsecurepage title="cmas.face.index.header"
                  customScripts="js/model/registration_model.js,js/controller/registration_controller.js,js/model/login_model.js,js/controller/login_controller.js,js/controller/registration_flow_controller.js"
>
    <script type="application/javascript">
        <my:enumToJs enumItems="${areas}" arrayVarName="registration_model.areas"/>
        $(document).ready(function () {
            registration_flow_controller.init('${mode}');
        });
    </script>

    <div class="content" id="Content">
        <div id="formImage" class="formImage">

        </div>
        <div class="formWrapper" id="formWrapper">
            <div class="regFormTabs">
                    <span class="reg-tab" id="regTabLogin">
                        <s:message code="cmas.face.registration.form.link.login"/>
                    </span>
                <span class="reg-tab-selected" id="regTabRegister">
                        <s:message code="cmas.face.login.form.link.reg"/>
                    </span>
            </div>
            <form id="regForm" action="">
                <div class="reg-block" id="registrationBlock">
                    <div class="form-row">
                        <input id="reg_certificate" type="text"
                               placeholder="<s:message code="cmas.face.registration.form.label.certificate"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_certificate"
                             style="display: none">
                        <div class="error" id="reg_error_certificate"></div>
                    </div>
                    <div class="form-row">
                        <input type="checkbox" name="noCertificate" id="reg_noCertificate" class="css-checkbox">
                        <label for="reg_noCertificate"
                               class="css-label radGroup1 clr">
                            <span class="form-checkbox-label">
                                <s:message code="cmas.face.registration.form.label.nocertificate"/>
                            </span>
                        </label>
                    </div>
                    <div class="form-row">
                        <input id="reg_firstName" type="text"
                               placeholder="<s:message code="cmas.face.registration.form.label.firstName"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_firstName"
                             style="display: none">
                        <div class="error" id="reg_error_firstName"></div>
                    </div>
                    <div class="form-row">
                        <input id="reg_lastName" type="text"
                               placeholder="<s:message code="cmas.face.registration.form.label.lastName"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_lastName"
                             style="display: none">
                        <div class="error" id="reg_error_lastName"></div>
                    </div>
                    <div class="form-row">
                        <input id="reg_dob" type="text"
                               placeholder="<s:message code="cmas.face.registration.form.label.dob"/>"/>
                        <img src="/i/ic_calendar.png" class="error-input-ico" id="reg_ico_dob">
                        <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_dob"
                             style="display: none">
                        <div class="error" id="reg_error_dob"></div>
                    </div>
                    <div class="form-row">
                        <select name="country" id="reg_country" size=1 onChange="">
                            <c:forEach items="${countries}" var="country">
                                <option value='${country.code}'>${country.name}</option>
                            </c:forEach>
                        </select>
                        <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_country"
                             style="display: none">
                        <div class="error" id="reg_error_country"></div>
                    </div>
                    <div class="form-row">
                        <input type="checkbox" name="termsAndCondAccepted" id="reg_termsAndCondAccepted"
                               class="css-checkbox">
                        <label for="reg_termsAndCondAccepted"
                               class="css-label radGroup1 clr">
                            <span class="form-checkbox-label">
                                <s:message code="cmas.face.registration.form.label.termsAndCond"/>
                            </span>
                        </label>
                        <div class="error" id="reg_error_termsAndCondAccepted"></div>
                        <div class="error" id="reg_error"></div>
                    </div>
                    <div class="form-row">
                    </div>
                    <button class="positive-button form-item-right form-button-single" id="regSubmit">
                        <s:message code="cmas.face.registration.form.next"/>
                    </button>
                </div>
                <div class="login-block" id="loginBlock" style="display: none">
                    <div class="form-row">
                        <input id="login_email" type="text"
                               placeholder="<s:message code="cmas.face.login.form.label.login"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="login_error_ico_email"
                             style="display: none">
                        <div class="error" id="login_error_email"></div>
                    </div>
                    <div class="form-row">
                        <input id="login_password" type="password"
                               placeholder="<s:message code="cmas.face.login.form.label.password"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="login_error_ico_password"
                             style="display: none">
                        <div class="error" id="login_error_password"></div>
                    </div>
                    <div class="form-row">
                        <input type="checkbox" name="noCertificate" id="login_remember" class="css-checkbox">
                        <label for="login_remember"
                               class="css-label radGroup1 clr">
                            <span class="form-checkbox-label">
                                <s:message code="cmas.face.login.form.label.remember"/>
                            </span>
                        </label>
                        <div class="error" id="login_error">
                        </div>
                    </div>
                    <button class="white-button form-item-left form-button-bigger" id="forgotPassword">
                        <s:message code="cmas.face.login.form.link.lostPasswd"/>
                    </button>
                    <input type="submit" class="positive-button form-item-right form-button-smaller" id="loginSubmit"
                           value="<s:message code="cmas.face.login.form.submitText"/>"/>
                </div>
            </form>
            <div id="messageBlock" style="display: none">
                <div class="header1-text" id="messageHeader">
                </div>
                <div class="form-description" id="messageText">
                </div>
                <button id="messageButton" class="positive-button form-item-right form-button-smaller">
                </button>
            </div>
            <div id="chooseDiversBlock" style="display: none">
                <div class="form-description">
                    <s:message code="cmas.face.registration.form.choose.divers"/>
                </div>
                <div id="diversToChooseList" class="diver-to-choose-list">
                </div>
                <button class="white-button form-item-left form-button-bigger" id="cannotFindButton">
                    <s:message code="cmas.face.registration.form.cannotFind"/>
                </button>
                <button class="positive-button form-item-right form-button-smaller" id="foundDiverSubmit">
                    <s:message code="cmas.face.registration.form.next"/>
                </button>
            </div>
            <div id="emailConfirmationBlock" style="display: none">
                <div class="form-description">
                    <s:message code="cmas.face.registration.form.emailConfirmation"/>
                </div>
                <div class="form-row">
                    <select name="emailConfirmation_area" id="emailConfirmation_area" size=1 onChange="">
                    </select>
                </div>
                <div class="form-row">
                    <input id="emailConfirmation_email" type="text"
                           placeholder="<s:message code="cmas.face.registration.form.label.email"/>"/>
                    <img src="/i/ic_error.png" class="error-input-ico" id="emailConfirmation_error_ico_email"
                         style="display: none">
                    <div class="error" id="emailConfirmation_error_email"></div>
                </div>
                <div class="form-row">
                </div>
                <button class="white-button form-item-left form-button-smaller" id="emailConfirmationBack">
                    <s:message code="cmas.face.link.back.text"/>
                </button>
                <button class="positive-button form-item-right form-button-smaller" id="registerButton">
                    <s:message code="cmas.face.registration.form.register"/>
                </button>
            </div>
        </div>
    </div>

    <picture>
        <!--[if IE 9]>
        <video style="display: none;">
        <![endif]-->
        <source srcset="/i/regImage@3x.png 1x"
                media="(min-width: 3000px)">
        <source srcset="/i/regImage@2x.png"
                media="(min-width: 2000px)">
        <source srcset="/i/regImage.png 1x, /i/regImage@2x.png 2x, /i/regImage@3x.png 3x" media="(min-width: 631px)">
        <source srcset="/i/regImageMob@2x.png" media="(min-width: 500px)">
        <source srcset="/i/regImageMob.png 1x, /i/regImageMob@2x.png 2x, /i/regImageMob@3x.png 3x">

        <!--[if IE 9]></video><![endif]-->
        <img id="regImageBackground" alt="registration background" style="display: none">
    </picture>

    <picture>
        <!--[if IE 9]>
        <video style="display: none;">
        <![endif]-->
        <source srcset="/i/loginImage@3x.png 1x"
                media="(min-width: 3000px)">
        <source srcset="/i/loginImage@2x.png"
                media="(min-width: 2000px)">
        <source srcset="/i/loginImage.png 1x, /i/loginImage@2x.png 2x, /i/loginImage@3x.png 3x"
                media="(min-width: 631px)">
        <source srcset="/i/loginImageMob@2x.png" media="(min-width: 500px)">
        <source srcset="/i/loginImageMob.png 1x, /i/loginImageMob@2x.png 2x, /i/loginImageMob@3x.png 3x">

        <!--[if IE 9]></video><![endif]-->
        <img id="loginImageBackground" alt="login background" style="display: none">
    </picture>

</my:nonsecurepage>
