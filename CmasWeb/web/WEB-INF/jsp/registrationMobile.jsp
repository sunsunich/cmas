<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>
<jsp:useBean id="federations" scope="request" type="java.util.List<org.cmas.entities.sport.NationalFederation>"/>
<jsp:useBean id="areas" scope="request" type="org.cmas.entities.diver.AreaOfInterest[]"/>

<my:nonsecurepage title="cmas.face.index.header"
                  customScripts="js/model/registration_model.js,js/controller/multiple_fileUpload_controller.js,js/controller/registration_mobile_controller.js"
>
    <script type="application/javascript">
        <my:enumToJs enumItems="${areas}" arrayVarName="registration_model.areas"/>
        $(document).ready(function () {
            registration_mobile_controller.init();
        });
    </script>

    <div class="content" id="Content">
        <div id="formImage" class="formImage">
            <div id="registrationAdvert">
                <my:advert diverRegistrationStatus="NEVER_REGISTERED"/>
            </div>
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
                        <input id="reg_firstName" type="text" name="firstName"
                               placeholder="<s:message code="cmas.face.registration.form.label.firstName"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_firstName"
                             style="display: none">
                        <div class="error" id="reg_error_firstName"></div>
                    </div>
                    <div class="form-row">
                        <input id="reg_lastName" type="text" name="lastName"
                               placeholder="<s:message code="cmas.face.registration.form.label.lastName"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_lastName"
                             style="display: none">
                        <div class="error" id="reg_error_lastName"></div>
                    </div>
                    <div class="form-row">
                        <input id="reg_dob" type="text" autocomplete="off" name="date_of_birth"
                               placeholder="<s:message code="cmas.face.registration.form.label.dob"/>"/>
                        <img src="/i/ic_calendar.png" class="error-input-ico" id="reg_ico_dob">
                        <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_dob"
                             style="display: none">
                        <div class="error" id="reg_error_dob"></div>
                    </div>
                    <div class="form-row">
                        <input id="reg_email" type="text" name="email"
                               placeholder="<s:message code="cmas.face.registration.form.label.email"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_email"
                             style="display: none">
                        <div class="error" id="reg_error_email"></div>
                    </div>
                    <div class="form-row">
                        <select name="countryCode" id="reg_countryCode" size=1 onChange="">
                            <c:forEach items="${countries}" var="country">
                                <option value='${country.code}'>${country.name}</option>
                            </c:forEach>
                        </select>
                        <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_countryCode"
                             style="display: none">
                        <div class="error" id="reg_error_countryCode"></div>
                    </div>
                    <div class="form-row">
                        <select name="federationId" id="reg_federationId" size=1 onChange="">
                            <c:forEach items="${federations}" var="federation">
                                <option value='${federation.id}'>${federation.name}</option>
                            </c:forEach>
                        </select>
                        <img src="/i/ic_error.png" class="error-input-ico" id="reg_error_ico_federationId"
                             style="display: none">
                        <div class="error" id="reg_error_federationId"></div>
                    </div>
                    <div class="form-row">
                        <select name="reg_area" id="reg_area" size=1 onChange="">
                        </select>
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
                    <div class="form-row">
                        <div class="clearfix">
                            <div class="logbook-form-label logbook-photo-header"><s:message
                                    code="cmas.face.card.images"/></div>

                            <div id="addImage"
                                 class="feedback-button-right positive-button logbook-button logbook-inline-button">
                                <img class="logbook-inline-button-icon"
                                     alt="error icon"
                                     src="${pageContext.request.contextPath}/i/ic_plus_white.png?v=${webVersion}"/>
                                <span class="logbook-inline-button-icon-text"><s:message
                                        code="cmas.face.card.addImage"/></span>
                            </div>
                        </div>
                        <div class="error" id="card_error_photo"></div>
                    </div>
                    <div id="photoListContainer" class="clearfix">
                    </div>
                    <div class="form-row">
                        <div class="error" id="card_error"></div>
                    </div>
                    <button class="positive-button form-item-right form-button-single" id="regSubmit">
                        <s:message code="cmas.face.registration.form.next"/>
                    </button>
                </div>
            </form>
        </div>
    </div>

    <%--    <picture>--%>
    <%--        <!--[if IE 9]>--%>
    <%--        <video style="display: none;">--%>
    <%--        <![endif]-->--%>
    <%--        <source srcset="/i/regImage@3x.png 1x"--%>
    <%--                media="(min-width: 3000px)">--%>
    <%--        <source srcset="/i/regImage@2x.png"--%>
    <%--                media="(min-width: 2000px)">--%>
    <%--        <source srcset="/i/regImage.png 1x, /i/regImage@2x.png 2x, /i/regImage@3x.png 3x" media="(min-width: 631px)">--%>
    <%--        <source srcset="/i/regImageMob@2x.png" media="(min-width: 500px)">--%>
    <%--        <source srcset="/i/regImageMob.png 1x, /i/regImageMob@2x.png 2x, /i/regImageMob@3x.png 3x">--%>

    <%--        <!--[if IE 9]></video><![endif]-->--%>
    <%--        <img id="regImageBackground" alt="registration background" style="display: none">--%>
    <%--    </picture>--%>

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

    <picture>
        <!--[if IE 9]>
        <video style="display: none;">
        <![endif]-->
        <source srcset="/i/paymentImage@3x.png 1x"
                media="(min-width: 3000px)">
        <source srcset="/i/paymentImage@2x.png"
                media="(min-width: 2000px)">
        <source srcset="/i/paymentImage.png 1x, /i/paymentImage@2x.png 2x, /i/paymentImage@3x.png 3x"
                media="(min-width: 631px)">
        <source srcset="/i/paymentImageMob@2x.png" media="(min-width: 500px)">
        <source srcset="/i/paymentImageMob.png 1x, /i/paymentImageMob@2x.png 2x, /i/paymentImageMob@3x.png 3x">

        <!--[if IE 9]></video><![endif]-->
        <img id="regImageBackground" alt="payment background" style="display: none">
    </picture>

</my:nonsecurepage>
