<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>
<jsp:useBean id="marketPriceEuro" scope="request" type="java.math.BigDecimal"/>
<jsp:useBean id="discountPriceEuro" scope="request" type="java.math.BigDecimal"/>

<my:securepage title="cmas.face.payment.title"
               activeMenuItem="loyaltyProgram"
               customScripts="js/controller/registration_flow_controller.js,js/model/loyalty_program_model.js,js/controller/loyalty_program_controller.js">

    <c:set var="isCmasFull"
           value="${diver.diverRegistrationStatus.name == 'CMAS_FULL'}"/>

    <script type="application/javascript">
        loyalty_program_model.isDiverAllowedLoyaltyProgram = ${isCmasFull};
    </script>

    <div class="content" id="Content">
        <div id="formImage" class="formImage">
            <div class="advert-header">
                <s:message code="cmas.face.loyalty.camera.header"/>
            </div>
            <ul class="advert-list">
                <li>
                    <s:message code="cmas.face.loyalty.camera.elem1"/>
                </li>
                <li>
                    <s:message code="cmas.face.loyalty.camera.elem2"/>
                </li>
                <li>
                    <s:message code="cmas.face.loyalty.camera.elem3"/>
                </li>
                <li>
                    <s:message code="cmas.face.loyalty.camera.elem4"/>
                </li>
                <li>
                    <s:message code="cmas.face.loyalty.camera.elem5"/>
                </li>
            </ul>
        </div>
        <div class="formWrapper" id="formWrapper">
            <div>
                <div class="header1-text">
                    <s:message code="cmas.face.loyalty.camera.order.header"/>
                </div>
                <div class="header1-text">
                    <span style="color: #f4225b"><s:message code="cmas.face.loyalty.camera.discount"
                                                            arguments="${marketPriceEuro};${discountPriceEuro}"
                                                            argumentSeparator=";"/></span>
                </div>
                <div>
                    <img id="insuranceIllustration" src="${pageContext.request.contextPath}/i/camera_discount.png"
                         width="260px"/>
                    <p style="padding-top: 24px; padding-bottom: 24px" id="cameraTextDescription">
                        <s:message code="cmas.face.loyalty.camera.description"/>
                    </p>
                </div>
                <div><c:choose>
                    <c:when test="${isCmasFull}">
                        <div class="form-row"  id="placeOrderContainer">
                            <span class="header2-text" style="line-height: 47px; padding-right: 16px">
                                <s:message code="cmas.face.loyalty.camera.order.label"/>:
                            </span>
                            <button class="positive-button form-button-smaller" id="placeOrder">
                                <s:message code="cmas.face.loyalty.camera.order.button"/>
                            </button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <button class="positive-button form-item-right form-button-smaller" id="placeOrder">
                            <s:message code="cmas.face.loyalty.become.cmasFull"/>
                        </button>
                    </c:otherwise>
                </c:choose>

                </div>
            </div>
        </div>
    </div>

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
        <img id="loyaltyProgramImageBackground" alt="payment background" style="display: none">
    </picture>

    <my:dialog id="cameraOrderConsent"
               title="cmas.face.loyalty.program.consent.header">
        <div class="form-row">
            <input type="checkbox" id="cameraOrder_termsAndCondAccepted"
                   class="css-checkbox">
            <label for="cameraOrder_termsAndCondAccepted"
                   class="css-label radGroup1 clr">
                <span class="header3-text">
                    <s:message code="cmas.face.loyalty.program.consent.text"/>
                </span>
            </label>
            <div class="error" id="cameraOrder_error_termsAndCondAccepted"></div>
        </div>
    </my:dialog>

    <my:dialog id="orderPlaceSuccess"
               title="cmas.face.loyalty.program.successTitle"
               buttonText="cmas.face.dialog.ok">
        <div><s:message code="cmas.face.loyalty.program.camera.successText" arguments="${diver.email}"/></div>
    </my:dialog>

    <!-- end of Content -->

</my:securepage>
