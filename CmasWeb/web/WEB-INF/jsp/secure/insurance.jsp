<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="my" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="ef" tagdir="/WEB-INF/tags/external-form" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>

<jsp:useBean id="diver" scope="request" type="org.cmas.entities.diver.Diver"/>
<jsp:useBean id="isGold" scope="request" type="java.lang.Boolean"/>
<jsp:useBean id="countries" scope="request" type="org.cmas.entities.Country[]"/>
<jsp:useBean id="genders" scope="request" type="org.cmas.entities.Gender[]"/>
<jsp:useBean id="insurancePrice" scope="request" type="java.math.BigDecimal"/>

<my:securepage title="cmas.face.loyalty.title"
               activeMenuItem="insurance"
               customScripts="/js/model/insurance_request_model.js,js/controller/registration_flow_controller.js,/js/controller/insurance_request_controller.js">

    <c:set var="hasInsurance" value="${insuranceExpiryDate != null}"/>

    <script type="application/javascript">
        $(document).ready(function () {
            <my:enumToJs enumItems="${genders}" arrayVarName="insurance_request_model.genders"/>

            insurance_request_model.insuranceRequest.diver.id = "${diver.id}";

            insurance_request_controller.init(${isGold}, ${hasInsurance});
        });
    </script>

    <div class="content" id="Content">
        <div id="formImage" class="formImage">
            <div class="advert-header">
                <s:message code="cmas.face.advert.registration.gold.header"/>
            </div>
            <ul class="advert-list">
                <li>
                    <s:message code="cmas.face.advert.registration.gold.elem1"/>
                </li>
            </ul>
        </div>
        <div class="formWrapper" id="formWrapper">
            <c:choose>
                <c:when test="${isGold}">
                    <c:choose>
                        <c:when test="${hasInsurance}">
                            <div id="alreadyHasInsuranceBlock">
                                <div class="form-description">
                                    <s:message code="cmas.loyalty.insurance.expireText"/> <fmt:formatDate
                                        value="${insuranceExpiryDate}" pattern="dd/MM/yyyy"/>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div id="insuranceBlock" class="reg-block">
                                <div class="header2-text">
                                    <s:message code="cmas.loyalty.insurance.inputDataTitle"/>
                                </div>
                                <br/>
                                <form>
                                    <div class="form-row">
                                        <select name="insuranceRequest_gender" style="width: 272px"
                                                id="insuranceRequest_gender"
                                                size=1
                                                onChange="">
                                        </select>
                                        <img src="/i/ic_error.png" class="error-input-ico"
                                             id="insuranceRequest_error_ico_gender"
                                             style="display: none">
                                        <div class="error" id="insuranceRequest_error_gender"></div>
                                    </div>
                                    <div class="form-row">
                                        <select name="insuranceRequest_country" id="insuranceRequest_country" size=1
                                                onChange="">
                                            <c:forEach items="${countries}" var="country">
                                                <option value='${country.code}'>${country.name}</option>
                                            </c:forEach>
                                        </select>
                                        <img src="/i/ic_error.png" class="error-input-ico"
                                             id="insuranceRequest_error_ico_country"
                                             style="display: none">
                                        <div class="error" id="insuranceRequest_error_country"></div>
                                    </div>
                                    <div class="form-row">
                                        <input id="insuranceRequest_region" type="text"
                                               placeholder="<s:message code="cmas.loyalty.insurance.address.region"/>"/>
                                        <img src="/i/ic_error.png" class="error-input-ico"
                                             id="insuranceRequest_error_ico_region"
                                             style="display: none">
                                        <div class="error" id="insuranceRequest_error_region"></div>
                                    </div>
                                    <div class="form-row">
                                        <input id="insuranceRequest_zipCode" type="text"
                                               placeholder="<s:message code="cmas.loyalty.insurance.address.zipCode"/>"/>
                                        <img src="/i/ic_error.png" class="error-input-ico"
                                             id="insuranceRequest_error_ico_zipCode"
                                             style="display: none">
                                        <div class="error" id="insuranceRequest_error_zipCode"></div>
                                    </div>
                                    <div class="form-row">
                                        <input id="insuranceRequest_city" type="text"
                                               placeholder="<s:message code="cmas.loyalty.insurance.address.city"/>"/>
                                        <img src="/i/ic_error.png" class="error-input-ico"
                                             id="insuranceRequest_error_ico_city"
                                             style="display: none">
                                        <div class="error" id="insuranceRequest_error_city"></div>
                                    </div>
                                    <div class="form-row">
                                        <input id="insuranceRequest_street" type="text"
                                               placeholder="<s:message code="cmas.loyalty.insurance.address.street"/>"/>
                                        <img src="/i/ic_error.png" class="error-input-ico"
                                             id="insuranceRequest_error_ico_street"
                                             style="display: none">
                                        <div class="error" id="insuranceRequest_error_street"></div>
                                    </div>
                                    <div class="form-row">
                                        <input id="insuranceRequest_house" type="text"
                                               placeholder="<s:message code="cmas.loyalty.insurance.address.house"/>"/>
                                        <img src="/i/ic_error.png" class="error-input-ico"
                                             id="insuranceRequest_error_ico_house"
                                             style="display: none">
                                        <div class="error" id="insuranceRequest_error_house"></div>
                                    </div>
                                    <div class="form-row">
                                        <input type="checkbox" name="termsAndCondAccepted" id="insuranceRequest_termsAndCondAccepted"
                                               class="css-checkbox">
                                        <label for="insuranceRequest_termsAndCondAccepted"
                                               class="css-label radGroup1 clr">
                                            <span class="form-checkbox-label">
                                                <s:message code="cmas.loyalty.insurance.consent.text"/>
                                            </span>
                                        </label>
                                        <div class="error" id="reg_error_termsAndCondAccepted"></div>
                                        <div class="error" id="reg_error"></div>
                                    </div>
                                    <div class="form-row">
                                    </div>
                                    <button class="white-button form-item-left form-button-smaller"
                                            id="insuranceRequestCancel">
                                        <s:message code="cmas.face.button.cancel"/>
                                    </button>
                                    <button class="positive-button form-item-right form-button-smaller"
                                            id="insuranceRequestSubmit">
                                        <s:message code="cmas.face.logbook.submit"/>
                                    </button>
                                </form>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </c:when>
                <c:otherwise>
                    <div id="insuranceInfoBlock">
                        <div class="header1-text">
                            <s:message code="cmas.loyalty.insurance.header"/>
                        </div>
                        <div class="form-description">
                            <s:message code="cmas.loyalty.insurance.text" arguments="${insurancePrice}"/>
                        </div>
                        <div class="clearfix" id="insuranceFeatureWrapper" style="display: none">
                            <div class="insurance-panel" id="insuranceFeaturePanel">
                                <my:insuranceInfo/>
                            </div>
                            <div>
                                <a class='link-in-text-flow insurance-info-close' href="#"
                                   id="insuranceFeaturePanelCloseBottom"
                                >
                                    <s:message code="cmas.loyalty.insurance.infoTable.close"/>
                                </a>
                            </div>
                        </div>
                        <div>
                            <button class="positive-button form-item-right form-button-smaller" id="becomeGold">
                                <s:message code="cmas.face.loyalty.become.gold"
                                />
                            </button>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
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

    <my:dialog id="insuranceRequestSuccess"
               title="cmas.loyalty.insurance.successTitle"
               buttonText="cmas.face.dialog.ok">
        <div><s:message code="cmas.loyalty.insurance.success"/></div>
    </my:dialog>

    <!-- end of Content -->

</my:securepage>
