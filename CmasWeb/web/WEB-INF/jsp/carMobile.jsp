<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="countries" scope="request" type="java.util.List<org.cmas.entities.Country>"/>
<jsp:useBean id="federations" scope="request" type="java.util.List<org.cmas.entities.sport.NationalFederation>"/>
<jsp:useBean id="diverTypes" scope="request" type="org.cmas.entities.diver.DiverType[]"/>
<jsp:useBean id="diverLevels" scope="request" type="org.cmas.entities.diver.DiverLevel[]"/>
<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.cards.CardApprovalRequestMobileFormObject"/>

<jsp:useBean id="cardGroups" scope="request" type="java.util.Map<java.lang.String, org.cmas.entities.cards.PersonalCard[]>"/>

<my:nonsecurepage title="cmas.face.index.header"
                  customScripts="js/model/add_card_model.js,js/controller/multiple_fileUpload_controller.js,js/controller/car_mobile_controller.js"
>
    <script type="application/javascript">
        <my:enumToJs enumItems="${diverTypes}" arrayVarName="add_card_model.diverTypes"/>
        <my:enumToJs enumItems="${diverLevels}" arrayVarName="add_card_model.diverLevels"/>
        $(document).ready(function () {
            car_mobile_controller.init();
        });
    </script>

    <div class="content" id="Content">
        <div id="formImage" class="formImage">
            <div id="registrationAdvert">
                <my:advert diverRegistrationStatus="NEVER_REGISTERED"/>
            </div>
        </div>
        <div class="formWrapper" id="formWrapper">
            <form id="regForm" action="">
                <div class="reg-block" id="registrationBlock">
                    <div class="form-row">
                        <select name="car_diverType" id="car_diverType" size=1 onChange="">
                        </select>
                        <div class="error" id="car_error_diverType"></div>
                    </div>
                    <div class="form-row">
                        <select name="car_diverLevel" id="car_diverLevel" size=1 onChange="">
                        </select>
                        <div class="error" id="car_error_diverLevel"></div>
                    </div>
                    <div class="form-row">
                        <select id="car_cardType" name="car_cardType">
                            <c:forEach var="cardGroup" items="${cardGroups}">
                                <optgroup label='<s:message code="${cardGroup.key}"/>'>
                                    <c:forEach var="cardType" items="${cardGroup.value}">
                                        <option value="${cardType}">${cardType}</option>
                                    </c:forEach>
                                </optgroup>
                            </c:forEach>
                        </select>
                        <div class="error" id="car_error_cardType"></div>
                    </div>
                    <div class="form-row">
                        <input id="car_validUntil" type="text" autocomplete="off" name="car_validUntil"
                               placeholder="<s:message code="cmas.face.card.addCard.validUntil"/>"/>
                        <img src="/i/ic_calendar.png" class="error-input-ico" id="car_ico_validUntil">
                        <img src="/i/ic_error.png" class="error-input-ico" id="car_error_ico_validUntil"
                             style="display: none">
                        <div class="error" id="car_error_validUntil"></div>
                    </div>
                    <div class="form-row">
                        <input id="car_federationCardNumber" type="text" name="car_federationCardNumber"
                               placeholder="<s:message code="cmas.face.card.number"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="car_error_ico_federationCardNumber"
                             style="display: none">
                        <div class="error" id="car_error_federationCardNumber"></div>
                    </div>
                    <div class="form-row">
                        <input id="car_token" type="text" name="car_token"
                               placeholder="Token"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="car_error_ico_token"
                             style="display: none">
                        <div class="error" id="car_error_token"></div>
                    </div>
                    <div class="form-row">
                        <select name="federationId" id="car_federationId" size=1 onChange="">
                            <c:forEach items="${federations}" var="federation">
                                <option value='${federation.id}'>${federation.name}</option>
                            </c:forEach>
                        </select>
                        <img src="/i/ic_error.png" class="error-input-ico" id="car_error_ico_federationId"
                             style="display: none">
                        <div class="error" id="car_error_federationId"></div>
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
                        <div class="error" id="card_error_images"></div>
                    </div>
                    <div id="photoListContainer" class="clearfix">
                    </div>
                    <div class="form-row">
                        <div class="error" id="card_error"></div>
                    </div>
                    <button class="positive-button form-item-right form-button-single" id="carSubmit">
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
