<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.billing.PaymentAddFormObject"/>

<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>
<jsp:useBean id="features" scope="request" type="java.util.List<org.cmas.entities.fin.PaidFeature>"/>
<jsp:useBean id="featuresJson" scope="request" type="java.lang.String"/>

<my:securepage title="cmas.face.payment.title"
               activeMenuItem="pay"
               customScripts="js/controller/registration_flow_controller.js,js/controller/payment_controller.js">

    <c:set var="isCmasFull"
           value="${diver.diverRegistrationStatus.name == 'DEMO' || diver.diverRegistrationStatus.name == 'CMAS_BASIC'}"/>

    <c:set var="isGuest" value="${diver.diverRegistrationStatus.name == 'GUEST'}"/>

    <script type="application/javascript">
        var features = ${featuresJson};
        <c:choose>
        <c:when test="${!isCmasFull && !isGuest}">
        var selectedFeatureIds = [];
        </c:when>
        <c:otherwise>
        var selectedFeatureIds = ['${features[0].id}'];
        </c:otherwise>
        </c:choose>
        $(document).ready(function () {
            payment_controller.init(features, selectedFeatureIds);
        });
    </script>

    <div class="content" id="Content">
        <div id="formImage" class="formImage">
           <my:advert diverRegistrationStatus="${diver.diverRegistrationStatus.name}"/>
        </div>
        <div class="formWrapper" id="formWrapper">
            <div id="paymentBlock">
                <div class="header1-text">
                    <s:message code="cmas.face.payment.header"/>
                </div>
                <c:choose>
                    <c:when test="${isCmasFull}">
                        <div class="form-description form-payment-licence">
                            <b><s:message code="cmas.face.payment.cmasLicence.price"/> ${features[0].price}</b>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="form-description">
                            <s:message code="cmas.face.payment.expireText"/> <fmt:formatDate
                                value="${diver.dateLicencePaymentIsDue}" pattern="dd/MM/yyyy"/>
                        </div>
                    </c:otherwise>
                </c:choose>
                <%--<div class="form-description">--%>
                    <%--<s:message code="cmas.face.payment.features.description"/>--%>
                <%--</div>--%>
                <%--<div class="featuresList">--%>
                    <%--<c:forEach var="feature" varStatus="step" items="${features}">--%>
                        <%--<c:if test="${!isFree && !isGuest || step.index != 0}">--%>
                            <%--<div class="form-row">--%>
                                <%--<input type="checkbox" name="paidFeature_${feature.id}" id="paidFeature_${feature.id}"--%>
                                       <%--class="css-checkbox">--%>
                                <%--<label for="paidFeature_${feature.id}"--%>
                                       <%--class="css-label radGroup1 clr">--%>
                                    <%--<span class="form-checkbox-label"><s:message code="${feature.name}"/></span>--%>
                                    <%--<span class="form-payment-price"><s:message--%>
                                            <%--code="cmas.face.payment.currency"/> ${feature.price}</span>--%>
                                    <%--<br/>--%>
                                    <%--<span class="form-feature-description secondary-text"><s:message--%>
                                            <%--code="${feature.descriptionCode}"/></span>--%>
                                <%--</label>--%>
                            <%--</div>--%>
                        <%--</c:if>--%>
                    <%--</c:forEach>--%>
                <%--</div>--%>
                <div class="form-payment-total">
                    <s:message code="cmas.face.payment.total"/> <s:message code="cmas.face.payment.currency"/> <span
                        id="total">0</span>
                    <div class="error" id="payment_error">
                        <s:hasBindErrors name="command">
                            <c:forEach items="${errors.globalErrors}" var="error">
                                <span class="error"><s:message message="${error}"/></span><br/>
                            </c:forEach>
                        </s:hasBindErrors>
                    </div>
                </div>
                <div>
                    <button class="positive-button form-item-right form-button-smaller" id="payButton">
                        <s:message code="cmas.face.payment.button"/>
                    </button>
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
        <img id="paymentImageBackground" alt="payment background" style="display: none">
    </picture>


    <!-- end of Content -->

</my:securepage>
