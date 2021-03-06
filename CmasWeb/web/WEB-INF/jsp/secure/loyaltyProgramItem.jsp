<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>
<jsp:useBean id="loyaltyProgramItem" scope="request" type="org.cmas.entities.loyalty.LoyaltyProgramItem"/>
<jsp:useBean id="canOrderThisYear" scope="request" type="java.lang.Boolean"/>

<my:securepage title="cmas.face.loyalty.title"
               activeMenuItem="loyaltyProgram"
               customScripts="js/controller/registration_flow_controller.js,js/model/loyalty_program_model.js,js/controller/loyalty_program_controller.js">

    <c:set var="isDiverAllowedLoyaltyProgram"
           value="${diver.diverRegistrationStatus.name == 'CMAS_FULL' || diver.diverRegistrationStatus.name == 'GUEST'}"/>

    <c:set var="itemId" value="${loyaltyProgramItem.id}"/>

    <script type="application/javascript">
        loyalty_program_model.isDiverAllowedLoyaltyProgram = ${isDiverAllowedLoyaltyProgram};
        loyalty_program_model.itemId = "${loyaltyProgramItem.id}";
    </script>

    <div class="content" id="Content">
        <div id="formImage" class="formImage">
            <div class="advert-header">
                <s:message code="cmas.face.loyalty.item.${itemId}.header"/>
            </div>
            <ul class="advert-list">
                <c:forEach begin="1" end="${loyaltyProgramItem.featuresCnt}" varStatus="i">
                    <li>
                        <s:message code="cmas.face.loyalty.item.${itemId}.feature.${i.index}"/>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <div class="formWrapper" id="formWrapper">
            <div>
                <div class="header1-text">
                    <s:message code="cmas.face.loyalty.item.${itemId}.small.header"/>
                </div>
                <div class="header1-text">
                    <span style="color: #f4225b">
                        <s:message code="cmas.face.loyalty.discount"
                                   arguments="${loyaltyProgramItem.marketPriceEuro};${loyaltyProgramItem.discountPriceEuro}"
                                   argumentSeparator=";"/>
                    </span>
                </div>
                <div>
                    <img src="${pageContext.request.contextPath}/i/loyalty/item_${itemId}.png"
                         height="260px" style="padding-top: 16px; padding-bottom: 16px"/>
                    <p style="padding-top: 24px; padding-bottom: 24px" id="cameraTextDescription">
                        <s:message code="cmas.face.loyalty.item.${itemId}.description"/>
                    </p>
                </div>
                <div>
                    <c:choose>
                        <c:when test="${isDiverAllowedLoyaltyProgram}">
                            <c:choose>
                                <c:when test="${canOrderThisYear}">
                                    <div class="form-row" id="placeOrderContainer">
                                        <span class="header2-text" style="line-height: 47px; padding-right: 16px">
                                            <s:message code="cmas.face.loyalty.camera.order.label"/>:
                                        </span>
                                        <button class="positive-button form-button-smaller" id="placeOrder">
                                            <s:message code="cmas.face.loyalty.camera.order.button"/>
                                        </button>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="form-row" id="placeOrderContainer">
                                        <span class="header2-text" style="color: #f4225b;">
                                            <s:message code="cmas.face.loyalty.camera.order.tooMany"/>
                                        </span>
                                    </div>
                                </c:otherwise>
                            </c:choose>
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
        <div><s:message code="cmas.face.loyalty.program.camera.successText"
                        arguments="${loyaltyProgramItem.name}|${diver.email}"
                        argumentSeparator="|"
        /></div>
    </my:dialog>

    <!-- end of Content -->

</my:securepage>
