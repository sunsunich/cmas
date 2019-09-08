<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="command" scope="request" type="org.cmas.presentation.model.billing.PaymentAddFormObject"/>

<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>
<jsp:useBean id="features" scope="request" type="java.util.List<org.cmas.entities.loyalty.PaidFeature>"/>
<jsp:useBean id="featuresJson" scope="request" type="java.lang.String"/>
<jsp:useBean id="countries" scope="request" type="org.cmas.entities.Country[]"/>
<jsp:useBean id="genders" scope="request" type="org.cmas.entities.Gender[]"/>

<my:securepage title="cmas.face.payment.title"
               activeMenuItem="pay"
               customScripts="/js/model/insurance_request_model.js,js/controller/registration_flow_controller.js,js/controller/payment_controller.js,/js/controller/insurance_request_controller.js">
    <c:set var="isDueToPayCmasFullLicence"
           value="${diver.diverRegistrationStatus.name == 'DEMO' || diver.diverRegistrationStatus.name == 'INACTIVE' && diver.previousRegistrationStatus.name == 'DEMO' || diver.diverRegistrationStatus.name == 'CMAS_BASIC' && diver.previousRegistrationStatus.name == 'NEVER_REGISTERED'}"/>

    <c:set var="isGuest"
           value="${diver.diverRegistrationStatus.name == 'GUEST' || diver.diverRegistrationStatus.name == 'INACTIVE' && diver.previousRegistrationStatus.name == 'GUEST'}"/>

    <c:set var="hasInsurance" value="${insuranceExpiryDate != null}"/>

    <script type="application/javascript">
        var features = ${featuresJson};
        <c:choose>
        <c:when test="${isDueToPayCmasFullLicence}">
        var selectedFeatureIds = ['${features[0].id}'];
        </c:when>
        <c:otherwise>
        var selectedFeatureIds = [];
        </c:otherwise>
        </c:choose>
        $(document).ready(function () {
            payment_controller.init(features, selectedFeatureIds);
        });

        <my:enumToJs enumItems="${genders}" arrayVarName="insurance_request_model.genders"/>

        insurance_request_model.insuranceRequest.diver.id = "${diver.id}";
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
                <c:if test="${diver.diverRegistrationStatus.name == 'INACTIVE' && diver.previousRegistrationStatus.name == 'DEMO'}">
                    <div class="form-description" style="color: #f4225b">
                        <b><s:message code="cmas.face.payment.expiredText"/></b>
                    </div>
                </c:if>
                <c:if test="${diver.diverRegistrationStatus.name == 'DEMO'}">
                    <div class="form-description">
                        <s:message code="cmas.face.payment.expireText"/> <fmt:formatDate
                            value="${diver.dateLicencePaymentIsDue}" pattern="dd/MM/yyyy"/>
                    </div>
                </c:if>
                <c:if test="${hasInsurance}">
                    <div class="form-description">
                        <s:message code="cmas.loyalty.insurance.expireText"/> <fmt:formatDate
                            value="${insuranceExpiryDate}" pattern="dd/MM/yyyy"/>
                    </div>
                </c:if>
                <c:if test="${isGuest}">
                    <div class="form-description">
                        <s:message code="cmas.face.payment.cmasLicence.guest"/>
                    </div>
                </c:if>
                <c:if test="${isDueToPayCmasFullLicence}">
                    <div class="form-description">
                        <s:message code="cmas.face.first.login.form.getFullAccess.describtion"/>
                    </div>
                    <div class="form-description form-payment-licence">
                        <b><s:message code="cmas.face.payment.cmasLicence.price"/> ${features[0].price}</b>
                    </div>
                </c:if>
                <c:if test="${!hasInsurance}">
                    <div class="form-description">
                        <s:message code="cmas.face.payment.features.description"/>
                    </div>
                    <div class="featuresList">
                        <c:forEach var="feature" varStatus="step" items="${features}">
                            <c:if test="${step.index == 1 && !hasInsurance
                                                             || step.index > 1}">
                                <div class="form-row">
                                    <input type="checkbox" name="paidFeature_${feature.id}"
                                           id="paidFeature_${feature.id}"
                                           class="css-checkbox">
                                    <label for="paidFeature_${feature.id}"
                                           class="css-label radGroup1 clr">
                                                        <span class="form-checkbox-label-insurance"><s:message
                                                                code="${feature.name}"/></span>
                                        <span class="form-payment-price"><s:message
                                                code="cmas.face.payment.currency"/> ${feature.price}</span>
                                        <br/>
                                        <span class="form-feature-description-insurance"><s:message
                                                code="${feature.descriptionCode}"/></span>
                                    </label>
                                </div>
                            </c:if>
                        </c:forEach>
                    </div>
                </c:if>
                <c:if test="${isDueToPayCmasFullLicence || !hasInsurance}">
                    <div class="form-payment-total">
                        <s:message code="cmas.face.payment.total"/> <s:message code="cmas.face.payment.currency"/>
                        <span id="total">0</span>
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
                </c:if>
            </div>
            <div id="insuranceBlock" class="reg-block" style="display: none">
                <div class="header2-text">
                    <s:message code="cmas.loyalty.insurance.inputDataTitle"/>
                </div>
                <br/>
                <form>
                    <div class="form-row">
                        <select name="insuranceRequest_gender" style="width: 272px" id="insuranceRequest_gender" size=1
                                onChange="">
                        </select>
                        <img src="/i/ic_error.png" class="error-input-ico" id="insuranceRequest_error_ico_gender"
                             style="display: none">
                        <div class="error" id="insuranceRequest_error_gender"></div>
                    </div>
                    <div class="form-row">
                        <select name="insuranceRequest_country" id="insuranceRequest_country" size=1 onChange="">
                            <c:forEach items="${countries}" var="country">
                                <option value='${country.code}'>${country.name}</option>
                            </c:forEach>
                        </select>
                        <img src="/i/ic_error.png" class="error-input-ico" id="insuranceRequest_error_ico_country"
                             style="display: none">
                        <div class="error" id="insuranceRequest_error_country"></div>
                    </div>
                    <div class="form-row">
                        <input id="insuranceRequest_region" type="text"
                               placeholder="<s:message code="cmas.loyalty.insurance.address.region"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="insuranceRequest_error_ico_region"
                             style="display: none">
                        <div class="error" id="insuranceRequest_error_region"></div>
                    </div>
                    <div class="form-row">
                        <input id="insuranceRequest_zipCode" type="text"
                               placeholder="<s:message code="cmas.loyalty.insurance.address.zipCode"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="insuranceRequest_error_ico_zipCode"
                             style="display: none">
                        <div class="error" id="insuranceRequest_error_zipCode"></div>
                    </div>
                    <div class="form-row">
                        <input id="insuranceRequest_city" type="text"
                               placeholder="<s:message code="cmas.loyalty.insurance.address.city"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="insuranceRequest_error_ico_city"
                             style="display: none">
                        <div class="error" id="insuranceRequest_error_city"></div>
                    </div>
                    <div class="form-row">
                        <input id="insuranceRequest_street" type="text"
                               placeholder="<s:message code="cmas.loyalty.insurance.address.street"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="insuranceRequest_error_ico_street"
                             style="display: none">
                        <div class="error" id="insuranceRequest_error_street"></div>
                    </div>
                    <div class="form-row">
                        <input id="insuranceRequest_house" type="text"
                               placeholder="<s:message code="cmas.loyalty.insurance.address.house"/>"/>
                        <img src="/i/ic_error.png" class="error-input-ico" id="insuranceRequest_error_ico_house"
                             style="display: none">
                        <div class="error" id="insuranceRequest_error_house"></div>
                    </div>
                    <div class="form-row">
                    </div>
                    <button class="white-button form-item-left form-button-smaller" id="insuranceRequestCancel">
                        <s:message code="cmas.face.button.cancel"/>
                    </button>
                    <button class="positive-button form-item-right form-button-smaller" id="insuranceRequestSubmit">
                        <s:message code="cmas.face.logbook.submit"/>
                    </button>
                </form>
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
